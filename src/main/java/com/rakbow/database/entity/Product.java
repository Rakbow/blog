package com.rakbow.database.entity;

import java.util.Date;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-20 1:43
 * @Description:
 */
public class Product {

    private int id;
    private int seriesId;
    private String nameZh;
    private String nameJp;
    private String nameEn;
    private Date releaseDate;
    private int classification;
    private String description;
    private String remark;
    private Date addedTime;
    private Date editedTime;
    private String imgUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
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

    public int getClassification() {
        return classification;
    }

    public void setClassification(int classification) {
        this.classification = classification;
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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", seriesId=" + seriesId +
                ", nameZh='" + nameZh + '\'' +
                ", nameJp='" + nameJp + '\'' +
                ", nameEn='" + nameEn + '\'' +
                ", releaseDate=" + releaseDate +
                ", classification=" + classification +
                ", description='" + description + '\'' +
                ", remark='" + remark + '\'' +
                ", addedTime=" + addedTime +
                ", editedTime=" + editedTime +
                ", imgUrl='" + imgUrl + '\'' +
                '}';
    }
}
