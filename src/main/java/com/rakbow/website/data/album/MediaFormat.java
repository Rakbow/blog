package com.rakbow.website.data.album;

import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.lang3.StringUtils;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:04
 * @Description: 媒体类型
 */
public enum MediaFormat {
    UNCLASSIFIED(0,"未分类", "Unclassified"),
    CD(1, "CD", "CD"),
    DVD(2, "DVD", "DVD"),
    BLU_RAY(3, "Blu-ray", "Blu-ray"),
    DIGITAL(4,"数字专辑", "Digital");

    private int index;
    private String name;
    private String nameEn;

    MediaFormat(int index, String name, String nameEn) {
        this.index = index;
        this.name = name;
        this.nameEn = nameEn;
    }

    public static String index2NameEnArray (JSONArray indexArr) {
        String[] nameEnArr = new String[indexArr.size()];
        for (int i = 0; i < indexArr.size(); i++) {
            nameEnArr[i] = getNameEnByIndex(indexArr.getIntValue(i));
        }
        return StringUtils.join(nameEnArr, ",");
    }

    public static String getNameByIndex (int index){
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getIndex() == index) {
                return mediaFormat.getName();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex (int index){
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getIndex() == index) {
                return mediaFormat.getNameEn();
            }
        }
        return "Unclassified";
    }

    public static int getIndexByName(String name){
        for (MediaFormat mediaFormat : MediaFormat.values()) {
            if (mediaFormat.getName().equals(name)) {
                return mediaFormat.index;
            }
        }
        return 0;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
