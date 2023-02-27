package com.rakbow.website.data.emun.merch;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:29
 * @Description:
 */
@AllArgsConstructor
public enum MerchCategory {

    UNCLASSIFIED(0,"未分类", "Unclassified"),
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
    private final int index;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String index2NameZh (int index) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (MerchCategory merchCategory : MerchCategory.values()) {
            if (merchCategory.index == index) {
                nameZh = merchCategory.nameZh;
            }
        }
        return nameZh;
    }

    /**
     * 获取周边商品分类数组
     *
     * @return list 周边商品分类数组
     * @author rakbow
     */
    public static JSONArray getMerchCategorySet() {
        JSONArray list = new JSONArray();
        for (MerchCategory merchCategory : MerchCategory.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", merchCategory.getNameZh());
            jsonObject.put("labelEn", merchCategory.getNameEn());
            jsonObject.put("value", merchCategory.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

    public static JSONObject getMerchCategoryJson(int merchCategoryId) {
        JSONObject category = new JSONObject();
        category.put("id", merchCategoryId);
        category.put("nameZh", MerchCategory.index2NameZh(merchCategoryId));
        return category;
    }

}
