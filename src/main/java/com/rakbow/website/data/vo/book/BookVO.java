package com.rakbow.website.data.vo.book;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.sql.Timestamp;
import java.util.Date;
import java.util.PropertyResourceBundle;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class BookVO {

    //基础信息
    private int id;//主键编号
    private String title;//标题（原文）
    private String titleEn;//标题（英文）
    private String titleZh;//标题（中文）
    private String isbn10;//国际标准书号（10位）
    private String isbn13;//国际标准书号（13位）
    private JSONObject bookType;//书籍类型 0-未分类 1-小说 2-漫画 3-设定集/原画集/公式书 4-其他
    private String publishDate;//出版日期
    private JSONObject region;//地区
    private JSONObject publishLanguage;//语言
    private int price;//出版价格
    private String currencyUnit;//货币单位
    private String publisher;//出版社
    private String summary;//简介
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    private JSONArray companies;
    private JSONArray editCompanies;

    private JSONArray personnel;
    private JSONArray editPersonnel;

    private JSONArray specs;
    private JSONArray editSpecs;

    //复杂字段
    private JSONArray authors;//作者（译者，插画，原作者等，json）
    private JSONArray spec;//规格
    private String bonus;//特典信息

}
