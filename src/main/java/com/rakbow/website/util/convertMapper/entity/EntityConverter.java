package com.rakbow.website.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.SpringUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.entity.FranchiseUtil;
import com.rakbow.website.util.entity.ProductUtil;
import com.rakbow.website.util.entry.EntryUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.mapstruct.Named;

import java.sql.Timestamp;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-08 9:42
 * @Description:
 */
public class EntityConverter {

    static String getDate(Date date) {
        return DateUtil.dateToString(date);
    }

    static JSONArray getProducts(String products) {
        return ProductUtil.getProductList(products);
    }

    static JSONArray getFranchises(String franchises) {
        return FranchiseUtil.getFranchiseList(franchises);
    }

    static JSONArray getMediaFormat(String formats) {
        return MediaFormat.getAttributes(formats);
    }

    static String getVOTime(Timestamp timestamp) {
        return DateUtil.timestampToString(timestamp);
    }

    static boolean getBool(int bool) {
        return bool == 1;
    }

    static String getThumb70Cover(String images) {
        return QiniuImageUtil.getThumb70Url(images);
    }

    static JSONArray getCompanies(String json) {
        return EntryUtil.getCompanies(json);
    }

    static JSONArray getPersonnel(String json) {
        return EntryUtil.getPersonnel(json);
    }

    static JSONArray getSpecs(String json) {
        return EntryUtil.getSpecs(json);
    }

    static JSONObject getCover(String images, EntityType entityType) {
        return CommonImageUtil.generateCover(images, entityType);
    }

    static JSONObject getThumbCover(String images, EntityType entityType, int size) {
        return CommonImageUtil.generateThumbCover(images, entityType, size);
    }

    static long getVisitCount(int entityTypeId, int id) {
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        return visitUtil.getVisit(entityTypeId, id);
    }

    static long getLikeCount(int entityTypeId, int id) {
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");
        return likeUtil.getLike(entityTypeId, id);
    }

    static String getCurrencyUnitByCode(String region) {
        return Region.getCurrencyUnitByCode(region);
    }

    static JSONObject getRegion(String region) {
        return Region.getRegion(region);
    }

    static JSONArray getJSONArray(String json) {
        return JSON.parseArray(json);
    }

}
