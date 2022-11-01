package com.rakbow.website.entity;


import com.alibaba.fastjson2.annotation.JSONField;
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
public class Album {

    @Id
    private int id;//表主键
    @Field(type = FieldType.Text)
    private String catalogNo;//专辑编号
    @Field(type = FieldType.Text)
    private String nameJp;//专辑名称（日语）
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
    private int publishPrice;//发行价格（含税）
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
    private int series;//所属系列
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
        this.nameJp = "";
        this.nameZh = "";
        this.nameEn = "";
        this.barcode = "";
        this.releaseDate = null;
        this.publishFormat = "{\"ids\":[]}";
        this.albumFormat = "{\"ids\":[]}";
        this.mediaFormat = "{\"ids\":[]}";
        this.publishPrice = 0;
        this.label = "";
        this.publisher = "";
        this.distributor = "";
        this.copyright = "";
        this.hasBonus = 0;
        this.bonus = "";
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCatalogNo() {
        return catalogNo;
    }

    public void setCatalogNo(String catalogNo) {
        this.catalogNo = catalogNo;
    }

    public String getNameJp() {
        return nameJp;
    }

    public void setNameJp(String nameJp) {
        this.nameJp = nameJp;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPublishFormat() {
        return publishFormat;
    }

    public void setPublishFormat(String publishFormat) {
        this.publishFormat = publishFormat;
    }

    public String getAlbumFormat() {
        return albumFormat;
    }

    public void setAlbumFormat(String albumFormat) {
        this.albumFormat = albumFormat;
    }

    public String getMediaFormat() {
        return mediaFormat;
    }

    public void setMediaFormat(String mediaFormat) {
        this.mediaFormat = mediaFormat;
    }

    public int getPublishPrice() {
        return publishPrice;
    }

    public void setPublishPrice(int publishPrice) {
        this.publishPrice = publishPrice;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDistributor() {
        return distributor;
    }

    public void setDistributor(String distributor) {
        this.distributor = distributor;
    }

    public String getCopyright() {
        return copyright;
    }

    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    public int getHasBonus() {
        return hasBonus;
    }

    public void setHasBonus(int hasBonus) {
        this.hasBonus = hasBonus;
    }

    public String getBonus() {
        return bonus;
    }

    public void setBonus(String bonus) {
        this.bonus = bonus;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getTrackInfo() {
        return trackInfo;
    }

    public void setTrackInfo(String trackInfo) {
        this.trackInfo = trackInfo;
    }

    public Timestamp getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Timestamp addedTime) {
        this.addedTime = addedTime;
    }

    public Timestamp getEditedTime() {
        return editedTime;
    }

    public void setEditedTime(Timestamp editedTime) {
        this.editedTime = editedTime;
    }

    public int get_s() {
        return _s;
    }

    public void set_s(int _s) {
        this._s = _s;
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", catalogNo='" + catalogNo + '\'' +
                ", nameJp='" + nameJp + '\'' +
                ", nameZh='" + nameZh + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", barcode='" + barcode + '\'' +
                ", releaseDate=" + releaseDate +
                ", publishFormat='" + publishFormat + '\'' +
                ", albumFormat='" + albumFormat + '\'' +
                ", mediaFormat='" + mediaFormat + '\'' +
                ", publishPrice=" + publishPrice +
                ", label='" + label + '\'' +
                ", publisher='" + publisher + '\'' +
                ", distributor='" + distributor + '\'' +
                ", copyright='" + copyright + '\'' +
                ", hasBonus=" + hasBonus +
                ", bonus='" + bonus + '\'' +
                ", series='" + series + '\'' +
                ", products='" + products + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", artists='" + artists + '\'' +
                ", images='" + images + '\'' +
                ", trackInfo='" + trackInfo + '\'' +
                ", addedTime=" + addedTime +
                ", editedTime=" + editedTime +
                ", _s=" + _s +
                '}';
    }
}
