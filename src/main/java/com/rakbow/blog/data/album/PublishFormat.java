package com.rakbow.blog.data.album;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-19 22:34
 * @Description: 出版形式
 */
public enum PublishFormat {
    UNCLASSIFIED(0, "未分类"),
    COMMERCIAL(1, "商业发行"),
    INDIE_DOUJIN(2,"独立同人"),
    BONUS(3,"同捆特典（非卖品）"),
    EVENT_ONLY(4,"展会限定"),
    PREORDER(5,"预约特典");

    private int index;
    private String name;

    PublishFormat(int index, String name) {
        this.index = index;
        this.name = name;
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
}
