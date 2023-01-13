package com.rakbow.website.data.vo.album;

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
public class AlbumVO {

    //基础
    private int id;//表主键
    private String catalogNo;//专辑编号
    private String name;//专辑名称（日语）
    private String nameZh;//专辑名称（中文）
    private String nameEn;//专辑名称（英语）
    private String barcode;//商品条形码
    private String releaseDate;//发行日期
    private int price;//发行价格（含税）
    private boolean hasBonus;//是否包含特典内容
    private String remark;//备注

    //厂商信息
    private String label;//唱片公司
    private String publisher;//发行商
    private String distributor;//经销商
    private String copyright;//版权方

    //关联信息
    private JSONArray franchises;//所属系列
    private JSONArray products;//所属产品id 在mysql中以数组字符串形式存储

    //规格信息
    private JSONArray publishFormat;//出版形式 在mysql中以数组字符串形式存储
    private JSONArray albumFormat;//专辑分类 在mysql中以数组字符串形式存储
    private JSONArray mediaFormat;//媒体类型

    //大文本字段
    private String description;//描述
    private JSONArray artists;//staff
    private String bonus;//特典信息

    //图片相关
    private JSONObject cover;
    private JSONArray images;//图片列表（JSON字符串）
    private JSONArray displayImages;
    private JSONArray otherImages;

    //音轨相关
    private JSONArray editDiscList;
    private JSONObject trackInfo;

    //审计字段
    private String addedTime;//数据新增时间
    private String editedTime;//数据更新时间
    private int _s;//激活状态

}
