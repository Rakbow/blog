package com.rakbow.website.data.emun.entity.game;

import com.rakbow.website.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@AllArgsConstructor
public enum ReleaseType {

    UNKNOWN(0, "Unknown", "未知"),
    OFFICIAL_RELEASE(1, "Official Release", "官方发行");

    @Getter
    private final int id;
    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;

    public static String getNameById(int id, String lang) {
        for (ReleaseType item : ReleaseType.values()) {
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

    public static List<Attribute> getAttributeSet(String lang) {
        List<Attribute> set = new ArrayList<>();
        if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
            for (ReleaseType item : ReleaseType.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
            for (ReleaseType item : ReleaseType.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

    public static Attribute getAttribute(int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new Attribute(id, getNameById(id, lang));
    }

}
