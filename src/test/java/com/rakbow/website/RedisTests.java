package com.rakbow.website;

import com.rakbow.website.dao.*;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.entity.EntityStatistic;
import com.rakbow.website.service.EntityService;
import com.rakbow.website.service.FranchiseService;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.service.UserService;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.VisitUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class RedisTests {

    @Autowired
    private UserService userService;
    @Autowired
    private EntityService entityService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private DiscMapper discMapper;
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private MerchMapper merchMapper;
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StatisticMapper statisticMapper;
    @Autowired
    private FranchiseMapper franchiseMapper;
    @Autowired
    private VisitUtil visitUtil;
    @Autowired
    private LikeUtil likeUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void refreshData() {
        productService.refreshRedisProducts();
        franchiseService.refreshRedisFranchises();
        entityService.refreshRedisEmunData();
    }

    @Test
    public void redisTest1() {
        redisUtil.redisTemplate.opsForZSet().add(RedisCacheConstant.ALBUM_VISIT_RANKING, 1, 0);
    }

    @Test
    public void deleteAllCache() {
        List<String> keys = redisUtil.keys("VISIT_*");
        keys.forEach(key -> {
            redisUtil.delete(key);
        });
    }

    @Test
    public void redisTest3() {

        List<String> keys = redisUtil.keys("visit:");
        keys.addAll(redisUtil.keys("like:"));
        keys.forEach(key-> {
            redisUtil.delete(key);
        });

        List<EntityStatistic> statistics = statisticMapper.getAll();

        statistics.forEach(s-> {
            redisUtil.set(visitUtil.getSingleVisitKey(s.getEntityType(), s.getEntityId()), (int)s.getVisitCount());
            redisUtil.set(likeUtil.getEntityLikeKey(s.getEntityType(), s.getEntityId()), (int)s.getLikeCount());
        });

    }

}
