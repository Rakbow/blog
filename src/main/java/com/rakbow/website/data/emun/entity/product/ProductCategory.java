package com.rakbow.website.data.emun.entity.product;

import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.MetaEmun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:54
 * @Description:
 */
@AllArgsConstructor
public enum ProductCategory implements MetaEmun {
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
