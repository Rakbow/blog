package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.ProductMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.ImageType;
import com.rakbow.website.data.ProductClass;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
                image.put("thumbUrl", CommonUtils.getCompressImageUrl(imgPath,
                        StringUtils.lowerCase(EntityType.PRODUCT.getNameEn()),
                        product.getId(), CommonUtils.getImageFileNameByUrl(image.getString("url"))));
            }
        }

        JSONObject cover = new JSONObject();
        cover.put("url", CommonConstant.EMPTY_IMAGE_WIDTH_URL);
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (image.getIntValue("type") == ImageType.COVER.getIndex()) {
                    cover.put("url", image.getString("url"));
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
        productJson.put("remark", product.getRemark());
        productJson.put("images", images);
        productJson.put("cover", cover);
        productJson.put("addedTime", CommonUtils.timestampToString(product.getAddedTime()));
        productJson.put("editedTime", CommonUtils.timestampToString(product.getEditedTime()));

        return productJson;
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
     * 新增图片
     *
     * @param id     专辑id
     * @param images 图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addProductImages(int id, String images) {
        productMapper.updateProductImages(id, images, new Timestamp(System.currentTimeMillis()));
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

        //图片文件名
        String fileName = "";

        //循环删除
        for (int i = 0; i < deleteImages.size(); i++) {
            JSONObject image = deleteImages.getJSONObject(i);
            // 迭代器
            Iterator<Object> iterator = images.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (image.getString("url").equals(itJson.getString("url"))) {
                    // 删除数组元素
                    String deleteImageUrl = itJson.getString("url");
                    fileName = deleteImageUrl.substring(
                            deleteImageUrl.lastIndexOf("/") + 1, deleteImageUrl.lastIndexOf("."));
                    iterator.remove();
                }
                //删除服务器上对应图片文件
                Path productImgPath = Paths.get(imgPath + "/product/" + id);
                CommonUtils.deleteFile(productImgPath, fileName);
            }
        }
        productMapper.updateProductImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 删除该专辑所有图片
     *
     * @param id 专辑id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllAlbumImages(int id) {
        JSONArray images = JSON.parseArray(getProductById(id).getImages());
        //图片文件名
        String fileName = "";
        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            String deleteImageUrl = image.getString("url");
            fileName = deleteImageUrl.substring(
                    deleteImageUrl.lastIndexOf("/") + 1, deleteImageUrl.lastIndexOf("."));
            //删除服务器上对应图片文件
            Path productImgPath = Paths.get(imgPath + "/product/" + id);
            CommonUtils.deleteFile(productImgPath, fileName);
        }
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    //endregion

}
