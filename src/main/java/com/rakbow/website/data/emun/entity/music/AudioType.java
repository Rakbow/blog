package com.rakbow.website.data.emun.entity.music;

import com.rakbow.website.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 20:31
 * @Description:
 */
@AllArgsConstructor
public enum AudioType {
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
                if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
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

    /**
     * 获取音乐分类数组
     * @author rakbow
     * @return list
     * */
    public static List<Attribute> getAttributeSet(String lang){
        List<Attribute> set = new ArrayList<>();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (AudioType item : AudioType.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (AudioType item : AudioType.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

}
