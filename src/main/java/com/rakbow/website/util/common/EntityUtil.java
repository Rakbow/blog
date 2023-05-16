package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ItemDetailInfo;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.system.SystemLanguage;
import com.rakbow.website.data.emun.system.UserAuthority;
import com.rakbow.website.util.entity.FranchiseUtil;
import com.rakbow.website.util.entity.ProductUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-06 16:38
 * @Description:
 */
@Component
public class EntityUtil {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
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
        String lang = LocaleContextHolder.getLocale().getLanguage();
        if(StringUtils.equals(lang, SystemLanguage.ENGLISH.getCode())) {
            if(entityType == EntityType.ENTRY.getId()) {
                options.put("entryCategorySet", redisUtil.get(RedisCacheConstant.ENTRY_CATEGORY_SET_EN));
            }
            if(entityType == EntityType.ALBUM.getId()) {
                options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET_EN));
                options.put("albumFormatSet", redisUtil.get(RedisCacheConstant.ALBUM_FORMAT_SET_EN));
                options.put("publishFormatSet", redisUtil.get(RedisCacheConstant.PUBLISH_FORMAT_SET_EN));
                options.put("companySet", redisUtil.get(RedisCacheConstant.COMPANY_SET_EN));
                options.put("companyRoleSet", redisUtil.get(RedisCacheConstant.COMPANY_ROLE_SET_EN));

                options.put("roleSet", redisUtil.get(RedisCacheConstant.ROLE_SET_EN));
                options.put("personnelSet", redisUtil.get(RedisCacheConstant.PERSONNEL_SET_EN));
            }
            if(entityType == EntityType.BOOK.getId()) {
                options.put("bookTypeSet", redisUtil.get(RedisCacheConstant.BOOK_TYPE_SET_EN));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
                options.put("languageSet", redisUtil.get(RedisCacheConstant.LANGUAGE_SET_EN));
                options.put("companySet", redisUtil.get(RedisCacheConstant.COMPANY_SET_EN));
                options.put("companyRoleSet", redisUtil.get(RedisCacheConstant.COMPANY_ROLE_SET_EN));

                options.put("roleSet", redisUtil.get(RedisCacheConstant.ROLE_SET_EN));
                options.put("personnelSet", redisUtil.get(RedisCacheConstant.PERSONNEL_SET_EN));
                options.put("specParameterSet", redisUtil.get(RedisCacheConstant.SPEC_PARAMETER_SET_EN));
                options.put("publicationSet", redisUtil.get(RedisCacheConstant.PUBLICATION_SET_EN));
            }
            if(entityType == EntityType.DISC.getId()) {
                options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET_EN));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
            }
            if(entityType == EntityType.GAME.getId()) {
                options.put("releaseTypeSet", redisUtil.get(RedisCacheConstant.RELEASE_TYPE_SET_EN));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
                options.put("gamePlatformSet", redisUtil.get(RedisCacheConstant.GAME_PLATFORM_SET_EN));
            }
            if(entityType == EntityType.MERCH.getId()) {
                options.put("merchCategorySet", redisUtil.get(RedisCacheConstant.MERCH_CATEGORY_SET_EN));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
            }
            if(entityType == EntityType.MUSIC.getId()) {
                options.put("audioTypeSet", redisUtil.get(RedisCacheConstant.AUDIO_TYPE_SET_EN));
            }
            if(entityType == EntityType.PRODUCT.getId()) {
                options.put("productCategorySet", redisUtil.get(RedisCacheConstant.PRODUCT_CATEGORY_SET_EN));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_EN));
                options.put("platformSet", redisUtil.get(RedisCacheConstant.PLATFORM_SET_EN));
            }
            options.put("franchiseSet", redisUtil.get(RedisCacheConstant.FRANCHISE_SET_EN));
        }else {
            if(entityType == EntityType.ENTRY.getId()) {
                options.put("entryCategorySet", redisUtil.get(RedisCacheConstant.ENTRY_CATEGORY_SET_ZH));
            }
            if(entityType == EntityType.ALBUM.getId()) {
                options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET_ZH));
                options.put("albumFormatSet", redisUtil.get(RedisCacheConstant.ALBUM_FORMAT_SET_ZH));
                options.put("publishFormatSet", redisUtil.get(RedisCacheConstant.PUBLISH_FORMAT_SET_ZH));
                options.put("companySet", redisUtil.get(RedisCacheConstant.COMPANY_SET_ZH));
                options.put("companyRoleSet", redisUtil.get(RedisCacheConstant.COMPANY_ROLE_SET_ZH));

                options.put("roleSet", redisUtil.get(RedisCacheConstant.ROLE_SET_ZH));
                options.put("personnelSet", redisUtil.get(RedisCacheConstant.PERSONNEL_SET_ZH));
            }
            if(entityType == EntityType.BOOK.getId()) {
                options.put("bookTypeSet", redisUtil.get(RedisCacheConstant.BOOK_TYPE_SET_ZH));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_ZH));
                options.put("languageSet", redisUtil.get(RedisCacheConstant.LANGUAGE_SET_ZH));

                options.put("companySet", redisUtil.get(RedisCacheConstant.COMPANY_SET_ZH));
                options.put("companyRoleSet", redisUtil.get(RedisCacheConstant.COMPANY_ROLE_SET_ZH));

                options.put("roleSet", redisUtil.get(RedisCacheConstant.ROLE_SET_ZH));
                options.put("personnelSet", redisUtil.get(RedisCacheConstant.PERSONNEL_SET_ZH));
                options.put("specParameterSet", redisUtil.get(RedisCacheConstant.SPEC_PARAMETER_SET_ZH));
                options.put("publicationSet", redisUtil.get(RedisCacheConstant.PUBLICATION_SET_ZH));
            }
            if(entityType == EntityType.DISC.getId()) {
                options.put("mediaFormatSet", redisUtil.get(RedisCacheConstant.MEDIA_FORMAT_SET_ZH));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_ZH));
            }
            if(entityType == EntityType.GAME.getId()) {
                options.put("releaseTypeSet", redisUtil.get(RedisCacheConstant.RELEASE_TYPE_SET_ZH));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_ZH));
                options.put("gamePlatformSet", redisUtil.get(RedisCacheConstant.GAME_PLATFORM_SET_ZH));
            }
            if(entityType == EntityType.MERCH.getId()) {
                options.put("merchCategorySet", redisUtil.get(RedisCacheConstant.MERCH_CATEGORY_SET_ZH));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_ZH));
            }
            if(entityType == EntityType.MUSIC.getId()) {
                options.put("audioTypeSet", redisUtil.get(RedisCacheConstant.AUDIO_TYPE_SET_ZH));
            }
            if(entityType == EntityType.PRODUCT.getId()) {
                options.put("productCategorySet", redisUtil.get(RedisCacheConstant.PRODUCT_CATEGORY_SET_ZH));
                options.put("regionSet", redisUtil.get(RedisCacheConstant.REGION_SET_ZH));
                options.put("platformSet", redisUtil.get(RedisCacheConstant.PLATFORM_SET_ZH));
            }
            options.put("franchiseSet", redisUtil.get(RedisCacheConstant.FRANCHISE_SET_ZH));
        }

        return options;
    }
}
