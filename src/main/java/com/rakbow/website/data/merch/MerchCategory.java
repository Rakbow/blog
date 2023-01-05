package com.rakbow.website.data.merch;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 14:29
 * @Description:
 */
public enum MerchCategory {

    UNCLASSIFIED(0,"未分类", "Unclassified"),
    BADGE(1,"徽章", "Badge"),
    ILLUSTRATE(2,"色纸", "Illustration"),
    POSTERS(3,"海报", "Posters"),
    TOY(4,"小玩具", "Toy"),
    FIGURE(5,"手办", "Figure"),
    OTHER(6,"其他", "Other");

    private int index;
    private String nameZh;
    private String nameEn;

    MerchCategory (int index, String nameZh, String nameEn) {
        this.index = index;
        this.nameZh = nameZh;
        this.nameEn = nameEn;
    }

    public static String index2NameZh (int index) {
        String nameZh = UNCLASSIFIED.nameZh;
        for (MerchCategory merchCategory : MerchCategory.values()) {
            if (merchCategory.index == index) {
                nameZh = merchCategory.nameZh;
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
