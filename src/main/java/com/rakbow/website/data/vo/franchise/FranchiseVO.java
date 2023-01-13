package com.rakbow.website.data.vo.franchise;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class FranchiseVO {

    //基础信息
    private int id;//主键
    private String name;//系列名
    private String nameZh;//系列名（中文）
    private String nameEn;//系列名（英语）
    private String originDate;//发行日期
    private String description;//描述
    private String remark;//备注

    //图片
    private JSONArray images;//图片
    private JSONObject cover;//封面/logo
    private JSONArray displayImages;//展示图片
    private JSONArray otherImages;//其他图片

    //审计字段
    private String addedTime;//创建时间
    private String editedTime;//更新时间
    private int _s;//状态

}
