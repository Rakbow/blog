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
    Product getProduct(int id);

    //获取所有作品
    List<Product> getAll();

    //修改系列信息
    int updateProduct(int id, Product product);

    //删除产品
    int deleteProduct(int id);

    //更新描述
    void updateProductDescription(int id, String description, Timestamp editedTime);

    //更新staff
    void updateProductStaffs(int id, String staffs, Timestamp editedTime);

    //更新图片
    void updateProductImages(int id, String images, Timestamp editedTime);

    //更新组织
    void updateProductOrganizations(int id, String organizations, Timestamp editedTime);

    //条件搜索
    List<Product> getProductsByFilter(String name, String nameZh, List<Integer> franchises, List<Integer> categories,

                                          boolean status, String sortField, int sortOrder, int first, int row);
    //条件搜索数量
    int getProductsRowsByFilter(String name, String nameZh, List<Integer> franchises, List<Integer> categories, boolean status);

}
