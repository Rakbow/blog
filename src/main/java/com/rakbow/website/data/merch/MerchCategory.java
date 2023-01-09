package com.rakbow.website.data.merch;

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
    BADGE(1,"徽章", "Badge"),
    ILLUSTRATE(2,"色纸", "Illustration"),
    POSTERS(3,"海报", "Posters"),
    TOY(4,"小玩具", "Toy"),
    FIGURE(5,"手办", "Figure"),
    OTHER(6,"其他", "Other");

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
    public static List<JSONObject> getMerchCategorySet() {
        List<JSONObject> list = new ArrayList<>();
        for (MerchCategory merchCategory : MerchCategory.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", merchCategory.getNameZh());
            jsonObject.put("labelEn", merchCategory.getNameEn());
            jsonObject.put("value", merchCategory.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
