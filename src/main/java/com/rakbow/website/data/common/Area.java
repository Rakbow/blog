package com.rakbow.website.data.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:00
 * @Description:
 */
public enum Area {

    JAPAN("Japan", "日本", "JP", "JPY"),
    CHINA("China", "中国大陆", "CN", "CNY"),
    TAIWAN("Taiwan", "台湾地区", "TW", "TWD"),
    EUROPE("Europe", "欧洲", "EU", "EUR"),
    UNITED_STATES("United States", "美国", "US", "USD"),
    OTHER("Other", "其他地区", "OTHER", "?");

    private String nameEn;
    private String nameZh;
    private String code;
    private String currency;

    Area(String nameEn, String nameZh, String code, String currency) {
        this.nameEn = nameEn;
        this.nameZh = nameZh;
        this.code = code;
        this.currency = currency;
    }

    //根据地区代码获取货币符号
    public static String areaCode2Currency(String code){
        String currency = OTHER.currency;
        for (Area area : Area.values()) {
            if (StringUtils.equals(area.code, code)) {
                currency = area.currency;
            }
        }
        return currency;
    }

    //根据地区代码获取地区名称(中文)
    public static String areaCode2NameZh(String code){
        String nameZh = OTHER.nameZh;
        for (Area area : Area.values()) {
            if (StringUtils.equals(area.code, code)) {
                nameZh = area.nameZh;
            }
        }
        return nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}
