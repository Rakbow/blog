package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-06 14:43
 * @Description:
 */
@Data
public class Game {

    private int id;//主键编号
    private String name;//游戏名（原文）
    private String nameZh;//游戏名（中文）
    private String nameEn;//游戏名（英文）
    private String barcode;//商品条形码
    private int series;//所属系列
    private String products;//所属产品
    private Date releaseDate;//发售日期
    private int releaseType;//发售类型
    private int platform;//平台
    private String region;//地区
    private int hasBonus;//是否包含特典
    private String organizations;//相关组织
    private String staffs;//开发制作人员
    private String images;//图片（json）
    private String description;//描述
    private String bonus;//特典信息
    private String remark;//备注
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private int _s;//状态

    public Game () {
        this.id = 0;
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.series = 0;
        this.products = "{\"ids\": []}";
        this.releaseDate = null;
        this.releaseType = 0;
        this.platform = 0;
        this.region = "";
        this.hasBonus = 0;
        this.organizations = "[]";
        this.staffs = "[]";
        this.images = "[]";
        this.description = "";
        this.bonus = "";
        this.remark = "";
        this.addedTime = new Timestamp(System.currentTimeMillis());;
        this.editedTime = new Timestamp(System.currentTimeMillis());;
        this._s = 1;
    }

}
