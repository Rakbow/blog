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
    private long id;
    private String title;
    private String subtitle;
    private String isbn;
    private String barcode;
    private int bookType;
    private int seriesId;
    private String author;
    private String translator;
    private String illustrator;
    private String publisher;
    private Date publish_date;
    private int price;
    private String designed;
    private String bonus;
    private String description;
    private String images;
    private String remark;
    private Timestamp addedTime;
    private Timestamp editedTime;
    private int _s;

    public Book() {
        this.id = 0l;
        this.title = "";
        this.subtitle = "";
        this.isbn = "";
        this.barcode = "";
        this.bookType = 0;
        this.seriesId = 0;
        this.author = "";
        this.translator = "";
        this.illustrator = "";
        this.publisher = "";
        this.publish_date = null;
        this.price = 0;
        this.designed = "";
        this.bonus = "";
        this.description = "";
        this.images = "[]";
        this.remark = "";
        this.addedTime = new Timestamp(System.currentTimeMillis());;
        this.editedTime = new Timestamp(System.currentTimeMillis());;
        this._s = 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTranslator() {
        return translator;
    }

    public void setTranslator(String translator) {
        this.translator = translator;
    }

    public String getIllustrator() {
        return illustrator;
    }

    public void setIllustrator(String illustrator) {
        this.illustrator = illustrator;
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

    public String getDesigned() {
        return designed;
    }

    public void setDesigned(String designed) {
        this.designed = designed;
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
                ", isbn='" + isbn + '\'' +
                ", barcode='" + barcode + '\'' +
                ", bookType=" + bookType +
                ", seriesId=" + seriesId +
                ", author='" + author + '\'' +
                ", translator='" + translator + '\'' +
                ", illustrator='" + illustrator + '\'' +
                ", publisher='" + publisher + '\'' +
                ", publish_date=" + publish_date +
                ", price=" + price +
                ", designed='" + designed + '\'' +
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
