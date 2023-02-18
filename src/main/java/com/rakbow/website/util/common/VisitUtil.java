package com.rakbow.website.util.common;

import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-17 2:11
 * @Description:
 */
@Component
public class VisitUtil {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 获取浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public long getVisit(int entityType, int entityId) {
        String rankKey = getEntityVisitRankingKeyName(entityType);
        String key = String.valueOf(entityId);
        return Math.round(redisUtil.redisTemplate.opsForZSet().score(rankKey, key));
    }

    /**
     * 获取浏览数通过visit key name
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public long getVisitByKey(int entityType, String key) {
        String rankKey = getEntityVisitRankingKeyName(entityType);
        return Math.round(redisUtil.redisTemplate.opsForZSet().score(rankKey, key));
    }

    /**
     * 自增并返回浏览数
     * @param entityType,entityId 实体类型,实体id
     * @Author Rakbow
     */
    public long getIncVisit(int entityType, int entityId) {
        String rankKey = getEntityVisitRankingKeyName(entityType);
        String key = String.valueOf(entityId);
        return Math.round(redisUtil.redisTemplate.opsForZSet().incrementScore(rankKey, key, 1));
    }

    /**
     * 新增浏览数存入redis缓存
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void addVisit(int entityType, int entityId) {
        setEntityVisitRanking(entityType, entityId, 0);
    }

    /**
     * 删除浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void deleteVisit(int entityType, int entityId) {
        String rankKey = getEntityVisitRankingKeyName(entityType);
        String key = String.valueOf(entityId);
        redisUtil.redisTemplate.opsForZSet().remove(rankKey, key);
    }

    /**
     * 获取浏览数排名,并返回指定数量的排名数据
     * @param entityType 实体类型
     * @Author Rakbow
     */
    public SortedMap<Integer, Long> getEntityVisitRanking(int entityType, int limit) {
        SortedMap<Integer, Long> res = new TreeMap<>();
        //rankKey
        String rankKey = getEntityVisitRankingKeyName(entityType);
        if(redisUtil.hasKey(rankKey)) {
            //0,-1的参数代表查询该key下的所有value
            Set<Object> keys = redisUtil.redisTemplate.opsForZSet().reverseRange(rankKey, 0, limit);
            for (Object key : keys) {
                res.put(Integer.parseInt(key.toString()), getVisitByKey(entityType, key.toString()));
            }
        }
        return res;
    }

    /**
     * 更新浏览数排名的set
     * @param entityType,entityId,visitNum 实体类型,实体id,浏览数
     * @Author Rakbow
     */
    public void setEntityVisitRanking(int entityType, int entityId, long visitNum) {
        String key = String.valueOf(entityId);
        String rankingKey = getEntityVisitRankingKeyName(entityType);
        redisUtil.redisTemplate.opsForZSet().add(rankingKey, key, visitNum);
    }

    public String getEntityVisitRankingKeyName(int entityType) {
        if(entityType == EntityType.ALBUM.getId()) {
            return RedisCacheConstant.ALBUM_VISIT_RANKING;
        }
        if(entityType == EntityType.BOOK.getId()) {
            return RedisCacheConstant.BOOK_VISIT_RANKING;
        }
        if(entityType == EntityType.DISC.getId()) {
            return RedisCacheConstant.DISC_VISIT_RANKING;
        }
        if(entityType == EntityType.GAME.getId()) {
            return RedisCacheConstant.GAME_VISIT_RANKING;
        }
        if(entityType == EntityType.MERCH.getId()) {
            return RedisCacheConstant.MERCH_VISIT_RANKING;
        }
        if(entityType == EntityType.MUSIC.getId()) {
            return RedisCacheConstant.MUSIC_VISIT_RANKING;
        }
        if(entityType == EntityType.PRODUCT.getId()) {
            return RedisCacheConstant.PRODUCT_VISIT_RANKING;
        }
        if(entityType == EntityType.FRANCHISE.getId()) {
            return RedisCacheConstant.FRANCHISE_VISIT_RANKING;
        }
        return "";
    }

    public String getEntityVisitKey(int entityType, int entityId) {
        return "VISIT_" + entityType + "_" + entityId;
    }

}
