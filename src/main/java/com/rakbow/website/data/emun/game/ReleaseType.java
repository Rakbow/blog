package com.rakbow.website.data.emun.game;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public enum ReleaseType {

    UNKNOWN(0, "Unknown", "未知"),
    OFFICIAL_RELEASE(1, "Official Release", "官方发行");

    @Getter
    private final int index;
    @Getter
    private final String nameEn;
    @Getter
    private final String nameZh;

    public static String index2NameZh (int index) {
        String nameZh = UNKNOWN.nameZh;
        for (ReleaseType releaseType : ReleaseType.values()) {
            if (releaseType.index == index) {
                nameZh = releaseType.nameZh;
            }
        }
        return nameZh;
    }

    public static JSONArray getReleaseTypeSet () {
        JSONArray list = new JSONArray();
        for (ReleaseType releaseType : ReleaseType.values()) {
            JSONObject jo = new JSONObject();
            jo.put("labelEn", releaseType.nameEn);
            jo.put("labelZh", releaseType.nameZh);
            jo.put("value", releaseType.index);
            list.add(jo);
        }
        return list;
    }

    public static JSONObject getReleaseTypeJson(int releaseTypeId) {
        JSONObject releaseType = new JSONObject();
        releaseType.put("id", releaseTypeId);
        releaseType.put("nameZh", ReleaseType.index2NameZh(releaseTypeId));
        return releaseType;
    }

}
