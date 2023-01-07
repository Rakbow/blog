package com.rakbow.website.data.common;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-07 18:26
 * @Description:
 */
public class SearchResult {

    public int total;//查询结果数
    public Object data;//查询结果数据

    public SearchResult (int total, Object data) {
        this.total = total;
        this.data = data;
    }

}
