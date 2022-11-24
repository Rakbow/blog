package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.ProductMapper;
import com.rakbow.website.data.ImageType;
import com.rakbow.website.data.ProductClass;
import com.rakbow.website.entity.Product;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
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
    //endregion

    //region ------基础增删改查------

    //新增作品
    public int insertProduct(Product product) {
        return productMapper.insertProduct(product);
    }

    //通过id查找作品
    public Product selectProductById(int id) {
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

        JSONObject cover = new JSONObject();
        cover.put("url", CommonConstant.EMPTY_IMAGE_URL);
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
        productJson.put("releaseDate", CommonUtil.dateToString(product.getReleaseDate()));
        productJson.put("classification", classification);
        productJson.put("description", product.getDescription());
        productJson.put("remark", product.getRemark());
        productJson.put("images", images);
        productJson.put("cover", cover);
        productJson.put("addedTime", CommonUtil.timestampToString(product.getAddedTime()));
        productJson.put("editedTime", CommonUtil.timestampToString(product.getEditedTime()));

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

    //endregion

}
