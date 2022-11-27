package com.rakbow.website.dao;

import com.rakbow.website.entity.Product;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:49
 * @Description:
 */
@Mapper
public interface ProductMapper {

    //新增产品
    int addProduct(Product product);

    //通过id查找作品
    Product selectProductById(int id);

    //获取所有作品
    List<Product> getAll();

    //修改系列信息
    int updateProduct(int id, Product product);

    //删除产品
    int deleteProductById(int id);

    //获取某系列所有产品
    List<Product> selectAllProductsBySeriesId(int seriesId);

    //更新描述
    int updateProductDescription(int id, String description, Timestamp editedTime);

    //更新staff
    int updateProductStaffs(int id, String staffs, Timestamp editedTime);

    //更新图片
    int updateProductImages(int id, String images, Timestamp editedTime);

}
