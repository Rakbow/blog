package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.MerchMapper;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.common.segmentImagesResult;
import com.rakbow.website.data.merch.MerchCategory;
import com.rakbow.website.data.product.ProductClass;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.ProductUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
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
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:19
 * @Description:
 */
@Service
public class MerchService {

    //region ------引入实例------

    @Autowired
    private MerchMapper merchMapper;
    @Autowired
    private CommonImageUtils commonImageUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private ProductUtils productUtils;

    //endregion

    //region ------更删改查------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 新增周边
     *
     * @param merch 新增的周边
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Merch getMerchById(int id) {
        return merchMapper.getMerchById(id);
    }

    /**
     * 根据Id删除周边
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void deleteMerchById(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllMerchImages(id);

        merchMapper.deleteMerchById(id);
    }

    /**
     * 更新周边基础信息
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMerch(int id, Merch merch) {
        merchMapper.updateMerch(id, merch);
    }

    //endregion

    //region ------数据处理------

    /**
     * Merch转Json对象，以便前端使用，转换量最大的
     *
     * @param merch
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject merch2Json(Merch merch) {

        JSONObject merchJson = (JSONObject) JSON.toJSON(merch);

        //是否为非卖品
        boolean isNotForSale = (merch.getIsNotForSale() == 1);

        //将图片分割处理
        segmentImagesResult segmentImages = commonImageUtils.segmentImages(merch.getImages());

        JSONArray spec = JSON.parseArray(merch.getSpec());

        JSONObject category = new JSONObject();
        category.put("id", merch.getCategory());
        category.put("nameZh", MerchCategory.index2NameZh(merch.getCategory()));

        //所属作品
        List<JSONObject> products = productUtils.getProductList(merch.getProducts());

        JSONObject series = new JSONObject();
        series.put("id", merch.getSeries());
        series.put("name", seriesService.selectSeriesById(merch.getSeries()).getNameZh());

        merchJson.put("category", category);
        merchJson.put("isNotForSale", isNotForSale);
        merchJson.put("series", series);
        merchJson.put("products", products);
        merchJson.put("spec", spec);

        merchJson.put("releaseDate", CommonUtils.dateToString(merch.getReleaseDate()));
        merchJson.put("images", segmentImages.images);
        merchJson.put("cover", segmentImages.cover);
        merchJson.put("displayImages", segmentImages.displayImages);
        merchJson.put("otherImages", segmentImages.otherImages);
        merchJson.put("addedTime", CommonUtils.timestampToString(merch.getAddedTime()));
        merchJson.put("editedTime", CommonUtils.timestampToString(merch.getEditedTime()));
        return merchJson;
    }

    /**
     * 列表转换, Merch转Json对象，以便前端使用，转换量最大的
     *
     * @param merchs
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> merch2Json(List<Merch> merchs) {
        List<JSONObject> merchJsons = new ArrayList<>();

        merchs.forEach(merch -> {
            merchJsons.add(merch2Json(merch));
        });
        return merchJsons;
    }

    /**
     * Merch转Json对象，以便前端list界面展示使用
     *
     * @param merch
     * @return List<JSONObject>
     * @author rakbow
     */
    public JSONObject merch2JsonList(Merch merch) {

        JSONObject merchJson = (JSONObject) JSON.toJSON(merch);

        //是否包含特典
        boolean isNotForSale = (merch.getIsNotForSale() == 1);

        JSONObject category = new JSONObject();
        category.put("id", merch.getCategory());
        category.put("nameZh", MerchCategory.index2NameZh(merch.getCategory()));

        //发售时间转为string
        merchJson.put("releaseDate", CommonUtils.dateToString(merch.getReleaseDate()));

        //所属作品
        List<JSONObject> products = productUtils.getProductList(merch.getProducts());

        //所属系列
        JSONObject series = new JSONObject();
        series.put("id", merch.getSeries());
        series.put("name", seriesService.selectSeriesById(merch.getSeries()).getNameZh());

        //对封面图片进行处理
        JSONArray images = JSONArray.parseArray(merch.getImages());
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 250));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 250));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        merchJson.put("isNotForSale", isNotForSale);
        merchJson.put("cover", cover);
        merchJson.put("series", series);
        merchJson.put("category", category);
        merchJson.put("products", products);
        merchJson.put("addedTime", CommonUtils.timestampToString(merch.getAddedTime()));
        merchJson.put("editedTime", CommonUtils.timestampToString(merch.getEditedTime()));

        merchJson.remove("description");
        merchJson.remove("images");
        merchJson.remove("spec");

        return merchJson;
    }

    /**
     * 列表转换, merch转Json对象，以便前端list界面展示使用
     *
     * @param merchs
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> merch2JsonList(List<Merch> merchs) {
        List<JSONObject> merchJsons = new ArrayList<>();

        merchs.forEach(merch -> {
            merchJsons.add(merch2JsonList(merch));
        });
        return merchJsons;
    }

    /**
     * json对象转Merch，以便保存到数据库
     *
     * @param merchJson
     * @return merch
     * @author rakbow
     */
    public Merch json2Merch(JSONObject merchJson) {
        return merchJson.toJavaObject(Merch.class);
    }

    /**
     * Merch转极简Json
     *
     * @param merch
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject merch2JsonSimple(Merch merch) {

        JSONArray images = JSONArray.parseArray(merch.getImages());
        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), Integer.toString(ImageType.COVER.getIndex()))) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        JSONObject category = new JSONObject();
        category.put("id", merch.getCategory());
        category.put("nameZh", MerchCategory.index2NameZh(merch.getCategory()));

        JSONObject merchJson = new JSONObject();
        merchJson.put("id", merch.getId());
        merchJson.put("barcode", merch.getBarcode());
        merchJson.put("releaseDate", CommonUtils.dateToString(merch.getReleaseDate()));
        merchJson.put("name", merch.getName());
        merchJson.put("nameZh", merch.getNameZh());
        merchJson.put("category", category);
        merchJson.put("cover", cover);
        merchJson.put("addedTime", CommonUtils.timestampToString(merch.getAddedTime()));
        merchJson.put("editedTime", CommonUtils.timestampToString(merch.getEditedTime()));
        return merchJson;
    }

    /**
     * 列表转换, Merch转极简Json
     *
     * @param merchs
     * @return JSONObject
     * @author rakbow
     */
    public List<JSONObject> merch2JsonSimple(List<Merch> merchs) {
        List<JSONObject> merchJsons = new ArrayList<>();

        merchs.forEach(merch -> merchJsons.add(merch2JsonSimple(merch)));

        return merchJsons;
    }

    /**
     * Merch转Json对象，供首页展示
     *
     * @param merch
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject merch2JsonIndex(Merch merch) {

        JSONObject merchJson = (JSONObject) JSON.toJSON(merch);

        //封面
        JSONObject cover = commonImageUtils.getIndexCover(merch.getImages());

        merchJson.put("releaseDate", CommonUtils.dateToString(merch.getReleaseDate()));

        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(merch.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", merch.getSeries());
        series.put("name", seriesService.selectSeriesById(merch.getSeries()).getNameZh());

        JSONObject category = new JSONObject();
        category.put("id", merch.getCategory());
        category.put("nameZh", MerchCategory.index2NameZh(merch.getCategory()));

        merchJson.put("cover", cover);
        merchJson.put("products", products);
        merchJson.put("series", series);
        merchJson.put("category", category);
        merchJson.put("addedTime", CommonUtils.timestampToString(merch.getAddedTime()));
        merchJson.put("editedTime", CommonUtils.timestampToString(merch.getEditedTime()));
        merchJson.remove("spec");
        merchJson.remove("description");
        merchJson.remove("images");
        return merchJson;
    }

    /**
     * 列表转换, Merch转Json对象，供首页展示
     *
     * @param merchs
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> merch2JsonIndex(List<Merch> merchs) {
        List<JSONObject> merchJsons = new ArrayList<>();

        merchs.forEach(merch -> {
            merchJsons.add(merch2JsonIndex(merch));
        });
        return merchJsons;
    }

    /**
     * 检测数据合法性
     *
     * @param merchJson
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
        if (StringUtils.isBlank(merchJson.getString("series"))) {
            return ApiInfo.MERCH_SERIES_EMPTY;
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
     * @param merchJson
     * @return 处理后的merch json格式数据
     * @author rakbow
     */
    public JSONObject handleMerchJson(JSONObject merchJson) {

        String[] products = CommonUtils.str2SortedArray(merchJson.getString("products"));

        merchJson.put("releaseDate", merchJson.getDate("releaseDate"));
        merchJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");

        return merchJson;
    }

    //endregion

    //region ------更新merch数据------

    /**
     * 新增周边图片
     *
     * @param id                 周边id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addMerchImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                              JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = commonImageUtils.commonAddImages
                (id, EntityType.MERCH, images, originalImagesJson, imageInfos);

        merchMapper.updateMerchImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新周边图片
     *
     * @param id     周边id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteMerchImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getMerchById(id).getImages());

        JSONArray finalImageJson = commonImageUtils.commonDeleteImages(id, images, deleteImages);

        merchMapper.updateMerchImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.MERCH.getNameZh());
    }

    /**
     * 删除该周边所有图片
     *
     * @param id 周边id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllMerchImages(int id) {
        Merch merch = getMerchById(id);
        JSONArray images = JSON.parseArray(merch.getImages());

        return commonImageUtils.commonDeleteAllImages(EntityType.MERCH, images);
    }

    /**
     * 更新周边规格信息
     *
     * @param id   周边id
     * @param spec 周边的规格信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMerchDescription(int id, String description) {
        merchMapper.updateMerchDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Map<String, Object> getMerchsByFilterList(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");

        String name = filter.getJSONObject("name").getString("value");
        String barcode = filter.getJSONObject("isbn10").getString("value");

        int category = 100;
        if (filter.getJSONObject("category").getInteger("value") != null) {
            category = filter.getJSONObject("category").getIntValue("value");
        }

        int series = 0;
        if (filter.getJSONObject("series").getInteger("value") != null) {
            series = filter.getJSONObject("series").getIntValue("value");
        }

        List<Integer> products = new ArrayList<>();
        List<Integer> tmpProducts = filter.getJSONObject("products").getList("value", Integer.class);
        if (tmpProducts != null) {
            products.addAll(tmpProducts);
        }

        String isNotForSale;
        if (filter.getJSONObject("isNotForSale").getBoolean("value") == null) {
            isNotForSale = null;
        } else {
            isNotForSale = filter.getJSONObject("isNotForSale").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }

        int first = queryParams.getIntValue("first");

        int row = queryParams.getIntValue("rows");

        List<Merch> merchs = merchMapper.getMerchsByFilterList(name, barcode, series, products, category,
                isNotForSale, sortField, sortOrder, first, row);

        int total = merchMapper.getMerchsRowsByFilterList(name, barcode, series, products, category, isNotForSale);

        Map<String, Object> res = new HashMap<>();
        res.put("data", merchs);
        res.put("total", total);

        return res;
    }

    /**
     * 根据作品id获取关联周边
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getMerchsByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Merch> merchs = merchMapper.getMerchsByFilterList(null, null, 0, products,
                100, null, "releaseDate", -1,  0, 0);

        return merch2JsonSimple(merchs);
    }

    /**
     * 获取相关联Merch
     *
     * @param id 周边id
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedMerchs(int id) {

        List<Merch> result = new ArrayList<>();

        Merch merch = getMerchById(id);

        List<JSONObject> relatedMerchs = new ArrayList<>();

        //该Merch包含的作品id
        List<Integer> productIds = JSONObject.parseObject(merch.getProducts()).getList("ids", Integer.class);

        //该系列所有Merch
        List<Merch> allMerchs = merchMapper.getMerchsByFilterList(null, null, merch.getSeries(),
                null, 100, null, "releaseDate", 1, 0, 0)
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
        result = CommonUtils.removeDuplicateList(result);
        result.forEach(i -> relatedMerchs.add(merch2JsonSimple(i)));
        return relatedMerchs;
    }

    /**
     * 获取最新收录的周边
     *
     * @param limit 获取条数
     * @return list封装的周边
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustAddedMerchs(int limit) {
        List<JSONObject> justAddedMerchs = new ArrayList<>();

        merchMapper.getMerchsOrderByAddedTime(limit)
                .forEach(i -> justAddedMerchs.add(merch2JsonIndex(i)));

        return justAddedMerchs;
    }

    /**
     * 获取最近编辑的Merch
     *
     * @param limit 获取条数
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustEditedMerchs(int limit) {
        List<JSONObject> editedMerchs = new ArrayList<>();

        merchMapper.getMerchsOrderByEditedTime(limit)
                .forEach(i -> editedMerchs.add(merch2JsonIndex(i)));

        return editedMerchs;
    }

    /**
     * 获取浏览量最高的Merch
     *
     * @param limit 获取条数
     * @return list封装的Merch
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getPopularMerchs(int limit) {
        List<JSONObject> popularMerchs = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.MERCH.getId(), limit);

        visits.forEach(visit -> {
            JSONObject merch = merch2JsonIndex(getMerchById(visit.getEntityId()));
            merch.put("visitNum", visit.getVisitNum());
            popularMerchs.add(merch);
        });
        return popularMerchs;
    }

    //endregion

}
