package com.rakbow.website.data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:54
 * @Description:
 */
public enum ProductClass {
    UNCLASSIFIED(0,"未分类", "unclassified"),
    GAME(1, "游戏", "Game"),
    ANIMATION(2, "TV动画", "Animation"),
    LIVE_ACTION_MOVIE(3, "真人电影", "Movie");

    private int index;
    private String nameZh;
    private String nameEn;

    ProductClass(int index, String nameZh, String nameEn) {
        this.index = index;
        this.nameZh = nameZh;
        this.nameEn = nameEn;
    }

    public static String getNameZhByIndex (int index){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getIndex() == index) {
                return productClass.getNameZh();
            }
        }
        return "未分类";
    }

    public static String getNameEnByIndex (int index){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getIndex() == index) {
                return productClass.getNameEn();
            }
        }
        return "unclassified";
    }

    public static int getIndexByNameZh(String nameZh){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getNameZh().equals(nameZh)) {
                return productClass.index;
            }
        }
        return 0;
    }

    public static int getIndexByNameEn(String nameEn){
        for (ProductClass productClass : ProductClass.values()) {
            if (productClass.getNameEn().equals(nameEn)) {
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

    public String getNameZh() {
        return nameZh;
    }

    public void setNameZh(String nameZh) {
        this.nameZh = nameZh;
    }

    public String getNameEn() {
        return nameEn;
    }

    public void setNameEn(String nameEn) {
        this.nameEn = nameEn;
    }
}
