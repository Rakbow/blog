package com.rakbow.website.data.emun;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:04
 * @Description: 媒体类型
 */
@AllArgsConstructor
public enum MediaFormat {
    UNCLASSIFIED(0, "未分类", "Unclassified"),
    CD(1, "CD", "CD"),
    DVD(2, "DVD", "DVD"),
    BLU_RAY(3, "Blu-ray", "Blu-ray"),
    DIGITAL(4, "数字专辑", "Digital");

    @Getter
    private final int index;
    @Getter
    private final String name;
    @Getter
    private final String nameEn;

    /**
     * index列表转用逗号隔开的nameEn数组字符串
     *
     * @param indexArr index的JSONArray数组
     * @return String
     * @author rakbow
     */
    public static String index2NameEnArrayString(JSONArray indexArr) {
        String[] nameEnArr = new String[indexArr.size()];
        for (int i = 0; i < indexArr.size(); i++) {
            nameEnArr[i] = getNameEnByIndex(indexArr.getIntValue(i));
        }
        return StringUtils.join(nameEnArr, ",");
    }

    public static JSONArray nameEn2IndexArray(JSONArray nameEnArray) {
        if (!nameEnArray.isEmpty()) {
            JSONArray indexArray = new JSONArray();

            for (int i = 0; i < nameEnArray.size(); i++) {
                indexArray.add(getIndexByNameEn(nameEnArray.getString(i)));
            }

            return indexArray;
        }else {
            return null;
        }
    }

    public static String getNameByIndex(int index) {
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getIndex() == index) {
                return mediaFormat.getName();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex(int index) {
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getIndex() == index) {
                return mediaFormat.getNameEn();
            }
        }
        return "Unclassified";
    }

    public static int getIndexByName(String name) {
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getName().equals(name)) {
                return mediaFormat.index;
            }
        }
        return 0;
    }

    public static int getIndexByNameEn(String nameEn) {
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getNameEn().equals(nameEn)) {
                return mediaFormat.index;
            }
        }
        return 0;
    }

    /**
     * 获取媒体类型数组
     *
     * @return list 媒体类型数组
     * @author rakbow
     */
    public static JSONArray getMediaFormatSet() {
        JSONArray list = new JSONArray();
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", mediaFormat.getName());
            jsonObject.put("labelEn", mediaFormat.getNameEn());
            jsonObject.put("value", mediaFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
    }

}
