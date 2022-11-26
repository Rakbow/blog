package com.rakbow.website.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-13 22:16
 * @Description:
 */
public enum EntityType {

    UNCLASSIFIED(0,"未分类", "Unclassified"),
    ALBUM(1,"专辑", "Album"),
    DISC(2,"碟片", "Disc"),
    BOOK(3,"书籍", "Book"),
    GOODS(4,"周边", "Goods"),
    GAME(5,"游戏", "Game"),
    SERIES(6,"系列", "Series"),
    PRODUCT(7,"作品", "Product"),
    ARTICLE(8,"文章", "Article"),
    MUSIC(9,"音乐", "Music");

    private int id;
    private String nameZh;
    private String nameEn;

    EntityType(int id, String nameZh, String nameEn) {
        this.id = id;
        this.nameZh = nameZh;
        this.nameEn = nameEn;
    }

    public static String getItemNameZhByIndex(int id){
        for (EntityType itemType : EntityType.values()) {
            if (itemType.getId() == id) {
                return itemType.nameZh;
            }
        }
        return "";
    }

    public static int getItemIndexByNameZh(String nameZh){
        for (EntityType itemType : EntityType.values()) {
            if (itemType.getNameZh().equals(nameZh)) {
                return itemType.id;
            }
        }
        return 0;
    }

    public static List<String> getItemList(){
        List<String> items = new ArrayList<>();
        for (EntityType itemType : EntityType.values()) {
            items.add(itemType.getNameZh());
        }
        return items;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
