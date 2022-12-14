package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.DiscMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Disc;
import com.rakbow.website.util.Image.QiniuImageUtils;
import com.rakbow.website.util.common.ActionResult;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-14 23:24
 * @Description:
 */
@Service
public class DiscService {

    //region ------引入实例------

    @Autowired
    private DiscMapper discMapper;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;

    //endregion

    //region ------更删改查------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 新增碟片
     *
     * @param disc 新增的碟片
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addDisc(Disc disc) {
        discMapper.addDisc(disc);
    }

    /**
     * 获取表中所有数据
     *
     * @return disc表中所有专辑，用list封装
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Disc> getAll() {
        return discMapper.getAllDisc();
    }

    /**
     * 根据Id获取碟片
     *
     * @param id 碟片id
     * @return disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Disc getDiscById(int id) {
        return discMapper.getDiscById(id);
    }

    /**
     * 根据Id删除碟片
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void deleteDiscById(int id) {
        discMapper.deleteDiscById(id);
    }

    /**
     * 更新碟片基础信息
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateDisc(int id, Disc disc) {
        discMapper.updateDisc(id, disc);
    }

    //endregion

    //region ------数据处理------


    //endregion

    //region ------更新专辑数据------

    /**
     * 新增碟片图片
     *
     * @param id                 碟片id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addDiscImages(int id, MultipartFile[] images, JSONArray originalImagesJson, JSONArray imageInfos) throws IOException {

        //最终保存到数据库的json信息
        JSONArray finalImageJson = new JSONArray();

        //新增图片信息json
        JSONArray addImageJson = new JSONArray();

        //创建存储链接前缀
        String filePath = EntityType.DISC.getNameEn().toLowerCase() + "/" + id + "/";

        for (int i = 0; i < images.length; i++) {
            //上传图片
            ActionResult ar = qiniuImageUtils.uploadImageToQiniu(images[i], filePath);
            if (ar.state) {
                JSONObject jo = new JSONObject();
                jo.put("url", ar.data.toString());
                jo.put("nameEn", imageInfos.getJSONObject(i).getString("nameEn"));
                jo.put("nameZh", imageInfos.getJSONObject(i).getString("nameZh"));
                jo.put("type", imageInfos.getJSONObject(i).getString("type"));
                jo.put("uploadTime", CommonUtils.getCurrentTime());
                if (imageInfos.getJSONObject(i).getString("description") == null) {
                    jo.put("description", "");
                } else {
                    jo.put("description", imageInfos.getJSONObject(i).getString("description"));
                }
                addImageJson.add(jo);
            }
        }

        //汇总
        finalImageJson.addAll(originalImagesJson);
        finalImageJson.addAll(addImageJson);

        discMapper.updateDiscImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新碟片图片
     *
     * @param id     碟片id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String updateDiscImages(int id, String images) {
        discMapper.updateDiscImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    /**
     * 删除碟片图片
     *
     * @param id           碟片id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteDiscImages(int id, JSONArray deleteImages) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getDiscById(id).getImages());

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的图片只有一张，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuImageUtils.deleteImageFromQiniu(deleteImages.getJSONObject(0).getString("url"));
            if (!ar.state) {
                return ar.message;
            }
            deleteResult.add(deleteImages.getJSONObject(0).getString("url"));
        } else {
            String[] fullImageUrlList = new String[deleteImages.size()];
            for (int i = 0; i < deleteImages.size(); i++) {
                fullImageUrlList[i] = deleteImages.getJSONObject(i).getString("url");
            }
            ActionResult ar = qiniuImageUtils.deleteImagesFromQiniu(fullImageUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除图片信息json数组
        // 迭代器

        for (String s : deleteResult) {
            Iterator<Object> iterator = images.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (StringUtils.equals(itJson.getString("url"), s)) {
                    // 删除数组元素
                    iterator.remove();
                }
            }
        }
        discMapper.updateDiscImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    /**
     * 删除该碟片所有图片
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllAlbumImages(int id) {
        Disc disc = getDiscById(id);
        JSONArray images = JSON.parseArray(disc.getImages());
        String[] deleteImageKeyList = new String[images.size()];
        //图片文件名
        String deleteImageUrl = "";
        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            deleteImageUrl = image.getString("url");
            //删除七牛服务器上对应图片文件
            deleteImageKeyList[i] = deleteImageUrl;
        }
        qiniuImageUtils.deleteImagesFromQiniu(deleteImageKeyList);
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    //endregion

}
