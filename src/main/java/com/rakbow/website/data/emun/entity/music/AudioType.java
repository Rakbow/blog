package com.rakbow.website.data.emun.entity.music;

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
 * @Create: 2022-11-06 20:31
 * @Description:
 */
@AllArgsConstructor
public enum AudioType implements MetaEmun {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    VOCAL(1, "歌曲","Vocal"),
    INSTRUMENTAL(2, "歌曲(无伴奏)","Instrumental"),
    ORIGINAL_SOUNDTRACK(3, "原声","Origin sound track"),
    DRAMA(4, "广播剧","Drama");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang){
        for (AudioType audioType : AudioType.values()) {
            if (audioType.getId() == id) {
                if(StringUtils.equals(lang, Locale.ENGLISH.getLanguage())) {
                    return audioType.getNameEn();
                }else {
                    return audioType.getNameZh();
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
