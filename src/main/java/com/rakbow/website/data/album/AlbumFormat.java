package com.rakbow.website.data.album;

import com.alibaba.fastjson2.JSONArray;
import org.apache.commons.lang3.StringUtils;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 22:57
 * @Description: 专辑分类
 */
public enum AlbumFormat {
    UNCLASSIFIED(0,"未分类","Unclassified"),
    THEME(1, "主题曲","Vocal"),
    ORIGINAL_SOUNDTRACK(2, "原声","OriginSoundTrack"),
    CHARACTER_SONG(3, "角色曲","CharacterSong"),
    DRAMA(4, "广播剧","Drama"),
    DOUJIN_MUSIC(5,"同人歌曲","Doujin"),
    VIDEO(6,"影像","Video"),
    ARRANGEMENT(7,"改编曲","Arrangement");

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
