package com.rakbow.website.entity;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-19 0:26
 * @Description: 书籍实体类
 */
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getTitle_zh() {
        return title_zh;
    }

    public void setTitle_zh(String title_zh) {
        this.title_zh = title_zh;
    }

    public String getSubtitle_zh() {
        return subtitle_zh;
    }

    public void setSubtitle_zh(String subtitle_zh) {
        this.subtitle_zh = subtitle_zh;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getSubtitle_en() {
        return subtitle_en;
    }

    public void setSubtitle_en(String subtitle_en) {
        this.subtitle_en = subtitle_en;
    }

    public String getIsbn_10() {
        return isbn_10;
    }

    public void setIsbn_10(String isbn_10) {
        this.isbn_10 = isbn_10;
    }

    public String getIsbn_13() {
        return isbn_13;
    }

    public void setIsbn_13(String isbn_13) {
        this.isbn_13 = isbn_13;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public int getBookType() {
        return bookType;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public int getSeriesId() {
        return seriesId;
    }

    public void setSeriesId(int seriesId) {
        this.seriesId = seriesId;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public Date getPublish_date() {
        return publish_date;
    }

    public void setPublish_date(Date publish_date) {
        this.publish_date = publish_date;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getSpec() {
        return spec;
    }

    public void setSpec(String spec) {
        this.spec = spec;
    }

    public String getDesigned() {
        return designed;
    }

    public void setDesigned(String designed) {
        this.designed = designed;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
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
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", title_zh='" + title_zh + '\'' +
                ", subtitle_zh='" + subtitle_zh + '\'' +
                ", title_en='" + title_en + '\'' +
                ", subtitle_en='" + subtitle_en + '\'' +
                ", isbn_10='" + isbn_10 + '\'' +
                ", isbn_13='" + isbn_13 + '\'' +
                ", barcode='" + barcode + '\'' +
                ", bookType=" + bookType +
                ", seriesId=" + seriesId +
                ", area='" + area + '\'' +
                ", language='" + language + '\'' +
                ", authors='" + authors + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publish_date=" + publish_date +
                ", price=" + price +
                ", spec='" + spec + '\'' +
                ", designed='" + designed + '\'' +
                ", hasBonus=" + hasBonus +
                ", bonus='" + bonus + '\'' +
                ", description='" + description + '\'' +
                ", images='" + images + '\'' +
                ", remark='" + remark + '\'' +
                ", addedTime=" + addedTime +
                ", editedTime=" + editedTime +
                ", _s=" + _s +
                '}';
    }
}
