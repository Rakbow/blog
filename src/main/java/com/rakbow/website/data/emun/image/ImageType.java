package com.rakbow.website.data.emun.image;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-18 23:01
 * @Description:
 */
@AllArgsConstructor
public enum ImageType {
    DISPLAY(0,"展示","Display"),
    COVER(1, "封面","Cover"),
    OTHER(2, "其他","Other");

    @Getter
    private final int index;
    @Getter
    private final String name;
    @Getter
    private final String nameEn;

    public static String getNameByIndex (int index){
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getIndex() == index) {
                return imageType.getName();
            }
        }
        return "";
    }

    public static String getNameEnByIndex (int index){
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getIndex() == index) {
                return imageType.getNameEn();
            }
        }
        return "";
    }

    public static int getIndexByName(String name){
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getName().equals(name)) {
                return imageType.index;
            }
        }
        return 404;
    }

    public static int getIndexByNameEn(String nameEn){
        for (ImageType imageType : ImageType.values()) {
            if (imageType.getNameEn().equals(nameEn)) {
                return imageType.index;
            }
        }
        return 404;
    }

}
