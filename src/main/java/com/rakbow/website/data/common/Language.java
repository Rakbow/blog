package com.rakbow.website.data.common;

import org.apache.commons.lang3.StringUtils;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:30
 * @Description:
 */
public enum Language {

    UNCLASSIFIED("Unclassified", "未分类", "Unknown"),
    JAPANESE("Japanese", "日语", "ja-JP"),
    SIMPLIFIED_CHINESE("Simplified Chinese", "简体中文", "zh-CN"),
    TRADITIONAL_CHINESE("Traditional Chinese", "繁体中文", "zh-TW"),
    ENGLISH("English", "英语", "en-US");

    private String nameEn;
    private String nameZh;
    private String code;

    Language(String nameEn, String nameZh, String code) {
        this.nameEn = nameEn;
        this.nameZh = nameZh;
        this.code = code;
    }

    public static String languageCode2NameZh (String code) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (Language language : Language.values()) {
            if (StringUtils.equals(language.code, code)) {
                nameZh = language.nameZh;
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
}
