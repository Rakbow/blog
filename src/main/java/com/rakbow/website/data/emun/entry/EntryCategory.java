package com.rakbow.website.data.emun.entry;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.system.SystemLanguage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashMap;
import java.util.Map;

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

    public static final Map<Integer, String> categoryMapZH = Map.ofEntries(
            Map.entry(COMPANY.id, RedisCacheConstant.COMPANY_SET_ZH),
            Map.entry(PERSONNEL.id, RedisCacheConstant.PERSONNEL_SET_ZH),
            Map.entry(MERCHANDISE.id, RedisCacheConstant.MERCHANDISE_SET_ZH),
            Map.entry(ROLE.id, RedisCacheConstant.ROLE_SET_ZH),
            Map.entry(SPEC_PARAMETER.id, RedisCacheConstant.SPEC_PARAMETER_SET_ZH),
            Map.entry(PUBLICATION.id, RedisCacheConstant.PUBLICATION_SET_ZH)
    );

    public static final Map<Integer, String> categoryMapEn = Map.ofEntries(
            Map.entry(COMPANY.id, RedisCacheConstant.COMPANY_SET_EN),
            Map.entry(PERSONNEL.id, RedisCacheConstant.PERSONNEL_SET_EN),
            Map.entry(MERCHANDISE.id, RedisCacheConstant.MERCHANDISE_SET_EN),
            Map.entry(ROLE.id, RedisCacheConstant.ROLE_SET_EN),
            Map.entry(SPEC_PARAMETER.id, RedisCacheConstant.SPEC_PARAMETER_SET_EN),
            Map.entry(PUBLICATION.id, RedisCacheConstant.PUBLICATION_SET_EN)
    );

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
