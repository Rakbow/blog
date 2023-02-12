package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.MerchMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOBeta;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.entity.User;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.convertMapper.MerchVOMapper;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:19
 * @Description: merch业务层
 */
@Service
public class MerchService {

    //region ------引入实例------

    @Autowired
    private MerchMapper merchMapper;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    @Autowired
    private QiniuFileUtils qiniuFileUtils;
    @Autowired
    private VisitService visitService;

    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    //endregion

    //region ------更删改查------

    /**
     * 新增周边
     *
     * @param merch 新增的周边
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMerch(Merch merch) {
        merchMapper.addMerch(merch);
    }

    /**
     * 根据Id获取周边
     *
     * @param id 周边id
     * @return merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Merch getMerch(int id) {
        return merchMapper.getMerch(id);
    }

    /**
     * 根据Id删除周边
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMerch(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllMerchImages(id);

        merchMapper.deleteMerch(id);
    }

    /**
     * 更新周边基础信息
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMerch(int id, Merch merch) {
        merchMapper.updateMerch(id, merch);
    }

    //endregion

    //region ------图片操作------

    /**
     * 新增周边图片
     *
     * @param id                 周边id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMerchImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                               JSONArray imageInfos, User user) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.MERCH, images, originalImagesJson, imageInfos, user);

        merchMapper.updateMerchImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新周边图片
     *
     * @param id     周边id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMerchImages(int id, String images) {
        merchMapper.updateMerchImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.MERCH.getNameZh());
    }

    /**
     * 删除周边图片
     *
     * @param id           周边id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteMerchImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getMerch(id).getImages());

        JSONArray finalImageJson = qiniuFileUtils.commonDeleteFiles(images, deleteImages);

        merchMapper.updateMerchImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.MERCH.getNameZh());
    }

    /**
     * 删除该周边所有图片
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAllMerchImages(int id) {
        Merch merch = getMerch(id);
        JSONArray images = JSON.parseArray(merch.getImages());

        qiniuFileUtils.commonDeleteAllFiles(images);
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Merch，以便保存到数据库
     *
     * @param merchJson merchJson
     * @return merch
     * @author rakbow
     */
    public Merch json2Merch(JSONObject merchJson) {
        return JSON.to(Merch.class, merchJson);
    }

    /**
     * 检测数据合法性
     *
     * @param merchJson merchJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkMerchJson(JSONObject merchJson) {
        if (StringUtils.isBlank(merchJson.getString("name"))) {
            return ApiInfo.MERCH_NAME_EMPTY;
        }
        if (StringUtils.isBlank(merchJson.getString("releaseDate"))) {
            return ApiInfo.MERCH_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(merchJson.getString("category"))) {
            return ApiInfo.MERCH_CATEGORY_EMPTY;
        }
        if (StringUtils.isBlank(merchJson.getString("franchises"))
                || StringUtils.equals(merchJson.getString("franchises"), "[]")) {
            return ApiInfo.MERCH_FRANCHISES_EMPTY;
        }
        if (StringUtils.isBlank(merchJson.getString("products"))
                || StringUtils.equals(merchJson.getString("products"), "[]")) {
            return ApiInfo.MERCH_PRODUCTS_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送周边数据
     *
     * @param merchJson merchJson
     * @return 处理后的merch json格式数据
     * @author rakbow
     */
    public JSONObject handleMerchJson(JSONObject merchJson) {

        String[] products = CommonUtils.str2SortedArray(merchJson.getString("products"));
        String[] franchises = CommonUtils.str2SortedArray(merchJson.getString("franchises"));

        merchJson.put("releaseDate", merchJson.getDate("releaseDate"));
        merchJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        merchJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");

        return merchJson;
    }

    //endregion

    //region ------更新merch数据------

    /**
     * 更新周边规格信息
     *
     * @param id   周边id
     * @param spec 周边的规格信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMerchSpec(int id, String spec) {
        merchMapper.updateMerchSpec(id, spec, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新周边描述
     *
     * @param id          周边id
     * @param description 周边的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMerchDescription(int id, String description) {
        merchMapper.updateMerchDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getMerchsByFilterList(JSONObject queryParams, int userAuthority) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String name = filter.getJSONObject("name").getString("value");
        String barcode = filter.getJSONObject("barcode").getString("value");
        String region = filter.getJSONObject("region").getString("value");

        int category = 100;
        if (filter.getJSONObject("category").getInteger("value") != null) {
            category = filter.getJSONObject("category").getIntValue("value");
        }

        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);

        String notForSale;
        if (filter.getJSONObject("notForSale").getBoolean("value") == null) {
            notForSale = null;
        } else {
            notForSale = filter.getJSONObject("notForSale").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }

        List<Merch> merchs = merchMapper.getMerchsByFilter(name, barcode, franchises, products, category, region,
                notForSale, userAuthority > 2, sortField, sortOrder, first, row);

        int total = merchMapper.getMerchsRowsByFilter(name, barcode, franchises, products, category, region, notForSale, userAuthority > 2);

        return new SearchResult(total, merchs);
    }

    /**
     * 根据作品id获取关联周边
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOBeta> getMerchsByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Merch> merchs = merchMapper.getMerchsByFilter(null, null, null, products,
                100, null, null, false, "releaseDate", -1,  0, 0);

        return merchVOMapper.merch2VOBeta(merchs);
    }

    /**
     * 获取相关联Merch
     *
     * @param id 周边id
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOBeta> getRelatedMerchs(int id) {

        List<Merch> result = new ArrayList<>();

        Merch merch = getMerch(id);

        //该Merch包含的作品id
        List<Integer> productIds = JSONObject.parseObject(merch.getProducts()).getList("ids", Integer.class);

        //该系列所有Merch
        List<Merch> allMerchs = merchMapper.getMerchsByFilter(null, null, CommonUtils.ids2List(merch.getFranchises()),
                null, 100, null, null, false, "releaseDate", 1, 0, 0)
                .stream().filter(tmpMerch -> tmpMerch.getId() != merch.getId()).collect(Collectors.toList());

        List<Merch> queryResult = allMerchs.stream().filter(tmpMerch ->
                StringUtils.equals(tmpMerch.getProducts(), merch.getProducts())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Merch> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Merch> tmpQueryResult = allMerchs.stream().filter(tmpMerch ->
                        JSONObject.parseObject(tmpMerch.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Merch> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allMerchs.stream().filter(tmpMerch ->
                                JSONObject.parseObject(tmpMerch.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtils.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return merchVOMapper.merch2VOBeta(CommonUtils.removeDuplicateList(result));
    }

    /**
     * 获取最新收录的周边
     *
     * @param limit 获取条数
     * @return list封装的周边
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOAlpha> getJustAddedMerchs(int limit) {
        return merchVOMapper.merch2VOAlpha(merchMapper.getMerchsOrderByAddedTime(limit));
    }

    /**
     * 获取最近编辑的Merch
     *
     * @param limit 获取条数
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOAlpha> getJustEditedMerchs(int limit) {
        return merchVOMapper.merch2VOAlpha(merchMapper.getMerchsOrderByEditedTime(limit));
    }

    /**
     * 获取浏览量最高的Merch
     *
     * @param limit 获取条数
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MerchVOAlpha> getPopularMerchs(int limit) {
        List<MerchVOAlpha> popularMerchs = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.MERCH.getId(), limit);

        visits.forEach(visit -> {
            MerchVOAlpha merch = merchVOMapper.merch2VOAlpha(getMerch(visit.getEntityId()));
            merch.setVisitNum(visit.getVisitNum());
            popularMerchs.add(merch);
        });
        return popularMerchs;
    }

    //endregion

}
