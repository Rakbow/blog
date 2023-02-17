package com.rakbow.website.util.common;

import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.pageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-17 2:11
 * @Description:
 */
@Component
public class VisitUtils {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * 新增浏览数存入redis缓存
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void addVisit(int entityType, int entityId) {
        String key = "VISIT_" + entityType + "_" + entityId;
        redisUtil.set(key, 0);
    }

    /**
     * 获取浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     * @return visit
     */
    public long getVisit(int entityType, int entityId) {
        String key = "VISIT_" + entityType + "_" + entityId;
        return (long) redisUtil.get(key);
    }

    /**
     * 删除浏览数
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public void deleteVisit(int entityType, int entityId) {
        String key = "VISIT_" + entityType + "_" + entityId;
        redisUtil.delete(key);
    }

    /**
     * 浏览数自增,并返回自增值
     * @param entityType,entityId 实体类型，实体id
     * @Author Rakbow
     */
    public long incVisit(int entityType, int entityId) {
        String key = "VISIT_" + entityType + "_" + entityId;
        return redisUtil.increment(key, 1);
    }

    /**
     * 获取浏览数排名,并返回自增值
     * @param entityType 实体类型
     * @Author Rakbow
     */
    public void getVisitRanking(int entityType) {

    }


}
