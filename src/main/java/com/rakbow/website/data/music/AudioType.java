package com.rakbow.website.data.music;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 20:31
 * @Description:
 */
public enum AudioType {
    UNCLASSIFIED(0,"未分类","Unclassified"),
    VOCAL(1, "歌曲","Vocal"),
    INSTRUMENTAL(2, "歌曲(无伴奏)","Instrumental"),
    ORIGINAL_SOUNDTRACK(3, "原声","Origin sound track"),
    DRAMA(4, "广播剧","Drama");

    private int index;
    private String name;
    private String nameEn;

    AudioType(int index, String name, String nameEn) {
        this.index = index;
        this.name = name;
        this.nameEn = nameEn;
    }

    public static String getNameByIndex (int index){
        for (AudioType audioType : AudioType.values()) {
            if (audioType.getIndex() == index) {
                return audioType.getName();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex (int index){
        for (AudioType audioType : AudioType.values()) {
            if (audioType.getIndex() == index) {
                return audioType.getNameEn();
            }
        }
        return "Unclassified";
    }

    public static int getIndexByName(String name){
        for (AudioType audioType : AudioType.values()) {
            if (audioType.getName().equals(name)) {
                return audioType.index;
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
