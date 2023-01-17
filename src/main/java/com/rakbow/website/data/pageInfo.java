package com.rakbow.website.data;

import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-17 23:22
 * @Description: 页面信息实体类
 */
@Data
public class pageInfo {

    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private long visitNum;//浏览数

    public pageInfo(String addedTime, String editedTime, long visitNum) {
        this.addedTime = addedTime;
        this.editedTime = editedTime;
        this.visitNum = visitNum;
    }

}
