package com.rakbow.website.data;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-06 16:22
 * @Description: 页面信息实体类通用属性
 */
@Data
public class ItemDetailInfo {

    private int id;//表主键
    private int entityType;//实体类型

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品id 在mysql中以数组字符串形式存储

    private String description;//描述信息

    //审计字段
    private int _s;//激活状态

}
