package com.rakbow.website.entity.common;

import lombok.Data;

import java.sql.Timestamp;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-02 3:55
 * @Description:
 */
@Data
public class MetaEntity {

    private String images;//图片列表（JSON字符串）
    private String description;//描述
    private String remark;//备注
    private Timestamp addedTime;//数据新增时间
    private Timestamp editedTime;//数据更新时间
    private int status;//激活状态

}
