package com.rakbow.website.data.album;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 22:34
 * @Description: 出版形式
 */
public enum PublishFormat {
    UNCLASSIFIED(0, "未分类", "Unclassified"),
    COMMERCIAL(1, "商业发行", "Commercial"),
    INDIE_DOUJIN(2,"独立同人", "Doujin"),
    BONUS(3,"同捆特典", "Bonus"),
    EVENT_ONLY(4,"展会、活动限定", "Event Only"),
    PREORDER(5,"预约特典", "Preorder");

    private int index;
    private String name;
    private String nameEn;

    PublishFormat(int index, String name, String nameEn) {
        this.index = index;
        this.name = name;
        this.nameEn = nameEn;
    }

    public static String getNameByIndex (int index){
        for (PublishFormat publishFormat : PublishFormat.values()) {
            if (publishFormat.getIndex() == index) {
                return publishFormat.getName();
            }
        }
        return "未分类";
    }

    public static int getIndexByName(String name){
        for (PublishFormat publishFormat : PublishFormat.values()) {
            if (publishFormat.getName().equals(name)) {
                return publishFormat.index;
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
