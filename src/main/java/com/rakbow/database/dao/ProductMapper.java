package com.rakbow.database.dao;

import com.rakbow.database.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-20 1:49
 * @Description:
 */
@Mapper
public interface ProductMapper {

    //新增产品
    int insertProduct(Product product);

    //通过id查找产品
    Product selectProductById(int id);

    //修改系列信息
    int updateProduct(int id, Product product);

    //删除产品
    int deleteProductById(int id);

    //获取某系列所有产品
    List<Product> selectAllProductsBySeriesId(int seriesId);

}
