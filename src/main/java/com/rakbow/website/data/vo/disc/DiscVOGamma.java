package com.rakbow.website.data.vo.disc;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-03 16:13
 * @Description: VO 存储到搜索引擎数据库
 */
@Data
public class DiscVOGamma {

    //基础信息
    private int id;//主键
    private String catalogNo;//商品型番
    private String name;//商品名(原语言)
    private String nameZh;//商品译名(中)
    private String nameEn;//商品译名(英)
    private JSONObject region;//地区
    private String releaseDate;//发行日期
    private boolean limited;//发售版本是否为限定版 0-否 1-是
    private boolean hasBonus;//是否包含特典

    //关联信息
    private JSONArray franchises;//所属系列id
    private JSONArray products;//所属作品id

    private JSONArray mediaFormat;//媒体格式 0-未分类 1-DVD 2-Blu-ray

    private String cover;

}
