package com.rakbow.website.data.emun.common;

import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.system.SystemLanguage;
import com.rakbow.website.util.common.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:04
 * @Description: 媒体类型
 */
@AllArgsConstructor
public enum MediaFormat {
    UNCATEGORIZED(0, "未分类", "Uncategorized"),
    CD(1, "CD", "CD"),
    DVD(2, "DVD", "DVD"),
    BLU_RAY(3, "Blu-ray", "Blu-ray"),
    DIGITAL(4, "数字专辑", "Digital");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    /**
     * index列表转用逗号隔开的nameEn数组字符串
     *
     * @param ids index的JSONArray数组
     * @return String
     * @author rakbow
     */
    public static List<String> getNamesByIds(List<Integer> ids) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return ids.stream().map(id -> getNameById(id, lang)).collect(Collectors.toList());
    }

    public static List<Integer> getIdsByNames(List<String> names) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if (names.isEmpty()) return new ArrayList<>();
        return names.stream().map(name -> getIdByName(name, lang)).collect(Collectors.toList());
    }

    public static String getNameById(int id, String lang) {
        for (MediaFormat item : MediaFormat.values()) {
            if (item.getId() == id) {
                if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
                    return item.getNameEn();
                }else {
                    return item.getNameZh();
                }
            }
        }
        return null;
    }

    public static int getIdByName(String name, String lang) {
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (MediaFormat format : MediaFormat.values()) {
                if(StringUtils.equals(name, format.nameEn)) {
                    return format.id;
                }
            }
        }else {
            for (MediaFormat format : MediaFormat.values()) {
                if(StringUtils.equals(name, format.nameZh)) {
                    return format.id;
                }
            }
        }
        return 0;
    }

    public static List<Attribute> getAttributeSet(String lang) {
        List<Attribute> set = new ArrayList<>();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (MediaFormat item : MediaFormat.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (MediaFormat item : MediaFormat.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

    public static List<Attribute> getAttributes(String json) {

        String lang = LocaleContextHolder.getLocale().getLanguage();

        List<Attribute> res = new ArrayList<>();

        List<Integer> ids = CommonUtil.ids2List(json);

        ids.forEach(id -> {
            res.add(new Attribute(id, getNameById(id, lang)));
        });

        return res;
    }

}
