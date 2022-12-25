package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.ProductMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.product.ProductClass;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.Image.QiniuImageUtils;
import com.rakbow.website.util.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:02
 * @Description:
 */
@Service
public class ProductService {

    //region ------注入实例------
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private SeriesService seriesService;
    @Value("${website.path.img}")
    private String imgPath;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    //endregion

    //region ------基础增删改查------

    //新增作品
    public int addProduct(Product product) {
        return productMapper.addProduct(product);
    }

    //通过id查找作品
    public Product getProductById(int id) {
        return productMapper.selectProductById(id);
    }

    //获取所有作品
    public List<Product> getAllProduct() {
        return productMapper.getAll();
    }

    //更新作品信息
    public int updateProduct(int id, Product product) {
        return productMapper.updateProduct(id, product);
    }

    //删除作品
    public int deleteProductById(int id) {
        deleteAllProductImages(id);
        return productMapper.deleteProductById(id);
    }

    //获取某系列所有作品
    public List<Product> selectProductsBySeriesId(int seriesId) {
        return productMapper.selectAllProductsBySeriesId(seriesId);
    }

    //通过系列Id获取所有作品的数组，供前端选项用
    public List<JSONObject> getAllProductSetBySeriesId(int seriesId) {
        List<JSONObject> productSet = new ArrayList<>();
        selectProductsBySeriesId(seriesId).forEach(product -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", product.getNameZh() + "(" + ProductClass.getNameZhByIndex(product.getClassification()) + ")");
            jsonObject.put("value", product.getId());
            productSet.add(jsonObject);
        });
        return productSet;
    }

    //endregion

    /**
     * Product转Json对象，以便前端使用，转换量最大的
     *
     * @param product
     * @return JSONObject
     * @author rakbow
     */
    //region ------数据处理------
    public JSONObject product2Json (Product product) {

        JSONObject productJson = new JSONObject();

        JSONObject series = new JSONObject();
        series.put("id", product.getSeriesId());
        series.put("name", seriesService.selectSeriesById(product.getSeriesId()).getNameZh());

        JSONObject classification = new JSONObject();
        classification.put("id", product.getClassification());
        classification.put("nameZh", ProductClass.getNameZhByIndex(product.getClassification()));

        JSONArray images = JSON.parseArray(product.getImages());
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                image.put("thumbUrl", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 100));
            }
        }

        JSONArray staffs = JSON.parseArray(product.getStaffs());

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_WIDTH_URL, 250));
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

        productJson.put("id", product.getId());
        productJson.put("series", series);
        productJson.put("name", product.getName());
        productJson.put("nameZh", product.getNameZh());
        productJson.put("nameEn", product.getNameEn());
        productJson.put("releaseDate", CommonUtils.dateToString(product.getReleaseDate()));
        productJson.put("classification", classification);
        productJson.put("description", product.getDescription());
        productJson.put("staffs", staffs);
        productJson.put("remark", product.getRemark());
        productJson.put("images", images);
        productJson.put("cover", cover);
        productJson.put("addedTime", CommonUtils.timestampToString(product.getAddedTime()));
        productJson.put("editedTime", CommonUtils.timestampToString(product.getEditedTime()));

        return productJson;
    }

    /**
     * Product转极简Json
     *
     * @param product
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject product2JsonSimple(Product product) {
        JSONObject productJson = new JSONObject();

        JSONObject series = new JSONObject();
        series.put("id", product.getSeriesId());
        series.put("name", seriesService.selectSeriesById(product.getSeriesId()).getNameZh());

        JSONObject classification = new JSONObject();
        classification.put("id", product.getClassification());
        classification.put("nameZh", ProductClass.getNameZhByIndex(product.getClassification()));

        JSONArray images = JSON.parseArray(product.getImages());

        JSONArray staffs = JSON.parseArray(product.getStaffs());

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_WIDTH_URL, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        productJson.put("id", product.getId());
        productJson.put("series", series);
        productJson.put("name", product.getName());
        productJson.put("nameZh", product.getNameZh());
        productJson.put("nameEn", product.getNameEn());
        productJson.put("releaseDate", CommonUtils.dateToString(product.getReleaseDate()));
        productJson.put("classification", classification);
        productJson.put("cover", cover);
        productJson.put("addedTime", CommonUtils.timestampToString(product.getAddedTime()));
        productJson.put("editedTime", CommonUtils.timestampToString(product.getEditedTime()));

        return productJson;
    }

    /**
     * 列表转换, Product转极简Json
     *
     * @param products
     * @return JSONObject
     * @author rakbow
     */
    public List<JSONObject> product2JsonSimple(List<Product> products) {
        List<JSONObject> productsJsons = new ArrayList<>();

        products.forEach(product -> {
            productsJsons.add(product2JsonSimple(product));
        });
        return productsJsons;
    }

    /**
     * 列表转换, Product转Json对象，以便前端使用，转换量最大的
     *
     * @param products
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> product2json (List<Product> products) {
        List<JSONObject> productJsons = new ArrayList<>();

        products.forEach(product -> {
            productJsons.add(product2Json(product));
        });
        return productJsons;
    }

    /**
     * json对象转Product，以便保存到数据库
     *
     * @param productJson
     * @return Album
     * @author rakbow
     */
    public Product json2Product(JSONObject productJson) {
        return productJson.toJavaObject(Product.class);
    }

    /**
     * 检测数据合法性
     *
     * @param productJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkProductJson (JSONObject productJson) {
        if (StringUtils.isBlank(productJson.getString("name"))) {
            return ApiInfo.PRODUCT_NAME_EMPTY;
        }
        if (StringUtils.isBlank(productJson.getString("nameZh"))) {
            return ApiInfo.PRODUCT_NAME_ZH_EMPTY;
        }
        if (StringUtils.isBlank(productJson.getString("releaseDate"))) {
            return ApiInfo.PRODUCT_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(productJson.getString("seriesId"))) {
            return ApiInfo.PRODUCT_SERIES_EMPTY;
        }
        if (StringUtils.isBlank(productJson.getString("classification"))) {
            return ApiInfo.PRODUCT_CLASSIFICATION_EMPTY;
        }
        return "";
    }

    /**
     * 更新描述信息
     * @author rakbow
     * @param id 作品id
     * @param description 描述信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateProductDescription(int id, String description) {
        productMapper.updateProductDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新Staff
     *
     * @param id 作品id
     * @param staffs staff相关信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateProductStaffs(int id, String staffs) {
        productMapper.updateProductStaffs(id, staffs, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 新增图片
     *
     * @param id     专辑id
     * @param images 图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addProductImages(int id, MultipartFile[] images, JSONArray originalImagesJson, JSONArray imageInfos) throws IOException {

        //最终保存到数据库的json信息
        JSONArray finalImageJson = new JSONArray();

        //新增图片信息json
        JSONArray addImageJson = new JSONArray();

        //创建存储链接前缀
        String filePath = EntityType.PRODUCT.getNameEn().toLowerCase() + "/" + id + "/";

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
                }else {
                    jo.put("description", imageInfos.getJSONObject(i).getString("description"));
                }
                addImageJson.add(jo);
            }
        }

        //汇总
        finalImageJson.addAll(originalImagesJson);
        finalImageJson.addAll(addImageJson);

        productMapper.updateProductImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图片
     *
     * @param id id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String updateProductImages(int id, String images) {
        productMapper.updateProductImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 删除图片
     *
     * @param id           专辑id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteProductImages(int id, JSONArray deleteImages) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getProductById(id).getImages());

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
        }else {
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
        productMapper.updateProductImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 删除该作品所有图片
     *
     * @param id 作品id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllProductImages(int id) {
        Product product = getProductById(id);
        JSONArray images = JSON.parseArray(product.getImages());
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
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 获取相关作品
     *
     * @param productId 作品id
     * @author rakbow
     * @return list
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedProducts (int productId) {
        List<JSONObject> relatedProducts = new ArrayList<>();

        Product product = getProductById(productId);

        List<Product> sameSeriesProducts = selectProductsBySeriesId(product.getSeriesId());
        List<Product> products = DataFinder.findProductsByClassification(product.getClassification(), sameSeriesProducts);

        products.removeIf(it -> it.getId() == productId);

        relatedProducts = product2JsonSimple(products);

        if (relatedProducts.size() > 5) {
            relatedProducts = relatedProducts.subList(0, 5);
        }

        return relatedProducts;
    }

    //endregion

}
