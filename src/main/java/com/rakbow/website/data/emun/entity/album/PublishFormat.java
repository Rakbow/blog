package com.rakbow.website.data.emun.entity.album;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.Attribute;
import com.rakbow.website.data.emun.common.MediaFormat;
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
 * @Create: 2022-08-19 22:34
 * @Description: 出版形式
 */
@AllArgsConstructor
public enum PublishFormat {
    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    COMMERCIAL(1, "商业发行", "Commercial"),
    INDIE_DOUJIN(2,"独立同人", "Doujin"),
    BONUS(3,"同捆特典", "Bonus"),
    EVENT_ONLY(4,"展会限定", "Event Only"),
    PREORDER(5,"预约特典", "Preorder");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang) {
        for (PublishFormat item : PublishFormat.values()) {
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

    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (PublishFormat item : PublishFormat.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (PublishFormat item : PublishFormat.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

    public static JSONArray getAttribute(String json) {

        String lang = LocaleContextHolder.getLocale().getLanguage();

        JSONArray res = new JSONArray();

        List<Integer> ids = CommonUtil.ids2List(json);

        ids.forEach(id -> {
            res.add(new Attribute(id, getNameById(id, lang)));
        });

        return res;
    }
}
