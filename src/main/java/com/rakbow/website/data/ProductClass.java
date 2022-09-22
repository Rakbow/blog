package com.rakbow.website.data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:54
 * @Description:
 */
public enum ProductClass {
    UNCLASSIFIED(0,"未分类"),
    GAME(1, "游戏"),
    ANIMATION(2, "TV动画"),
    LIVE_ACTION_MOVIE(3, "真人电影");

    private int index;
    private String name;

    ProductClass(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static String getNameByIndex (int index){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getIndex() == index) {
                return productClass.getName();
            }
        }
        return "未分类";
    }

    public static int getIndexByName(String name){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getName().equals(name)) {
                return productClass.index;
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
