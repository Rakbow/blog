package com.rakbow.blog.data.album;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-19 22:57
 * @Description: 专辑分类
 */
public enum AlbumFormat {
    UNCLASSIFIED(0,"未分类"),
    THEME(1, "主题曲"),
    ORIGINAL_SOUNDTRACK(2, "原声"),
    CHARACTER_SONG(3, "角色曲"),
    DRAMA(4, "广播剧"),
    DOUJIN_MUSIC(5,"同人歌曲"),
    VIDEO(6,"影像");

    private int index;
    private String name;

    AlbumFormat(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public static String getNameByIndex (int index){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getIndex() == index) {
                return albumFormat.getName();
            }
        }
        return "未分类";
    }

    public static int getIndexByName(String name){
        for (AlbumFormat albumFormat : AlbumFormat.values()) {
            if (albumFormat.getName().equals(name)) {
                return albumFormat.index;
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
