package com.rakbow.website.data.book;

import javax.print.DocFlavor;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-29 21:29
 * @Description:
 */
public enum BookType {

    UNCLASSIFIED(0,"未分类", "Unclassified"),
    NOVEL(1,"小说", "Novel"),
    comic(2,"漫画", "comic"),
    ANTHOLOGY(3,"作品集", "Anthology"),
    ART_BOOK(4,"原画集/设定集", "Art Book"),
    ELECTRONIC_BOOK(5,"电子书", "e-book"),
    OTHER(6,"其他", "Other");

    private int index;
    private String nameZh;
    private String nameEn;

    BookType (int index, String nameZh, String nameEn) {
        this.index = index;
        this.nameZh = nameZh;
        this.nameEn = nameEn;
    }

    public static String index2NameZh (int index) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (BookType bookType : BookType.values()) {
            if (bookType.index == index) {
                nameZh = bookType.nameZh;
            }
        }
        return nameZh;
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
