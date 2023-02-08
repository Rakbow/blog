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
    private String remark;//备注

    private boolean metaLabel;//是否为meta-franchise
    private JSONArray childFranchiseIds;//子系列

}
