package com.rakbow.database.entity;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-07 21:34
 * @Description: 标签
 */
public class Tag {

    private int id;//标签id
    private String name;//标签名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
