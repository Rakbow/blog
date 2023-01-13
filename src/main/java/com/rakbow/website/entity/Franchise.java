package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 0:50
 * @Description: 系列实体类
 */
@Data
public class Franchise {

    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private Date originDate;//发行日期
    private Timestamp addedTime;//创建时间
    private Timestamp editedTime;//更新时间
    private String description;//描述
    private String remark;//备注
    private String images;//图片
    private int _s;//状态

    public Franchise() {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.originDate = null;
        this.addedTime = new Timestamp(System.currentTimeMillis());
        this.editedTime = new Timestamp(System.currentTimeMillis());
        this.description = "";
        this.remark = "";
        this.images = "[]";
        this._s = 1;
    }

}
