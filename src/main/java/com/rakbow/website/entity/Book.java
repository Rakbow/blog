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
    private String subtitle;//副标题（原文）
    private String title_zh;//标题（中文）
    private String subtitle_zh;//副标题（中文）
    private String title_en;//标题（英文）
    private String subtitle_en;//副标题（英文）
    private String isbn_10;//国际标准书号（10位）
    private String isbn_13;//国际标准书号（13位）
    private String barcode;//商品条形码
    private int bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private int seriesId;//所属系列
    private String area;//地区
    private String language;//语言
    private String authors;//作者（译者，插画，原作者等，json）
    private String publisher;//出版社
    private Date publish_date;//出版日期
    private int price;//出版价格
    private String spec;//规格
    private String designed;//装帧
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
        this.subtitle = "";
        this.title_zh = "";
        this.subtitle_zh = "";
        this.title_en = "";
        this.subtitle_en = "";
        this.isbn_10 = "";
        this.isbn_13 = "";
        this.barcode = "";
        this.bookType = 0;
        this.seriesId = 0;
        this.area = "";
        this.language = "";
        this.authors = "[]";
        this.publisher = "";
        this.publish_date = null;
        this.price = 0;
        this.spec = "";
        this.designed = "";
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
