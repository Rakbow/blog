package com.rakbow.website.entity;


import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-19 0:55
 * @Description: 专辑实体类
 */
@Document(indexName = "album")
@Data
public class Album {

    @Id
    private int id;//表主键
    @Field(type = FieldType.Text)
    private String catalogNo;//专辑编号
    @Field(type = FieldType.Text)
    private String name;//专辑名称（日语）
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_smart")
    private String nameZh;//专辑名称（中文）
    @Field(type = FieldType.Text)
    private String nameEn;//专辑名称（英语）
    @Field(type = FieldType.Text)
    private String barcode;//商品条形码
    @DateTimeFormat(pattern="yyyy/MM/dd")//存到数据库
    @JSONField(format = "yyyy/MM/dd") //从数据库读出
    @Field(type = FieldType.Date)
    private Date releaseDate;//发行日期
    private String publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private String albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private String mediaFormat;//媒体类型
    @Field(type = FieldType.Integer)
    private int price;//发行价格（含税）
    private String currencyUnit;
    @Field(type = FieldType.Text)
    private String label;//唱片公司
    @Field(type = FieldType.Text)
    private String publisher;//发行商
    @Field(type = FieldType.Text)
    private String distributor;//经销商
    @Field(type = FieldType.Text)
    private String copyright;//版权方
    private int hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息
    private String franchises;//所属系列
    private String products;//所属产品id 在mysql中以数组字符串形式存储
    private String description;//描述
    private String remark;//备注
    private String artists;//staff
    private String images;//图片列表（JSON字符串）
    private String trackInfo;//曲目列表（JSON字符串）
    @DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss")//存到数据库
    @JSONField(format="yyyy/MM/dd HH:mm:ss")
    private Timestamp addedTime;//数据新增时间
    @DateTimeFormat(pattern="yyyy/MM/dd HH:mm:ss")//存到数据库
    @JSONField(format="yyyy/MM/dd HH:mm:ss")
    private Timestamp editedTime;//数据更新时间
    private int _s;//激活状态

    public Album() {

        this.id = 0;
        this.catalogNo = "";
        this.name = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.releaseDate = null;
        this.publishFormat = "{\"ids\":[]}";
        this.albumFormat = "{\"ids\":[]}";
        this.mediaFormat = "{\"ids\":[]}";
        this.price = 0;
        this.currencyUnit = "";
        this.label = "";
        this.publisher = "";
        this.distributor = "";
        this.copyright = "";
        this.hasBonus = 0;
        this.bonus = "";
        this.franchises = "{\"ids\":[]}";
        this.products = "{\"ids\":[]}";
        this.description = "";
        this.remark = "";
        this.artists = "[]";
        this.images = "[]";
        this.trackInfo = "{}";
        this.addedTime = new Timestamp(System.currentTimeMillis());
        this.editedTime = new Timestamp(System.currentTimeMillis());
        this._s = 1;

    }



}
