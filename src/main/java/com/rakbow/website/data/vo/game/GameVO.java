package com.rakbow.website.data.vo.game;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-12 10:42
 * @Description: 转换量最大的VO，一般用于详情页面
 */
@Data
public class GameVO {

    //基础信息
    private int id;//主键编号
    private String name;//游戏名（原文）
    private String nameZh;//游戏名（中文）
    private String nameEn;//游戏名（英文）
    private String barcode;//商品条形码
    private String releaseDate;//发售日期
    private boolean hasBonus;//是否包含特典
    private String remark;//备注

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品

    //复杂字段
    private JSONObject releaseType;//发售类型
    private JSONObject platform;//平台
    private JSONObject region;//地区
    private JSONArray organizations;//相关组织
    private JSONArray staffs;//开发制作人员
    private String description;//描述
    private String bonus;//特典信息

    //图片相关
    private JSONArray images;//图片（json）
    private JSONObject cover;
    private JSONArray displayImages;
    private JSONArray otherImages;

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private int _s;//状态

}
