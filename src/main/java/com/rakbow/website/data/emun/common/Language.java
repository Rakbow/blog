package com.rakbow.website.data.emun.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:30
 * @Description:
 */
@AllArgsConstructor
public enum Language {

    UNCLASSIFIED("Unclassified", "未分类", "Unknown"),
    JAPANESE("Japanese", "日语", "ja-JP"),
    SIMPLIFIED_CHINESE("Simplified Chinese", "简体中文", "zh-CN"),
    TRADITIONAL_CHINESE("Traditional Chinese", "繁体中文", "zh-TW"),
    ENGLISH("English", "英语", "en-US");

    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;
    @Getter
    private final String code;

    public static String languageCode2NameZh (String code) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (Language language : Language.values()) {
            if (StringUtils.equals(language.code, code)) {
                nameZh = language.nameZh;
            }
        }
        return nameZh;
    }

    /**
    * 获取语言数组
    *
    * @return list
    * @author rakbow
    * */
    public static JSONArray getLanguageSet() {
        JSONArray list = new JSONArray();
        for (Language language : Language.values()) {
            JSONObject jo = new JSONObject();
            jo.put("nameEn", language.nameZh);
            jo.put("nameZh", language.nameZh);
            jo.put("code", language.code);
            list.add(jo);
        }
        return list;
    }

}
