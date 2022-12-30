package com.rakbow.website.entity;

import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-19 0:26
 * @Description: 书籍实体类
 */
@Data
public class Book {
    private int id;//主键编号
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    private String isbn10;//国际标准书号（10位）
    private String isbn13;//国际标准书号（13位）
    private int bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private int series;//所属系列
    private String products;//所属产品
    private String area;//地区
    private String publishLanguage;//语言
    private String authors;//作者（译者，插画，原作者等，json）
    private String publisher;//出版社
    private Date publishDate;//出版日期
    private int price;//出版价格
    private String summary;//简介
    private String spec;//规格
    private int hasBonus;//是否包含特典
    private String bonus;//特典信息
    private String description;//描述
    private String images;//图片（json）
    private String remark;//备注
    private Timestamp addedTime;//收录时间
    private Timestamp editedTime;//编辑时间
    private int _s;//状态

    public Book() {
        this.id = 0;
        this.title = "";
        this.titleEn = "";
        this.titleZh = "";
        this.isbn10 = "";
        this.isbn13 = "";
        this.bookType = 0;
        this.series = 0;
        this.products = "{\"ids\": []}";
        this.area = "";
        this.publishLanguage = "";
        this.authors = "[]";
        this.publisher = "";
        this.publishDate = null;
        this.price = 0;
        this.summary = "";
        this.spec = "[]";
        this.hasBonus = 0;
        this.bonus = "";
        this.description = "";
        this.images = "[]";
        this.remark = "";
        this.addedTime = new Timestamp(System.currentTimeMillis());;
        this.editedTime = new Timestamp(System.currentTimeMillis());;
        this._s = 1;
    }
}
