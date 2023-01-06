package com.rakbow.website.data.game;

import com.alibaba.fastjson2.JSONObject;

import java.util.ArrayList;
import java.util.List;

public enum ReleaseType {

    UNKNOWN(0, "Unknown", "未知"),
    OFFICIAL_RELEASE(1, "Official Release", "官方发行");

    private int index;
    private String nameEn;
    private String nameZh;

    ReleaseType(int index, String nameEn, String nameZh) {
        this.index = index;
        this.nameEn = nameEn;
        this.nameZh = nameZh;
    }

    public static String index2NameZh (int index) {
        String nameZh = UNKNOWN.nameZh;
        for (ReleaseType releaseType : ReleaseType.values()) {
            if (releaseType.index == index) {
                nameZh = releaseType.nameZh;
            }
        }
        return nameZh;
    }

    public static List<JSONObject> getReleaseTypeSet () {
        List<JSONObject> list = new ArrayList<>();
        for (ReleaseType releaseType : ReleaseType.values()) {
            JSONObject jo = new JSONObject();
            jo.put("labelEn", releaseType.nameEn);
            jo.put("labelZh", releaseType.nameZh);
            jo.put("value", releaseType.index);
            list.add(jo);
        }
        return list;
    }

}
