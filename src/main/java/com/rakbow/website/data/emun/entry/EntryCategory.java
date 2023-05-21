package com.rakbow.website.data.emun.entry;

import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.RedisKey;
import com.rakbow.website.data.emun.MetaEmun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 4:23
 * @Description:
 */
@AllArgsConstructor
public enum EntryCategory implements MetaEmun {

    FRANCHISE(0, "系列", "Franchise", RedisKey.FRANCHISE_SET),
    CLASSIFICATION(1, "分类", "Classification", RedisKey.CLASSIFICATION_SET),
    COMPANY(2, "企业/组织", "Company", RedisKey.COMPANY_SET),
    PERSONNEL(3, "相关人员", "Personnel", RedisKey.PERSONNEL_SET),
    MERCH_TYPE(4, "商品类型", "Merchandise", RedisKey.MERCH_TYPE_SET),
    ROLE(5, "职位", "Role", RedisKey.ROLE_SET),
    CHARACTER(6, "角色", "Character", ""),
    MATERIAL(7, "原材料", "Material", ""),
    EVENT(8, "活动", "Event", ""),
    SPEC_PARAM(9, "规格参数", "Spec Parameter", RedisKey.SPEC_PARAM_SET),
    PUBLICATION(10, "书刊", "Publication", RedisKey.PUBLICATION_SET);

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;
    @Getter
    private final String redisKey;

    public String getLocaleKey() {
        return String.format(this.redisKey, LocaleContextHolder.getLocale().getLanguage());
    }

    public static String getZhRedisKeyById(int id) {
        for (EntryCategory item : EntryCategory.values()) {
            if (item.getId() == id) {
                return String.format(item.redisKey, Locale.CHINESE.getLanguage());
            }
        }
        return null;
    }

    public static String getEnRedisKeyById(int id) {
        for (EntryCategory item : EntryCategory.values()) {
            if (item.getId() == id) {
                return String.format(item.redisKey, Locale.ENGLISH.getLanguage());
            }
        }
        return null;
    }

    public static String getNameById(int id, String lang) {
        for (EntryCategory item : EntryCategory.values()) {
            if (item.getId() == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
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

}
