package com.rakbow.website.data.emun.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.system.SystemLanguage;
import com.rakbow.website.util.common.CommonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.List;

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
    public static String getNamesByIds(JSONArray ids) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        String[] names = new String[ids.size()];
        for (int i = 0; i < ids.size(); i++) {
            names[i] = getNameById(ids.getIntValue(i), lang);
        }
        return StringUtils.join(names, ",");
    }

    public static JSONArray getIdsByNames(JSONArray names) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if (!names.isEmpty()) {
            JSONArray ids = new JSONArray();
            for (int i = 0; i < names.size(); i++) {
                ids.add(getIdByName(names.getString(i), lang));
            }
            return ids;
        }else {
            return new JSONArray();
        }
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

    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
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

    public static JSONArray getAttributes(String json) {

        String lang = LocaleContextHolder.getLocale().getLanguage();

        JSONArray res = new JSONArray();

        List<Integer> ids = CommonUtil.ids2List(json);

        ids.forEach(id -> {
            res.add(new Attribute(id, getNameById(id, lang)));
        });

        return res;
    }

}
