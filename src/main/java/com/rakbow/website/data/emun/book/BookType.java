package com.rakbow.website.data.emun.book;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:29
 * @Description:
 */
@AllArgsConstructor
public enum BookType {

    UNCLASSIFIED(0,"未分类", "Unclassified"),
    NOVEL(1,"小说", "Novel"),
    COMIC(2,"漫画", "comic"),
    ANTHOLOGY(3,"作品集", "Anthology"),
    ART_BOOK(4,"原画集/设定集", "Art Book"),
    ELECTRONIC_BOOK(5,"电子书", "e-book"),
    OTHER(6,"其他", "Other");

    @Getter
    private final int index;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

    public static String index2NameZh (int index) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (BookType bookType : BookType.values()) {
            if (bookType.index == index) {
                nameZh = bookType.nameZh;
            }
        }
        return nameZh;
    }

    /**
     * 获取图书分类数组
     *
     * @return list 图书分类数组
     * @author rakbow
     */
    public static JSONArray getBookTypeSet() {
        JSONArray list = new JSONArray();
        for (BookType bookType : BookType.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", bookType.getNameZh());
            jsonObject.put("labelEn", bookType.getNameEn());
            jsonObject.put("value", bookType.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
