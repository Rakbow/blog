package com.rakbow.website.data.emun.entity.product;

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
 * @Create: 2022-08-20 2:54
 * @Description:
 */
@AllArgsConstructor
public enum ProductCategory {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    GAME(1, "游戏", "Game"),
    ANIMATION(2, "TV动画/动画电影", "Animation"),
    OVA_OAD(3, "OVA/OAD", "OVA/OAD"),

    NOVEL(4, "小说", "Novel"),
    MANGA(5, "漫画", "Manga"),
    PUBLICATION(6, "其他出版物", "Publication"),
    LIVE_ACTION_MOVIE(7, "真人电影", "Live Action Movie"),
    TV_SERIES(8, "电视剧", "TV Series"),
    MISC(99, "杂项", "Misc");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang){
        for (ProductCategory item : ProductCategory.values()) {
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

    /**
     * 获取作品分类数组
     *
     * @return list 作品分类数组
     * @author rakbow
     */
    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (ProductCategory item : ProductCategory.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (ProductCategory item : ProductCategory.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

}
