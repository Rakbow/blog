package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.FranchiseMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.file.QiniuImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:17
 * @Description:
 */
@Service
public class FranchiseService {

    //region ------依赖注入------

    @Autowired
    private FranchiseMapper franchiseMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;

    //endregion

    //region ------曾删改查------

    //新增系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addFranchise(Franchise franchise){
        franchiseMapper.addFranchise(franchise);
        refreshRedisFranchises();
    }

    //通过id查找系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Franchise getFranchise(int id){
        return franchiseMapper.getFranchise(id);
    }

    //修改系列信息
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateFranchise(int id, Franchise franchise){
        franchiseMapper.updateFranchise(id, franchise);
        refreshRedisFranchises();
    }

    //删除系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int deleteFranchise(int id){
        return franchiseMapper.deleteFranchise(id);
    }

    /**
     * 更新描述
     *
     * @param id id
     * @param description 描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateFranchiseDescription(int id, String description) {
        franchiseMapper.updateFranchiseDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Franchise，以便保存到数据库
     *
     * @param franchiseJson franchiseJson
     * @return franchise
     * @author rakbow
     */
    public Franchise json2Franchise(JSONObject franchiseJson) {
        return JSON.to(Franchise.class, franchiseJson);
    }

    /**
     * 检测数据合法性
     *
     * @param franchiseJson franchiseJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkFranchiseJson(JSONObject franchiseJson) {
        if (StringUtils.isBlank(franchiseJson.getString("name"))) {
            return ApiInfo.FRANCHISE_NAME_EMPTY;
        }

        if (StringUtils.isBlank(franchiseJson.getString("originDate"))) {
            return ApiInfo.FRANCHISE_ORIGIN_DATE_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送数据
     *
     * @param franchiseJson franchiseJson
     * @return 处理后的 json格式数据
     * @author rakbow
     */
    public JSONObject handleFranchiseJson(JSONObject franchiseJson) {

        franchiseJson.put("originDate", franchiseJson.getDate("originDate"));

        return franchiseJson;
    }

    //endregion

    //region ------图片操作------

    /**
     * 新增图片
     *
     * @param id                 id
     * @param images             图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addFranchiseImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                              JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.FRANCHISE, images, originalImagesJson, imageInfos);

        franchiseMapper.updateFranchiseImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图片
     *
     * @param id     id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateFranchiseImages(int id, String images) {
        franchiseMapper.updateFranchiseImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.FRANCHISE.getNameZh());
    }

    /**
     * 删除图片
     *
     * @param id           id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteFranchiseImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getFranchise(id).getImages());

        JSONArray finalImageJson = qiniuImageUtils.commonDeleteImages(id, images, deleteImages);

        franchiseMapper.updateFranchiseImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.FRANCHISE.getNameZh());
    }

    //endregion

    //region ------其他操作------

    /**
     * 刷新Redis缓存中的franchises数据
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public void refreshRedisFranchises () {

        JSONArray franchiseSet = new JSONArray();
        List<Franchise> franchises = franchiseMapper.getAll();
        for (Franchise franchise : franchises) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("value", franchise.getId());
            jsonObject.put("label", franchise.getNameZh());
            franchiseSet.add(jsonObject);
        }

        redisUtil.set("franchiseSet", franchiseSet);
        //缓存时间1个月
        redisUtil.expire("franchiseSet", 2592000);

    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getFranchisesByFilter(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");

        List<Franchise> franchises = franchiseMapper.getFranchisesByFilter(name, nameZh, sortField, sortOrder, first, row);

        int total = franchiseMapper.getFranchisesRowsByFilter(name, nameZh);

        return new SearchResult(total, franchises);
    }

    //endregion

}
