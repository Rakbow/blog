package com.rakbow.website.data.emun;

import com.rakbow.website.data.Attribute;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-19 19:02
 * @Description:
 */
public interface MetaEmun {

    int getId();
    String getNameZh();
    String getNameEn();

    static <T extends Enum<T> & MetaEmun> String getLocaleNameById(Class<T> enumClass, int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        for (T e : enumClass.getEnumConstants()) {
            if (e.getId() == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return e.getNameEn();
                }else {
                    return e.getNameZh();
                }
            }
        }
        return "Uncategorized";
    }

    static <T extends Enum<T> & MetaEmun> List<Attribute> getAttributeSet(Class<T> enumClass, String lang) {
        List<Attribute> set = new ArrayList<>();
        for (T e : enumClass.getEnumConstants()) {
            if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
                set.add(new Attribute(e.getId(), e.getNameZh()));
            }else if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                set.add(new Attribute(e.getId(), e.getNameEn()));
            }
        }
        return set;
    }

}
