package com.rakbow.website.data.emun.entity.book;

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
 * @Create: 2022-12-29 21:29
 * @Description:
 */
@AllArgsConstructor
public enum BookType implements MetaEmun {

    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    NOVEL(1,"小说", "Novel"),
    COMIC(2,"漫画", "Comic"),
    ANTHOLOGY(3,"作品集", "Anthology"),
    ART_BOOK(4,"原画集/设定集", "Art Book"),
    ELECTRONIC_BOOK(5,"电子书", "E-book"),
    OTHER(6,"其他", "Other");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang) {
        for (BookType bookType : BookType.values()) {
            if (bookType.id == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return bookType.nameEn;
                }else {
                    return bookType.nameZh;
                }
            }
        }
        return null;
    }

    public static Attribute getAttribute(int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new Attribute(id, BookType.getNameById(id, lang));
    }

}
