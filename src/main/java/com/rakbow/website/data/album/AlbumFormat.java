package com.rakbow.website.data.album;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 22:57
 * @Description: 专辑分类
 */
public enum AlbumFormat {
    UNCLASSIFIED(0,"未分类","Unclassified"),
    VOCAL(1, "歌曲","Vocal"),
    OPENING_THEME(2, "片头曲","Opening Theme"),
    ENDING_THEME(3, "片尾曲","Ending Theme"),
    INSERT_SONG(4, "插入曲","Insert Song"),
    SOUNDTRACK(5, "原声","Soundtrack"),
    CHARACTER_SONG(6, "角色曲","Character Song"),
    DRAMA(7, "广播剧","Drama"),
    TALK(8, "广播电台","Talk"),
    REMIX(9, "混音","Remix"),
    DOUJIN_REMIX(10, "同人混音","Doujin Remix"),
    DERIVATIVE(11, "衍生曲","Derivative"),
    ARRANGEMENT(12, "改编","Arrangement"),
    DOUJIN_ARRANGEMENT(13,"同人改编","Doujin Arrangement"),
    VIDEO(14,"影片","Video");

    private int index;
    private String name;
    private String nameEn;

    AlbumFormat(int index, String name, String nameEn) {
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

    public static String getNameByIndex (int index){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getIndex() == index) {
                return albumFormat.getName();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex (int index){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getIndex() == index) {
                return albumFormat.getNameEn();
            }
        }
        return "Unclassified";
    }

    public static int getIndexByName(String name){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getName().equals(name)) {
                return albumFormat.index;
            }
        }
        return 0;
    }

    public static int getIndexByNameEn(String nameEn){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getNameEn().equals(nameEn)) {
                return albumFormat.index;
            }
        }
        return 0;
    }

    /**
     * 获取专辑分类数组
     *
     * @return list 专辑分类数组
     * @author rakbow
     */
    public static List<JSONObject> getAlbumFormatSet() {
        List<JSONObject> list = new ArrayList<>();
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("label", albumFormat.getName());
            jsonObject.put("labelEn", albumFormat.getNameEn());
            jsonObject.put("value", albumFormat.getIndex());
            list.add(jsonObject);
        }
        return list;
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
