package com.rakbow.website.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.rakbow.website.entity.common.MetaEntity;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.jackson.DateDeserializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Date;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-19 0:55
 * @Description: 专辑实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Album extends MetaEntity {

    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    @JsonDeserialize(using = DateDeserializer.class)
    private Date releaseDate;//发行日期
    private String publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private String albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private String mediaFormat;//媒体类型
    private int price;//发行价格（含税）
    private String currencyUnit;
    private String companies;//相关企业
    private int hasBonus;//是否包含特典内容 0-无 1-有
    private String bonus;//特典信息
    private String franchises;//所属系列
    private String products;//所属产品id 在mysql中以数组字符串形式存储
    private String personnel;
    private String artists;//staff
    private String trackInfo;//曲目列表（JSON字符串）

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
        this.companies = "[]";
        this.hasBonus = 0;
        this.bonus = "";
        this.franchises = "{\"ids\":[]}";
        this.products = "{\"ids\":[]}";
        this.setDescription("");
        this.setRemark("");
        this.personnel = "[]";
        this.artists = "[]";
        this.setImages("[]");
        this.trackInfo = "{}";
        this.setAddedTime(DateUtil.NOW_TIMESTAMP);
        this.setEditedTime(DateUtil.NOW_TIMESTAMP);
        this.setStatus(1);

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Album album = (Album) o;
        return id == album.id &&
                price == album.price &&
                hasBonus == album.hasBonus &&
                Objects.equals(catalogNo, album.catalogNo) &&
                Objects.equals(name, album.name) &&
                Objects.equals(nameZh, album.nameZh) &&
                Objects.equals(nameEn, album.nameEn) &&
                Objects.equals(barcode, album.barcode) &&
                Objects.equals(releaseDate, album.releaseDate) &&
                Objects.equals(publishFormat, album.publishFormat) &&
                Objects.equals(albumFormat, album.albumFormat) &&
                Objects.equals(mediaFormat, album.mediaFormat) &&
                Objects.equals(currencyUnit, album.currencyUnit) &&
                Objects.equals(companies, album.companies) &&
                Objects.equals(bonus, album.bonus) &&
                Objects.equals(franchises, album.franchises) &&
                Objects.equals(products, album.products) &&
                Objects.equals(personnel, album.personnel) &&
                Objects.equals(artists, album.artists) &&
                Objects.equals(trackInfo, album.trackInfo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, catalogNo, name, nameZh, nameEn, barcode, releaseDate, publishFormat, albumFormat, mediaFormat, price, currencyUnit, companies, hasBonus, bonus, franchises, products, personnel, artists, trackInfo);
    }
}
