package com.rakbow.blog.entity;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-13 23:09
 * @Description:
 */
public class ItemInTag {

    private int ItemTypeId;
    private int ItemId;
    private int TagId;

    public int getItemTypeId() {
        return ItemTypeId;
    }

    public void setItemTypeId(int itemTypeId) {
        ItemTypeId = itemTypeId;
    }

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getTagId() {
        return TagId;
    }

    public void setTagId(int tagId) {
        TagId = tagId;
    }

    @Override
    public String toString() {
        return "ItemInTag{" +
                "ItemTypeId=" + ItemTypeId +
                ", ItemId=" + ItemId +
                ", TagId=" + TagId +
                '}';
    }
}
