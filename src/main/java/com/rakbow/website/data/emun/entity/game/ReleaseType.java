package com.rakbow.website.data.emun.entity.game;

import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.MetaEmun;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

@AllArgsConstructor
public enum ReleaseType implements MetaEmun {

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

    public static Attribute getAttribute(int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new Attribute(id, getNameById(id, lang));
    }

}
