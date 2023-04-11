package com.rakbow.website;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.EntityStatistic;
import com.rakbow.website.service.*;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.RelatedInfoUtil;
import com.rakbow.website.util.common.VisitUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class RedisTests {

    @Resource
    private UserService userService;
    @Resource
    private EntityService entityService;
    @Resource
    private AlbumService albumService;
    @Resource
    private FranchiseService franchiseService;
    @Resource
    private ProductService productService;
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private DiscMapper discMapper;
    @Resource
    private GameMapper gameMapper;
    @Resource
    private MerchMapper merchMapper;
    @Resource
    private MusicMapper musicMapper;
    @Resource
    private ProductMapper productMapper;
    @Resource
    private StatisticMapper statisticMapper;
    @Resource
    private FranchiseMapper franchiseMapper;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RelatedInfoUtil relatedInfoUtil;

    @Test
    public void refreshData() {
        productService.refreshRedisProducts();
        franchiseService.refreshRedisFranchises();
//        entityService.refreshRedisEmunData();
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

    @Test
    public void refreshIndexCoverUrls() {

        entityService.refreshIndexCoverUrls();

    }

    @Test
    public void refreshRelatedInfos() {
//        List<Album> albums = albumMapper.getAll();
//        albums.forEach(album -> albumService.generateRelatedAlbumIds(album));
        // List<String> keys = redisUtil.keys("entity_related_item:*");
        // keys.forEach(key -> redisUtil.delete(key));

        long t1 = new Timestamp(System.currentTimeMillis()).getTime();



        long t2 = new Timestamp(System.currentTimeMillis()).getTime();

        System.out.println(t2 - t1);

    }

}
