package com.rakbow.website.entity;

import lombok.Data;

import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 1:43
 * @Description:
 */
@Data
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
    private String images;

}
