package com.rakbow.website.util.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-18 19:41
 * @Description:
 */
@Component
public class LikeUtil {

    @Autowired
    private RedisUtil redisUtil;

    public static final String SPLIT = ":";
    public static final String PREFIX_LIKE = "like";
    public static final String PREFIX_LIKE_COUNT = "like_count";

    public String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_LIKE_COUNT + SPLIT + entityType + SPLIT + entityId;
    }

    public String getEntityLikeTmpTokenKey(int entityType, int entityId, String likeToken) {
        if(likeToken != null) {
            return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId + SPLIT + likeToken;
        }else {
            return PREFIX_LIKE + SPLIT + entityType + SPLIT + entityId + SPLIT + CommonUtil.generateUUID();
        }

    }

}
