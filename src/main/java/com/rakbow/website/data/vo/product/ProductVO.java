package com.rakbow.website.data.vo.product;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-13 9:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class ProductVO {

    //基础信息
    private int id;//主键
    private String name;//原名
    private String nameZh;//中文译名
    private String nameEn;//英文译名
    private String releaseDate;//发售日期
    private JSONObject category;//作品分类
    private String remark;//备注

    //关联信息
    private JSONObject franchise;//所属系列id

    //图片
    private JSONArray images;
    private JSONObject cover;
    private JSONArray displayImages;
    private JSONArray otherImages;

    //其他
    private JSONArray staffs;//staff
    private String description;//描述

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private int _s;//状态

}
