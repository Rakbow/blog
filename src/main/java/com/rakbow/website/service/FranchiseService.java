package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.FranchiseMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.entity.franchise.MetaInfo;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.entity.User;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.entity.FranchiseUtils;
import com.rakbow.website.util.file.QiniuFileUtils;
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
    @Autowired
    private QiniuFileUtils qiniuFileUtils;

    //endregion

    //region ------曾删改查------

    //新增系列
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addFranchise(Franchise franchise){
        franchiseMapper.addFranchise(franchise);
        if(!FranchiseUtils.isMetaFranchise(franchise)) {
            refreshRedisFranchises();
        }
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
        if(!FranchiseUtils.isMetaFranchise(franchise)) {
            refreshRedisFranchises();
        }
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

    /**
     * 更新父级系列
     *
     * @param parentFranchiseId,childFranchiseIds 父系列id，子系列ids
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateParentFranchise(int parentFranchiseId, List<Integer> childFranchiseIds) {

        List<Franchise> originChildFranchises = franchiseMapper.getFranchisesByParentId(Integer.toString(parentFranchiseId));

        if(childFranchiseIds.isEmpty()) {
            //若子系列ids为空，则将原先父id为parentFranchiseId的所有系列父id归0
            originChildFranchises.forEach(franchise -> {
                MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                metaInfo.metaId = "0";
                franchiseMapper.updateMetaInfo(franchise.getId(), JSON.toJSONString(metaInfo));
            });
        }else {
            //若子系列ids不为空，将父id不为该parentFranchiseId的系列父id归0
            originChildFranchises.forEach(franchise -> {
                if(!childFranchiseIds.contains(franchise.getId())) {
                    MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                    metaInfo.metaId = "0";
                    franchiseMapper.updateMetaInfo(franchise.getId(), JSON.toJSONString(metaInfo));
                }
            });

            childFranchiseIds.forEach(id -> {
                Franchise franchise = getFranchise(id);
                MetaInfo metaInfo = new MetaInfo(franchise.getMetaInfo());
                metaInfo.metaId = Integer.toString(parentFranchiseId);
                franchiseMapper.updateMetaInfo(id, JSON.toJSONString(metaInfo));
            });
        }
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

        JSONObject metaInfo = new JSONObject();
        if (franchiseJson.getBoolean("metaLabel")) {
            metaInfo.put("isMeta", "1");
        }else {
            metaInfo.put("isMeta", "0");
        }

        List<Integer> childFranchiseIds = franchiseJson.getList("childFranchises", Integer.class);

        metaInfo.put("childFranchises", childFranchiseIds);
        metaInfo.put("metaId", "0");
        franchiseJson.put("metaInfo", metaInfo);
        updateParentFranchise(franchiseJson.getIntValue("id"), childFranchiseIds);

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
                              JSONArray imageInfos, User user) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.FRANCHISE, images, originalImagesJson, imageInfos, user);

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

        JSONArray finalImageJson = qiniuFileUtils.commonDeleteFiles(images, deleteImages);

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
            if(!FranchiseUtils.isMetaFranchise(franchise)) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", franchise.getId());
                jsonObject.put("label", franchise.getNameZh());
                franchiseSet.add(jsonObject);
            }
        }

        redisUtil.set("franchiseSet", franchiseSet);
        //缓存时间1个月
        redisUtil.expire("franchiseSet", 2592000);

    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getFranchisesByFilter(JSONObject queryParams, int userAuthority) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");
        String isMeta;
        if (filter.getJSONObject("metaLabel").getBoolean("value") == null) {
            isMeta = null;
        }else {
            isMeta = filter.getJSONObject("metaLabel").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        List<Franchise> franchises = franchiseMapper.getFranchisesByFilter(name, nameZh, isMeta,
                userAuthority > 2, sortField, sortOrder, first, row);

        int total = franchiseMapper.getFranchisesRowsByFilter(name, nameZh, isMeta, userAuthority > 2);

        return new SearchResult(total, franchises);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Franchise> getFranchisesByParentId(int parentId) {
        return franchiseMapper.getFranchisesByParentId(Integer.toString(parentId));
    }

    //endregion

}
