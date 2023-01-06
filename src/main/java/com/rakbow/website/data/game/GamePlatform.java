package com.rakbow.website.data.game;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum GamePlatform {

    UNKNOWN(0, "Unknown", "未知"),
    PC(1, "PC", "PC"),
    WEB(2, "Web", "网页端"),
    ANDROID(3, "Android", "安卓"),
    APPLE_IOS(4, "Apple iOS", "苹果");

    private int index;
    private String nameEn;
    private String nameZh;

    GamePlatform(int index, String nameEn, String nameZh) {
        this.index = index;
        this.nameEn = nameEn;
        this.nameZh = nameZh;
    }

    public static String index2Name (int index) {
        String nameEn = UNKNOWN.nameEn;
        for (GamePlatform gamePlatform : GamePlatform.values()) {
            if (gamePlatform.index == index) {
                nameEn = gamePlatform.nameEn;
            }
        }
        return nameEn;
    }

    public static List<JSONObject> getGamePlatformSet () {
        List<JSONObject> list = new ArrayList<>();
        for (GamePlatform platform : GamePlatform.values()) {
            JSONObject jo = new JSONObject();
            jo.put("labelEn", platform.nameEn);
            jo.put("labelZh", platform.nameZh);
            jo.put("value", platform.index);
            list.add(jo);
        }
        return list;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }
}
