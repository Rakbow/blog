package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.ProductMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.data.vo.product.ProductVOAlpha;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.entity.ProductUtils;
import com.rakbow.website.util.common.*;
import com.rakbow.website.util.convertMapper.ProductVOMapper;
import com.rakbow.website.util.image.QiniuImageUtils;
import com.rakbow.website.util.common.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;

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
    private QiniuImageUtils qiniuImageUtils;
    @Autowired
    private RedisUtil redisUtil;

    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
    //endregion

    //region ------基础增删改查------

    //新增作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int addProduct(Product product) {
        return productMapper.addProduct(product);
    }

    //通过id查找作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Product getProduct(int id) {
        return productMapper.getProduct(id);
    }

    //获取所有作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Product> getAllProduct() {
        return productMapper.getAll();
    }

    //更新作品信息
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int updateProduct(int id, Product product) {
        return productMapper.updateProduct(id, product);
    }

    //删除作品
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int deleteProduct(int id) {
        deleteAllProductImages(id);
        return productMapper.deleteProduct(id);
    }

    //通过系列Id获取所有作品的数组，供前端选项用
    public List<JSONObject> getProductSet(int franchiseId, int entityType) {

        List<Integer> franchises = new ArrayList<>();
        franchises.add(franchiseId);

        List<Integer> categories = ProductUtils.getCategoriesByEntityType(entityType);

        List<JSONObject> productSet = new ArrayList<>();
        productMapper.getProductsByFilter(null, null, franchises, categories,
                null, -1, 0, 0).forEach(product -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", product.getNameZh() + "(" + ProductCategory.getNameZhByIndex(product.getCategory()) + ")");
            jsonObject.put("value", product.getId());
            productSet.add(jsonObject);
        });
        return productSet;
    }

    //通过系列Id获取所有作品的数组，供前端选项用
    public List<JSONObject> getProductSet(List<Integer> franchises, int entityType) {

        List<JSONObject> productSet = new ArrayList<>();

        if (franchises.size() != 0) {
            List<Integer> categories = ProductUtils.getCategoriesByEntityType(entityType);
            productMapper.getProductsByFilter(null, null, franchises, categories,
                    null, -1, 0, 0).forEach(product -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("value", product.getId());
                jsonObject.put("label", product.getNameZh() + "(" + ProductCategory.getNameZhByIndex(product.getCategory()) + ")");
                productSet.add(jsonObject);
            });
        }
        return productSet;
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Product，以便保存到数据库
     *
     * @param productJson
     * @return Album
     * @author rakbow
     */
    public Product json2Product(JSONObject productJson) {
        return JSON.to(Product.class, productJson);
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
        if (StringUtils.isBlank(productJson.getString("franchise"))) {
            return ApiInfo.PRODUCT_FRANCHISE_EMPTY;
        }
        if (StringUtils.isBlank(productJson.getString("category"))) {
            return ApiInfo.PRODUCT_CATEGORY_EMPTY;
        }
        return "";
    }

    /**
     * 更新描述信息
     * @author rakbow
     * @param id 作品id
     * @param description 描述信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addProductImages(int id, MultipartFile[] images, JSONArray originalImagesJson, JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.PRODUCT, images, originalImagesJson, imageInfos);

        productMapper.updateProductImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图片
     *
     * @param id id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteProductImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getProduct(id).getImages());

        JSONArray finalImageJson = qiniuImageUtils.commonDeleteImages(id, images, deleteImages);

        productMapper.updateProductImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 删除该作品所有图片
     *
     * @param id 作品id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteAllProductImages(int id) {
        Product product = getProduct(id);
        JSONArray images = JSON.parseArray(product.getImages());
        qiniuImageUtils.commonDeleteAllImages(EntityType.PRODUCT, images);

        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
    }

    /**
     * 获取相关作品
     *
     * @param productId 作品id
     * @author rakbow
     * @return list
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<ProductVOAlpha> getRelatedProducts (int productId) {
        List<ProductVOAlpha> relatedProducts;

        Product product = getProduct(productId);

        List<Integer> franchises = new ArrayList<>();
        franchises.add(product.getFranchise());

        List<Integer> categories = new ArrayList<>();
        categories.add(product.getCategory());

        List<Product> sameFranchiseProducts = productMapper.getProductsByFilter
                (null, null, franchises, categories, null, -1, 0, 0);
        List<Product> products = DataFinder.findProductsByClassification(product.getCategory(), sameFranchiseProducts);

        products.removeIf(it -> it.getId() == productId);

        relatedProducts = productVOMapper.product2VOAlpha(products);

        if (relatedProducts.size() > 5) {
            relatedProducts = relatedProducts.subList(0, 5);
        }

        return relatedProducts;
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getProductsByFilter(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String name = filter.getJSONObject("name").getString("value");
        String nameZh = filter.getJSONObject("nameZh").getString("value");
        List<Integer> franchises = filter.getJSONObject("franchise").getList("value", Integer.class);
        List<Integer> categories = filter.getJSONObject("category").getList("value", Integer.class);

        List<Product> products = productMapper.getProductsByFilter(name, nameZh, franchises, categories,
                sortField, sortOrder, first, row);
        int total = productMapper.getProductsRowsByFilter(name, nameZh, franchises, categories);

        return new SearchResult(total, products);
    }

    /**
     * 刷新Redis缓存中的products数据
     *
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public void refreshRedisProducts () {

        JSONArray products = new JSONArray();
        getAllProduct().forEach(product -> {
            JSONObject jo = new JSONObject();
            jo.put("value", product.getId());
            jo.put("label", product.getNameZh() + "(" +
                    ProductCategory.getNameZhByIndex(product.getCategory()) + ")");
            products.add(jo);
        });

        redisUtil.set("productSet", products);
        //缓存时间1个月
        redisUtil.expire("productSet", 2592000);

    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<ProductVOAlpha> getProductsByFranchiseId(int franchiseId) {

        List<Integer> franchise = new ArrayList<>();
        franchise.add(franchiseId);

        List<Product> products = productMapper.getProductsByFilter(null, null, franchise,
                null, "releaseDate", 1, 0 ,0);

        return productVOMapper.product2VOAlpha(products);
    }

    //endregion

    //region ------图片操作------
    //endregion

}
