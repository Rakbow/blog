package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ItemDetailInfo;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.pageInfo;
import com.rakbow.website.util.entity.FranchiseUtil;
import com.rakbow.website.util.entity.ProductUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-06 16:38
 * @Description:
 */
@Component
public class EntityUtils {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private VisitUtil visitUtil;
    @Autowired
    private LikeUtil likeUtil;

    public ItemDetailInfo getItemDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));
        detailInfo.setProducts(ProductUtil.getProductList(json.getString("products")));
        detailInfo.setFranchises(FranchiseUtil.getFranchiseList(json.getString("franchises")));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

    public static ItemDetailInfo getMetaDetailInfo(Object o, int entityType) {
        ItemDetailInfo detailInfo = new ItemDetailInfo();
        if(o == null) {
            return null;
        }
        JSONObject json = JSON.parseObject(JSON.toJSONString(o));
        detailInfo.setId(json.getInteger("id"));

        detailInfo.setDescription(json.getString("description"));

        detailInfo.setEntityType(entityType);
        detailInfo.setStatus(json.getIntValue("status") == 1);

        return detailInfo;
    }

    /**
     * 获取页面选项数据
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @Author Rakbow
     */
    public JSONObject getDetailOptions(int entityType) {
        JSONObject options = new JSONObject();
        if(entityType == EntityType.ALBUM.getId()) {
            options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET));
            options.put("albumFormatSet", redisUtil.get(RedisCacheConstant.ALBUM_FORMAT_SET));
            options.put("publishFormatSet", redisUtil.get(RedisCacheConstant.PUBLISH_FORMAT_SET));
        }
        if(entityType == EntityType.BOOK.getId()) {
            options.put("bookTypeSet", redisUtil.get(RedisCacheConstant.BOOK_TYPE_SET));
            options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET));
            options.put("languageSet", redisUtil.get(RedisCacheConstant.LANGUAGE_SET));
        }
        if(entityType == EntityType.DISC.getId()) {
            options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET));
            options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET));
        }
        if(entityType == EntityType.GAME.getId()) {
            options.put("releaseTypeSet", redisUtil.get(RedisCacheConstant.RELEASE_TYPE_SET));
            options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET));
            options.put("gamePlatformSet", redisUtil.get(RedisCacheConstant.GAME_PLATFORM_SET));
        }
        if(entityType == EntityType.MERCH.getId()) {
            options.put("merchCategorySet", redisUtil.get(RedisCacheConstant.MERCH_CATEGORY_SET));
            options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET));
        }
        if(entityType == EntityType.MUSIC.getId()) {
            options.put("audioTypeSet", redisUtil.get(RedisCacheConstant.AUDIO_TYPE_SET));
        }
        if(entityType == EntityType.PRODUCT.getId()) {
            options.put("productCategorySet", redisUtil.get(RedisCacheConstant.PRODUCT_CATEGORY_SET));
            options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET));
            options.put("platformSet", redisUtil.get(RedisCacheConstant.PLATFORM_SET));
        }
        options.put("franchiseSet", redisUtil.get(RedisCacheConstant.FRANCHISE_SET));
        return options;
    }
}
