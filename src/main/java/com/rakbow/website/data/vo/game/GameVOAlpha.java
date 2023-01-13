package com.rakbow.website.data.vo.game;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 10:42
 * @Description: 转换量较少的VO，一般用于list index页面
 */
@Data
public class GameVOAlpha {

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

    //图片相关
    private JSONObject cover;

    //审计字段
    private String addedTime;//收录时间
    private String editedTime;//编辑时间
    private int _s;//状态

    //其他字段
    private long visitNum;//浏览数

}
