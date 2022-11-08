package com.rakbow.website.data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 4:02
 * @Description:
 */
public enum DataActionType {
    INSERT(0,"新增"),
    UPDATE(1,"更新"),
    REAL_DELETE(2,"真删"),
    FAKE_DELETE(3,"假删"),
    ALL_DELETE(4,"全删");

    public int id;
    public String name;

    DataActionType(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
