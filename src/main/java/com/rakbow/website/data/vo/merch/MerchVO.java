package com.rakbow.website.data.vo.merch;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class MerchVO {

    //基础信息
    private int id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String barcode;//商品条形码
    private String releaseDate;//发售日
    private int price;//价格
    private String currencyUnit;//价格单位
    private boolean notForSale;//是否非卖品
    private String remark;//备注

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品

    //复杂字段
    private JSONObject category;//商品分类
    private JSONObject region;//地区
    private JSONArray spec;//规格
    private String description;//描述

    //图片
    private JSONArray images;//图片（json）
    private JSONObject cover;
    private JSONArray displayImages;
    private JSONArray otherImages;

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private int _s;//状态

}
