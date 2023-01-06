package com.rakbow.website.data.common;

import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:00
 * @Description:
 */
public enum Region {

    JAPAN("Japan", "日本", "jp", "JPY"),
    CHINA("China", "中国大陆", "cn", "CNY"),
    TAIWAN("Taiwan", "台湾地区", "tw", "TWD"),
    EUROPE("Europe", "欧洲", "eu", "EUR"),
    UNITED_STATES("United States", "美国", "us", "USD");

    private String nameEn;
    private String nameZh;
    private String code;
    private String currency;

    Region(String nameEn, String nameZh, String code, String currency) {
        this.nameEn = nameEn;
        this.nameZh = nameZh;
        this.code = code;
        this.currency = currency;
    }

    //根据地区代码获取货币符号
    public static String regionCode2Currency(String code){
        String currency = JAPAN.currency;
        for (Region region : Region.values()) {
            if (StringUtils.equals(region.code, code)) {
                currency = region.currency;
            }
        }
        return currency;
    }

    //根据地区代码获取地区名称(中文)
    public static String regionCode2NameZh(String code){
        String nameZh = JAPAN.nameZh;
        for (Region region : Region.values()) {
            if (StringUtils.equals(region.code, code)) {
                nameZh = region.nameZh;
            }
        }
        return nameZh;
    }

    /**
     * 获取地区数组
     *
     * @return list
     * @author rakbow
     */
    public static List<JSONObject> getRegionSet () {
        List<JSONObject> list = new ArrayList<>();
        for (Region region : Region.values()) {
            JSONObject jo = new JSONObject();
            jo.put("nameZh", region.nameZh);
            jo.put("nameEn", region.nameEn);
            jo.put("code", region.code);
            jo.put("currency", region.currency);
            list.add(jo);
        }
        return list;
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
