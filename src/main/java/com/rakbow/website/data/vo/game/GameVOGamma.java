package com.rakbow.website.data.vo.game;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.Attribute;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-03 16:13
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class GameVOGamma {

    //基础信息
    private int id;//主键编号
    private String name;//游戏名（原文）
    private String nameZh;//游戏名（中文）
    private String nameEn;//游戏名（英文）
    private String releaseDate;//发售日期
    private boolean hasBonus;//是否包含特典

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品

    private Attribute platform;//平台
    private JSONObject region;//地区

    private String cover;

    private long visitCount;//浏览数
    private long likeCount;//点赞数

}
