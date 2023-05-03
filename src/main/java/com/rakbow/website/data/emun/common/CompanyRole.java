package com.rakbow.website.data.emun.common;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.data.emun.system.SystemLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-04-28 21:16
 * @Description:
 */
@AllArgsConstructor
public enum CompanyRole {

    UNCATEGORIZED(0, "未分类", "Uncategorized"),
    MANUFACTURER(1, "制造商", "Manufacturer"),
    PUBLISHER(2, "出版商", "Publisher"),
    DISTRIBUTOR(3, "分销商", "Distributor"),
    DEVELOPER(4, "开发商", "Developer"),
    LABEL(5, "唱片公司", "Label"),
    COPYRIGHT_HOLDER(6, "版权方", "Copyright Holder"),
    RETAILER(7, "零售商", "Retailer"),
    PRODUCER(8, "生产商", "Producer"),
    DOUJIN_GROUP(9, "同人社团", "Doujin Group"),
    INDEPENDENT(10, "独立工作室", "Independent");


    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang) {
        for (CompanyRole item : CompanyRole.values()) {
            if (item.getId() == id) {
                if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
                    return item.nameEn;
                }else {
                    return item.nameZh;
                }
            }
        }
        return null;
    }

    public static Attribute getAttribute(int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new Attribute(id, getNameById(id, lang));
    }

    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (CompanyRole item : CompanyRole.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (CompanyRole item : CompanyRole.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

}
