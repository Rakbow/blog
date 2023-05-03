package com.rakbow.website.data.emun.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.system.SystemLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:30
 * @Description:
 */
@AllArgsConstructor
public enum Language {

    JAPANESE("Japanese", "日语", "ja-JP"),
    SIMPLIFIED_CHINESE("Simplified Chinese", "简体中文", "zh-CN"),
    ENGLISH("English", "英语", "en-US"),
    TRADITIONAL_CHINESE("Traditional Chinese", "繁体中文", "zh-TW");

    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;
    @Getter
    private final String code;

    public static String getNameByCode(String code, String lang) {
        for (Language language : Language.values()) {
            if (StringUtils.equals(language.code, code)) {
                if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
                    return language.nameEn;
                }else {
                    return language.nameZh;
                }
            }
        }
        return null;
    }

    /**
    * 获取语言数组
    *
    * @return list
    * @author rakbow
    * */
    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (Language item : Language.values()) {
                JSONObject jo = new JSONObject();
                jo.put("name", item.nameEn);
                jo.put("code", item.code);
                set.add(jo);
            }
        }
        else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (Language item : Language.values()) {
                JSONObject jo = new JSONObject();
                jo.put("name", item.nameZh);
                jo.put("code", item.code);
                set.add(jo);
            }
        }
        return set;
    }

    public static JSONObject getLanguage(String code) {
        JSONObject language = new JSONObject();
        String lang = LocaleContextHolder.getLocale().getLanguage();
        language.put("code", code);
        language.put("name", Language.getNameByCode(code, lang));
        return language;
    }

}
