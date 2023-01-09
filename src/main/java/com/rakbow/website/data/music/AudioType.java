package com.rakbow.website.data.music;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 20:31
 * @Description:
 */
@AllArgsConstructor
public enum AudioType {
    UNCLASSIFIED(0,"未分类","Unclassified"),
    VOCAL(1, "歌曲","Vocal"),
    INSTRUMENTAL(2, "歌曲(无伴奏)","Instrumental"),
    ORIGINAL_SOUNDTRACK(3, "原声","Origin sound track"),
    DRAMA(4, "广播剧","Drama");

    @Getter
    private final int index;
    @Getter
    private final String name;
    @Getter
    private final String nameEn;

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
}
