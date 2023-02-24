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

    public static final String SPLIT = ":";
    public static final String PREFIX_VISIT_TOKEN = "visit_token";
    public static final String PREFIX_VISIT = "visit";

    /**
     * 新增浏览数存入redis缓存
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void addSingleVisit(int entityType, int entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.set(key, 1);
    }

    /**
     * 获取浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public long getSingleVisit(int entityType, int entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        if(!redisUtil.hasKey(key)) {
            addSingleVisit(entityType, entityId);
        }
        return Long.parseLong(redisUtil.get(key).toString());
    }

    /**
     * 自增并返回浏览数
     * @param entityType,entityId 实体类型,实体id
     * @Author Rakbow
     */
    public long incSingleVisit(int entityType, int entityId, String visitToken) {
        String key = getSingleVisitKey(entityType, entityId);
        String tokenKey = getEntityVisitTokenKey(entityType, entityId, visitToken);
        if(redisUtil.hasKey(tokenKey)) {
            return getSingleVisit(entityType, entityId);
        }else {
            redisUtil.set(tokenKey, 1);
            redisUtil.expire(tokenKey, 3600*24);
            return redisUtil.increment(key, 1);
        }
    }

    /**
     * 删除浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void deleteSingleVisit(int entityType, int entityId) {
        String key = getSingleVisitKey(entityType, entityId);
        redisUtil.delete(key);
    }

    /**
     * 获取实体访问token key,用于判断是否第一次访问
     * */
    public String getEntityVisitTokenKey(int entityType, int entityId, String visitToken) {
        return PREFIX_VISIT_TOKEN + SPLIT + entityType + SPLIT + entityId + SPLIT + visitToken;
    }

    /**
     * 获取实体浏览数key,用于记录浏览数
     * */
    public String getSingleVisitKey(int entityType, int entityId) {
        return PREFIX_VISIT + SPLIT + entityType + SPLIT + entityId;
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
                res.put(Integer.parseInt(key.toString()), Math.round(redisUtil.redisTemplate.opsForZSet().score(rankKey, key)));
            }
        }
        return res;
    }

    /**
     * 清空所有浏览数据
     * @Author Rakbow
     */
    public void clearAllVisitRank() {

        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.ALBUM_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.BOOK_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.DISC_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.GAME_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.MERCH_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.MUSIC_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.PRODUCT_VISIT_RANKING, 0, -1);
        redisUtil.redisTemplate.opsForZSet().removeRange(RedisCacheConstant.FRANCHISE_VISIT_RANKING, 0, -1);

    }

    /**
     * 更新浏览数排名的set
     * @param entityType,entityId,visitCount 实体类型,实体id,浏览数
     * @Author Rakbow
     */
    public void setEntityVisitRanking(int entityType, int entityId, long visitCount) {
        String key = String.valueOf(entityId);
        String rankingKey = getEntityVisitRankingKeyName(entityType);
        redisUtil.redisTemplate.opsForZSet().add(rankingKey, key, visitCount);
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



}
