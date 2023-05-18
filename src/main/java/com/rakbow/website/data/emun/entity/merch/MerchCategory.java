package com.rakbow.website.data.emun.entity.merch;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.data.Attribute;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:29
 * @Description:
 */
@AllArgsConstructor
public enum MerchCategory {

    UNCATEGORIZED(0,"未分类", "Uncategorized"),
    CARD(1,"收藏卡片", "Card"),
    CLOTHING(2,"衣物", "Clothing"),
    ACCESSORY(3,"小物", "Accessory"),
    STATIONERY(4,"文具", "Stationery"),
    SEAL_STICKER(5,"印章/贴纸", "Seal/Sticker"),
    BADGE(6,"徽章", "Badge"),
    KEY_RING(7,"钥匙挂坠/钥匙圈", "Key ring"),
    BAG(8,"包", "Bag"),
    TOWEL_HANDKERCHIEF(9,"毛巾/手帕", "Towel/Handkerchief"),
    CUSHION_SHEETS_PILLOW_COVER(10,"软垫/床垫/枕套", "Cushion/Sheets/Pillow cover"),
    CALENDAR(11,"日历", "Calendar"),
    POSTER(12,"海报", "Poster"),
    TAPESTRY(13,"挂毯", "Tapestry"),
    POSTCARD_COLORED_PAPER(14,"明信片/色纸", "Postcard/Colored paper"),
    CUP_TABLEWARE(15,"杯子/餐具", "Cup/Tableware"),
    DAILY_NECESSITIES(16,"日用品", "Daily necessities"),
    PLASTIC_MODEL(17,"塑料模型", "Plastic model"),
    FIGURE(18,"手办", "Figure"),
    TOY(19,"玩具", "Toy"),
    STUFFED_TOY(20,"布偶/毛绒玩具", "Stuffed toy"),
    DOLL(21,"娃娃", "Doll"),
    MISC(99,"杂物", "Misc");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String getNameById(int id, String lang) {
        for (MerchCategory merchCategory : MerchCategory.values()) {
            if (merchCategory.id == id) {
                if(StringUtils.equals(lang, Locale.CHINESE.getLanguage())) {
                    return merchCategory.nameZh;
                }else if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
                    return merchCategory.nameEn;
                }
            }
        }
        return null;
    }

    /**
     * 获取周边商品分类数组
     *
     * @return list 周边商品分类数组
     * @author rakbow
     */
    public static JSONArray getAttributeSet(String lang) {
        JSONArray set = new JSONArray();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            for (MerchCategory item : MerchCategory.values()) {
                set.add(new Attribute(item.id, item.nameEn));
            }
        }else if(StringUtils.equals(lang, SystemLanguage.CHINESE.getCode())) {
            for (MerchCategory item : MerchCategory.values()) {
                set.add(new Attribute(item.id, item.nameZh));
            }
        }
        return set;
    }

    public static Attribute getAttribute(int id) {
        String lang = LocaleContextHolder.getLocale().getLanguage();
        return new Attribute(id, MerchCategory.getNameById(id, lang));
    }

}
