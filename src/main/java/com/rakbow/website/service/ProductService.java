package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.ProductMapper;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.data.common.SearchResult;
import com.rakbow.website.data.product.ProductCategory;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.ProductUtils;
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
    private FranchiseService franchiseService;
    @Value("${website.path.img}")
    private String imgPath;
    @Autowired
    private CommonImageUtils commonImageUtils;
    //endregion

    //region ------基础增删改查------

    //新增作品
    public int addProduct(Product product) {
        return productMapper.addProduct(product);
    }

    //通过id查找作品
    public Product getProduct(int id) {
        return productMapper.getProduct(id);
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
     * Product转Json对象，以便前端使用，转换量最大的
     *
     * @param product
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject product2Json (Product product) {

        JSONObject productJson = new JSONObject();

        JSONObject franchise = new JSONObject();
        franchise.put("id", product.getFranchise());
        franchise.put("nameZh", franchiseService.getFranchise(product.getFranchise()).getNameZh());

        JSONObject category = new JSONObject();
        category.put("id", product.getCategory());
        category.put("nameZh", ProductCategory.getNameZhByIndex(product.getCategory()));

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
        productJson.put("franchise", franchise);
        productJson.put("name", product.getName());
        productJson.put("nameZh", product.getNameZh());
        productJson.put("nameEn", product.getNameEn());
        productJson.put("releaseDate", CommonUtils.dateToString(product.getReleaseDate()));
        productJson.put("category", category);
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

        JSONObject franchise = new JSONObject();
        franchise.put("id", product.getFranchise());
        franchise.put("nameZh", franchiseService.getFranchise(product.getFranchise()).getNameZh());

        JSONObject category = new JSONObject();
        category.put("id", product.getCategory());
        category.put("nameZh", ProductCategory.getNameZhByIndex(product.getCategory()));

        JSONArray images = JSON.parseArray(product.getImages());

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrlWidth(CommonConstant.EMPTY_IMAGE_WIDTH_URL, 50));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrlWidth(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        productJson.put("id", product.getId());
        productJson.put("franchise", franchise);
        productJson.put("name", product.getName());
        productJson.put("nameZh", product.getNameZh());
        productJson.put("nameEn", product.getNameEn());
        productJson.put("releaseDate", CommonUtils.dateToString(product.getReleaseDate()));
        productJson.put("category", category);
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

        JSONArray finalImageJson = commonImageUtils.commonAddImages
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
    public String deleteProductImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getProduct(id).getImages());

        JSONArray finalImageJson = commonImageUtils.commonDeleteImages(id, images, deleteImages);

        productMapper.updateProductImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
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
        Product product = getProduct(id);
        JSONArray images = JSON.parseArray(product.getImages());
        return commonImageUtils.commonDeleteAllImages(EntityType.PRODUCT, images);
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
        List<JSONObject> relatedProducts;

        Product product = getProduct(productId);

        List<Integer> franchises = new ArrayList<>(product.getFranchise());

        List<Product> sameFranchiseProducts = productMapper.getProductsByFilter
                (null, null, franchises, null, null, -1, 0, 0);
        List<Product> products = DataFinder.findProductsByClassification(product.getCategory(), sameFranchiseProducts);

        products.removeIf(it -> it.getId() == productId);

        relatedProducts = product2JsonSimple(products);

        if (relatedProducts.size() > 5) {
            relatedProducts = relatedProducts.subList(0, 5);
        }

        return relatedProducts;
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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

    //endregion

}
