package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.DiscMapper;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.data.common.ImageType;
import com.rakbow.website.data.MediaFormat;
import com.rakbow.website.data.product.ProductClass;
import com.rakbow.website.entity.Disc;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.Image.QiniuImageUtils;
import com.rakbow.website.util.common.*;
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
    private CommonImageUtils commonImageUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private SeriesService seriesService;

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
        //删除前先把服务器上对应图片全部删除
        deleteAllDiscImages(id);

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

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Map<String, Object> getDiscsByFilterList (JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String catalogNo = filter.getJSONObject("catalogNo").getString("value");

        String name = filter.getJSONObject("name").getString("value");

        String sortField = queryParams.getString("sortField");

        int sortOrder = queryParams.getIntValue("sortOrder");

        int seriesId = 0;
        if (filter.getJSONObject("seriesId").getInteger("value") != null) {
            seriesId = filter.getJSONObject("seriesId").getIntValue("value");
        }

        List<Integer> products = new ArrayList<>();
        List<Integer> tmpProducts = filter.getJSONObject("products").getList("value", Integer.class);
        if (tmpProducts != null) {
            products.addAll(tmpProducts);
        }

        List<Integer> mediaFormat = new ArrayList<>();
        List<Integer> tmpMediaFormat = filter.getJSONObject("mediaFormat").getList("value", Integer.class);
        if (tmpMediaFormat != null) {
            mediaFormat.addAll(tmpMediaFormat);
        }

        String isLimited;
        if (filter.getJSONObject("isLimited").getBoolean("value") == null) {
            isLimited = null;
        }else {
            isLimited = filter.getJSONObject("isLimited").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        }else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        int first = queryParams.getIntValue("first");

        int row = queryParams.getIntValue("rows");

        List<Disc> discs = discMapper.getDiscsByFilterList(catalogNo, name, seriesId, products,
                mediaFormat, isLimited, hasBonus, sortField, sortOrder,  first, row);

        int total = discMapper.getDiscsRowsByFilterList(catalogNo, name, seriesId, products,
                mediaFormat, isLimited, hasBonus);

        Map<String, Object> res = new HashMap<>();
        res.put("data", discs);
        res.put("total", total);

        return res;
    }

    //endregion

    //region ------数据处理------

    /**
     * Disc转Json对象，以便前端使用，转换量最大的
     *
     * @param disc
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject disc2Json(Disc disc) throws IOException {

        JSONObject discJson = (JSONObject) JSON.toJSON(disc);

        //是否包含特典
        boolean hasBonus = (disc.getHasBonus() == 1);
        //是否为限定版
        boolean isLimited = (disc.getIsLimited() == 1);

        JSONArray images = JSONArray.parseArray(disc.getImages());
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                image.put("thumbUrl", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 100));
                image.put("thumbUrl50", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 50));
            }
        }

        //发售时间转为string
        discJson.put("releaseDate", CommonUtils.dateToString(disc.getReleaseDate()));

        //媒体格式
        List<String> mediaFormat = new ArrayList<>();
        JSONObject.parseObject(disc.getMediaFormat()).getList("ids", Integer.class)
                .forEach(id -> mediaFormat.add(MediaFormat.getNameByIndex(id)));

        //所属产品（详细）
        List<String> product = new ArrayList<>();
        JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class)
                .forEach(id -> product.add(productService.getProductById(id).getNameZh() + "(" +
                        ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")"));

        //所属产品
        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", disc.getSeriesId());
        series.put("name", seriesService.selectSeriesById(disc.getSeriesId()).getNameZh());

        //对封面图片进行处理
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

        //对展示图片进行封装
        List<JSONObject> displayImages = new ArrayList<>();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (image.getIntValue("type") == ImageType.DISPLAY.getIndex()
                        || image.getIntValue("type") == ImageType.COVER.getIndex()) {
                    displayImages.add(image);
                }
            }
        }

        //对其他图片进行封装
        List<JSONObject> otherImages = new ArrayList<>();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "2")) {
                    otherImages.add(image);
                }
            }
        }

        discJson.put("isLimited", isLimited);
        discJson.put("hasBonus", hasBonus);
        discJson.put("mediaFormat", mediaFormat);
        discJson.put("images", images);
        discJson.put("cover", cover);
        discJson.put("displayImages", displayImages);
        discJson.put("otherImages", otherImages);
        discJson.put("series", series);
        discJson.put("product", product);
        discJson.put("products", products);
        discJson.put("addedTime", CommonUtils.timestampToString(disc.getAddedTime()));
        discJson.put("editedTime", CommonUtils.timestampToString(disc.getEditedTime()));
        return discJson;
    }

    /**
     * 列表转换, Disc转Json对象，以便前端使用，转换量最大的
     *
     * @param discs
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> album2Json(List<Disc> discs) {
        List<JSONObject> discJsons = new ArrayList<>();

        discs.forEach(disc -> {
            try {
                discJsons.add(disc2Json(disc));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return discJsons;
    }

    /**
     * Disc转Json对象，以便前端list界面展示使用
     *
     * @param disc
     * @return List<JSONObject>
     * @author rakbow
     */
    public JSONObject disc2JsonList(Disc disc) throws IOException {

        JSONObject discJson = (JSONObject) JSON.toJSON(disc);

        //是否包含特典
        boolean hasBonus = (disc.getHasBonus() == 1);
        //是否为限定版
        boolean isLimited = (disc.getIsLimited() == 1);

        //发售时间转为string
        discJson.put("releaseDate", CommonUtils.dateToString(disc.getReleaseDate()));

        //媒体格式
        List<String> mediaFormat = new ArrayList<>();
        JSONObject.parseObject(disc.getMediaFormat()).getList("ids", Integer.class)
                .forEach(id -> mediaFormat.add(MediaFormat.getNameByIndex(id)));

        //所属产品（详细）
        List<String> product = new ArrayList<>();
        JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class)
                .forEach(id -> product.add(productService.getProductById(id).getNameZh() + "(" +
                        ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")"));

        //所属产品
        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", disc.getSeriesId());
        series.put("name", seriesService.selectSeriesById(disc.getSeriesId()).getNameZh());

        //对封面图片进行处理
        JSONArray images = JSONArray.parseArray(disc.getImages());
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

        discJson.put("isLimited", isLimited);
        discJson.put("hasBonus", hasBonus);
        discJson.put("mediaFormat", mediaFormat);
        discJson.put("cover", cover);
        discJson.put("series", series);
        discJson.put("product", product);
        discJson.put("products", products);
        discJson.put("addedTime", CommonUtils.timestampToString(disc.getAddedTime()));
        discJson.put("editedTime", CommonUtils.timestampToString(disc.getEditedTime()));

        discJson.remove("bonus");
        discJson.remove("description");
        discJson.remove("images");
        discJson.remove("spec");

        return discJson;
    }

    /**
     * 列表转换, Disc转Json对象，以便前端list界面展示使用
     *
     * @param discs
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> disc2JsonList(List<Disc> discs) {
        List<JSONObject> discJsons = new ArrayList<>();

        discs.forEach(disc -> {
            try {
                discJsons.add(disc2JsonList(disc));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return discJsons;
    }

    /**
     * json对象转Disc，以便保存到数据库
     *
     * @param discJson
     * @return Disc
     * @author rakbow
     */
    public Disc json2Disc(JSONObject discJson) {
        return discJson.toJavaObject(Disc.class);
    }

    /**
     * 检测数据合法性
     *
     * @param discJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkDiscJson(JSONObject discJson) {
        if (StringUtils.isBlank(discJson.getString("name"))) {
            return ApiInfo.DISC_NAME_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("releaseDate"))) {
            return ApiInfo.DISC_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("seriesId"))) {
            return ApiInfo.DISC_SERIES_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("products"))
                || StringUtils.equals(discJson.getString("products"), "[]")) {
            return ApiInfo.DISC_PRODUCTS_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("mediaFormat"))
                || StringUtils.equals(discJson.getString("mediaFormat"), "[]")) {
            return ApiInfo.DISC_MEDIA_FORMAT_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送碟片数据
     *
     * @param discJson
     * @return 处理后的disc json格式数据
     * @author rakbow
     */
    public JSONObject handleDiscJson(JSONObject discJson) {

        String[] products = CommonUtils.str2SortedArray(discJson.getString("products"));
        String[] mediaFormat = CommonUtils.str2SortedArray(discJson.getString("mediaFormat"));

        //处理时间
        // String releaseDate = CommonUtil.dateToString(albumJson.getDate("releaseDate"));

        discJson.put("releaseDate", discJson.getDate("releaseDate"));
        discJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        discJson.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return discJson;
    }

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

        JSONArray finalImageJson = commonImageUtils.commonAddImages
                (id, EntityType.DISC, images, originalImagesJson, imageInfos);

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
    public String deleteDiscImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getDiscById(id).getImages());

        JSONArray finalImageJson = commonImageUtils.commonDeleteImages(id, images, deleteImages);

        discMapper.updateDiscImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    /**
     * 删除该碟片所有图片
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllDiscImages(int id) {
        Disc disc = getDiscById(id);
        JSONArray images = JSON.parseArray(disc.getImages());
        return commonImageUtils.commonDeleteAllImages(EntityType.DISC, images);
    }

    /**
     * 更新碟片规格信息
     *
     * @param id          碟片id
     * @param spec 碟片的规格信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateDiscSpec(int id, String spec) {
        discMapper.updateDiscSpec(id, spec, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新碟片描述
     *
     * @param id          碟片id
     * @param description 碟片的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateDiscDescription(int id, String description) {
        discMapper.updateDiscDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新特典信息
     *
     * @param id    碟片id
     * @param bonus 碟片的特典信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateDiscBonus(int id, String bonus) {
        discMapper.updateDiscBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

}
