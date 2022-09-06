package com.rakbow.blog.entity;


import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-07-19 0:55
 * @Description: 专辑实体类
 */
public class Album {

    private int id;//表主键
    private String catalogNo;//专辑编号
    private String nameJp;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    @DateTimeFormat(pattern="yyyy/MM/dd")//存到数据库
    @JSONField(format = "yyyy/MM/dd") //从数据库读出
    private Date releaseDate;//发行日期
    private String publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private String albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private String mediaFormat;//媒体类型
    private int publishPrice;//发行价格（含税）
    private String label;//唱片公司
    private String publisher;//发行商
    private String distributor;//经销商
    private String copyright;//版权方
    private int discNum;//唱片数
    private int trackNum;//曲目数
    private int hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息
    private int seriesId;//所属系列id
    private String productId;//所属产品id 在mysql中以数组字符串形式存储
    private String description;//描述
    private String remark;//备注
    private String credits;//staff
    private String imgUrl;//图片列表（JSON字符串）
    private String trackList;//曲目列表（JSON字符串）
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
        this.publishFormat = "";
        this.albumFormat = "";
        this.mediaFormat = "";
        this.publishPrice = 0;
        this.label = "";
        this.publisher = "";
        this.distributor = "";
        this.copyright = "";
        this.discNum = 0;
        this.trackNum = 0;
        this.hasBonus = 0;
        this.bonus = "";
        this.productId = "";
        this.description = "";
        this.remark = "";
        this.credits = "[]";
        this.imgUrl = "[]";
        this.trackList = "[]";
        this.addedTime = null;
        this.editedTime = null;
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

    public int getDiscNum() {
        return discNum;
    }

    public void setDiscNum(int discNum) {
        this.discNum = discNum;
    }

    public int getTrackNum() {
        return trackNum;
    }

    public void setTrackNum(int trackNum) {
        this.trackNum = trackNum;
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

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
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

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getTrackList() {
        return trackList;
    }

    public void setTrackList(String trackList) {
        this.trackList = trackList;
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
                ", discNum=" + discNum +
                ", trackNum=" + trackNum +
                ", hasBonus=" + hasBonus +
                ", bonus='" + bonus + '\'' +
                ", seriesId=" + seriesId +
                ", productId='" + productId + '\'' +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", credits='" + credits + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", trackList='" + trackList + '\'' +
                ", addedTime=" + addedTime +
                ", editedTime=" + editedTime +
                ", _s=" + _s +
                '}';
    }
}
