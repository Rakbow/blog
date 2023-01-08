package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 10:26
 * @Description: 周边实体类
 */
@Data
public class Merch {

    private int id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String barcode;//商品条形码
    private String franchises;//所属系列
    private String products;//所属产品
    private int category;//商品分类
    private Date releaseDate;//发售日
    private int price;//价格
    private String priceUnit;//价格单位
    private int isNotForSale;//是否非卖品
    private String spec;//规格
    private String description;//描述
    private String images;//图片（json）
    private String remark;//备注
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private int _s;//状态

    public Merch() {
        this.id = 0;
        this.name = "";
        this.nameEn = "";
        this.nameZh = "";
        this.barcode = "";
        this.franchises = "{\"ids\": []}";
        this.products = "{\"ids\": []}";
        this.category = 0;
        this.releaseDate = null;
        this.price = 0;
        this.priceUnit = "";
        this.isNotForSale = 0;
        this.spec = "[]";
        this.description = "";
        this.images = "[]";
        this.remark = "";
        this.addedTime = new Timestamp(System.currentTimeMillis());;
        this.editedTime = new Timestamp(System.currentTimeMillis());;
        this._s = 1;
    }

}
