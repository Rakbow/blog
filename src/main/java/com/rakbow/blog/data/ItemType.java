package com.rakbow.blog.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-13 22:16
 * @Description:
 */
public enum ItemType {

    UNCLASSIFIED(0,"未分类"),
    ALBUM(1,"音乐专辑"),
    ANIME_DVD(2,"动画碟片DVD"),
    ANIME_BD(3,"动画碟片BD");

    private int Id;
    private String Name;

    ItemType(int id, String name) {
        this.Id = id;
        this.Name = name;
    }

    public static String getItemNameByIndex(int id){
        for (ItemType itemType : ItemType.values()) {
            if (itemType.getId() == id) {
                return itemType.Name;
            }
        }
        return "";
    }

    public static int getItemIndexByName(String name){
        for (ItemType itemType : ItemType.values()) {
            if (itemType.getName().equals(itemType)) {
                return itemType.Id;
            }
        }
        return 0;
    }

    public static List<String> getItemList(){
        List<String> items = new ArrayList<>();
        for (ItemType itemType : ItemType.values()) {
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
