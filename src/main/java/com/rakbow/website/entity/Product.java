package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:43
 * @Description:
 */
@Data
public class Product {

    private int id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private Date releaseDate;//发售日期
    private int franchise;//所属系列id
    private int category;//作品分类
    private String images;//图片合集，json格式
    private String organizations;//相关组织
    private String staffs;//staff
    private String description;//描述
    private String remark;//备注
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private int status;//状态

    public Product() {
        id = 0;
        name = "";
        nameZh = "";
        nameEn = "";
        releaseDate = null;
        franchise = 0;
        category = 0;
        images = "[]";
        organizations = "[]";
        description = "";
        staffs = "[]";
        remark = "";
        addedTime = new Timestamp(System.currentTimeMillis());
        editedTime = new Timestamp(System.currentTimeMillis());
        status = 1;
    }
}
