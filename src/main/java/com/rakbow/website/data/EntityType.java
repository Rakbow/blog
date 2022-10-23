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

    UNCLASSIFIED(0,"未分类"),
    ALBUM(1,"专辑"),
    DISC(2,"碟片"),
    BOOK(3,"书籍"),
    GOODS(4,"周边"),
    GAME(5,"游戏"),
    SERIES(6,"系列"),
    PRODUCT(7,"作品"),
    article(8,"文章");

    private int Id;
    private String Name;

    EntityType(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public static String getItemNameByIndex(int id){
        for (EntityType itemType : EntityType.values()) {
            if (itemType.getId() == id) {
                return itemType.Name;
            }
        }
        return "";
    }

    public static int getItemIndexByName(String name){
        for (EntityType itemType : EntityType.values()) {
            if (itemType.getName().equals(itemType)) {
                return itemType.Id;
            }
        }
        return 0;
    }

    public static List<String> getItemList(){
        List<String> items = new ArrayList<>();
        for (EntityType itemType : EntityType.values()) {
            items.add(itemType.getName());
        }
        return items;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }
}
