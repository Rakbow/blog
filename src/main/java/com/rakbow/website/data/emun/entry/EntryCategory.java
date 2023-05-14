package com.rakbow.website.data.emun.entry;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.system.SystemLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 4:23
 * @Description:
 */
@AllArgsConstructor
public enum EntryCategory {

    MISC(0, "杂项", "Misc"),
    COMPANY(1, "企业/组织", "Company"),
    PERSONNEL(2, "相关人员", "Personnel"),
    MERCHANDISE(3, "商品类型", "Merchandise"),
    ROLE(4, "职位", "Role"),
    CHARACTER(5, "角色", "Character"),
    MATERIAL(6, "原料", "Material"),
    EVENT(7, "活动/展会", "Event"),
    SPEC_PARAMETER(8, "规格参数", "Spec Parameter"),
    PUBLICATION(9, "书刊", "Publication");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang) {
        for (EntryCategory item : EntryCategory.values()) {
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
            for (EntryCategory item : EntryCategory.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (EntryCategory item : EntryCategory.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

}
