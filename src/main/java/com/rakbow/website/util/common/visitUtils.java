package com.rakbow.website.util.common;

import com.rakbow.website.data.emun.common.EntityType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-17 2:11
 * @Description:
 */
@Component
public class visitUtils {

    @Autowired
    private RedisUtil redisUtil;

    public void addVisit(EntityType entityType, int entityId) {
        String key = "visit" + "-" + entityType.getNameEn() + "-" + entityId;
        redisUtil.saveDatabase(2, key, 0);
    }

}
