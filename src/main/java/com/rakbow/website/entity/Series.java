package com.rakbow.website.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 0:50
 * @Description: 系列实体类
 */
public class Series {

    private int id;//主键
    private String nameZh;//系列名（中文）
    private String nameJp;//系列名（日语）
    private String nameEn;//系列名（英语）
    private Date releaseDate;//发行日期
    private Date addedTime;//创建时间
    private Date editedTime;//更新时间
    private String description;//描述
    private String remark;//备注
    private String images;//图片

    public Series() {
        this.id = 0;
        this.nameZh = "";
        this.nameJp = "";
        this.nameEn = "";
        this.releaseDate = null;
        this.addedTime = new Timestamp(System.currentTimeMillis());
        this.editedTime = new Timestamp(System.currentTimeMillis());
        this.description = "";
        this.remark = "";
        this.images = "[]";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameJp() {
        return nameJp;
    }

    public void setNameJp(String nameJp) {
        this.nameJp = nameJp;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Date getAddedTime() {
        return addedTime;
    }

    public void setAddedTime(Date addedTime) {
        this.addedTime = addedTime;
    }

    public Date getEditedTime() {
        return editedTime;
    }

    public void setEditedTime(Date editedTime) {
        this.editedTime = editedTime;
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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "Series{" +
                "id=" + id +
                ", nameZh='" + nameZh + '\'' +
                ", nameJp='" + nameJp + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", releaseDate=" + releaseDate +
                ", addedTime=" + addedTime +
                ", editedTime=" + editedTime +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", images='" + images + '\'' +
                '}';
    }
}
