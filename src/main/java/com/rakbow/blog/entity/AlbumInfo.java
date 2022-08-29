package com.rakbow.blog.entity;

import java.util.Date;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-07-30 17:44
 * @Description:
 */
public class AlbumInfo {
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑原名
    private String nameZh;//专辑译名（中文）
    private String releaseDate;//发行日期
    private String artist;//
    private String series;//所属系列
    private String type;//分类：0-未分类 1-主题曲 2-动画原声 3-角色曲 4-广播剧
    private int discNum;//唱片数
    private int trackNum;//曲目数
    private String publisher;//发行商
    private int price;//价格
    private String remark;//备注
    private String description;//描述
    private String coverUrl;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    @Override
    public String toString() {
        return "AlbumInfo{" +
                "id=" + id +
                ", catalogNo='" + catalogNo + '\'' +
                ", name='" + name + '\'' +
                ", nameZh='" + nameZh + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", artist='" + artist + '\'' +
                ", series='" + series + '\'' +
                ", type='" + type + '\'' +
                ", discNum=" + discNum +
                ", trackNum=" + trackNum +
                ", publisher='" + publisher + '\'' +
                ", price=" + price +
                ", remark='" + remark + '\'' +
                ", description='" + description + '\'' +
                ", coverUrl='" + coverUrl + '\'' +
                '}';
    }
}
