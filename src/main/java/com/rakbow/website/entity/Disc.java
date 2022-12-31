package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-27 18:49
 * @Description:
 */
@Data
public class Disc {

    private int id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private String barcode;//商品条形码
    private int series;//所属系列id
    private String products;//所属作品id
    private Date releaseDate;//发行日期
    private int price;//发行价格
    private String mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray
    private int isLimited;//发售版本是否为限定版 0-否 1-是
    private String spec;//商品规格
    private int hasBonus;//是否包含特典
    private String bonus;//特典信息
    private String images;//图片 json格式
    private String description;//描述
    private String remark;//备注
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private int _s;//状态

    public Disc () {
        id = 0;
        catalogNo = "";
        name = "";
        nameZh = "";
        nameEn = "";
        barcode = "";
        series = 0;
        products = "{\"ids\":[]}";
        releaseDate = null;
        price = 0;
        mediaFormat = "{\"ids\":[]}";
        isLimited = 0;
        spec = "";
        hasBonus = 0;
        bonus = "";
        images = "[]";
        description = "";
        remark = "";
        addedTime = new Timestamp(System.currentTimeMillis());
        editedTime = new Timestamp(System.currentTimeMillis());
        _s = 1;
    }


}
