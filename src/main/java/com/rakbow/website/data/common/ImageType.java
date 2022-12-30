package com.rakbow.website.data.common;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-18 23:01
 * @Description:
 */
public enum ImageType {
    DISPLAY(0,"展示","Display"),
    COVER(1, "封面","Cover"),
    OTHER(2, "其他","Other");

    private int index;
    private String name;
    private String nameEn;

    ImageType(int index, String name, String nameEn) {
        this.index = index;
        this.name = name;
        this.nameEn = nameEn;
    }

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
