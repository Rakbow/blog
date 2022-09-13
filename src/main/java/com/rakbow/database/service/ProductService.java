package com.rakbow.database.service;

import com.alibaba.fastjson.JSONObject;
import com.rakbow.database.dao.ProductMapper;
import com.rakbow.database.data.ProductClass;
import com.rakbow.database.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-20 2:02
 * @Description:
 */
@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;

    //新增产品
    public int insertProduct(Product product){
        return productMapper.insertProduct(product);
    };

    //通过id查找产品
    public Product selectProductById(int id){
        return productMapper.selectProductById(id);
    };

    //修改产品信息
    public int updateProduct(int id, Product product){
        return productMapper.updateProduct(id, product);
    };

    //删除产品
    public int deleteProductById(int id){
        return productMapper.deleteProductById(id);
    };

    //获取某系列所有产品
    public List<Product> selectAllProductsBySeriesId(int seriesId){
        return productMapper.selectAllProductsBySeriesId(seriesId);
    }

    //通过系列Id获取所有产品的数组，供前端选项用
    public List<JSONObject> getAllProductSetBySeriesId(int seriesId) {
        List<JSONObject> list = new ArrayList<>();
        List<Product> list_temp = selectAllProductsBySeriesId(seriesId);
        for (Product product : list_temp) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", product.getNameZh() + "(" + ProductClass.getNameByIndex(product.getClassification()) + ")");
            jsonObject.put("value", product.getId());
            list.add(jsonObject);
        }
        return list;
    }

}
