package com.rakbow.website.data.vo.merch;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 16:13
 * @Description: VO 信息量最少
 */
@Data
public class MerchVOBeta {

    //基础信息
    private int id;//主键编号
    private String name;//商品名（原文）
    private String nameZh;//商品名（中文）
    private String nameEn;//商品名（英文）
    private String barcode;//商品条形码
    private String releaseDate;//发售日
    private boolean notForSale;//是否非卖品

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品

    //复杂字段
    private JSONObject category;//商品分类

    //图片
    private JSONObject cover;

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间

}
