package com.rakbow.website.task;

import com.rakbow.website.dao.StatisticMapper;
import com.rakbow.website.data.common.Like;
import com.rakbow.website.data.common.Visit;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.StatisticPOMapper;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-23 10:36
 * @Description: 每1小时更新一次，将浏览数排名前十更新
 */
@Component
public class VisitRankTask extends QuartzJobBean {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private VisitUtil visitUtil;

    private final StatisticPOMapper statisticPOMapper = StatisticPOMapper.INSTANCES;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        // 定时任务逻辑
        // TODO
        System.out.println("------redis缓存数据获取中------");
        //获取所有浏览数据
        List<String> visitKeys = redisUtil.keys(VisitUtil.PREFIX_VISIT);
        System.out.println("------redis缓存数据获取完毕------");

        System.out.println("------缓存数据转换中------");
        List<Visit> visits = statisticPOMapper.keys2Visit(visitKeys);
        System.out.println("------缓存数据转换完毕------");

        System.out.println("------正在更新浏览排名------");
        //清空浏览数据
        visitUtil.clearAllVisitRank();
        //更新数据
//        Map<Integer, Long> album_visits = visits.stream().filter(v->v.getEntityType() == EntityType.ALBUM.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> book_visits = visits.stream().filter(v->v.getEntityType() == EntityType.BOOK.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> disc_visits = visits.stream().filter(v->v.getEntityType() == EntityType.DISC.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> game_visits = visits.stream().filter(v->v.getEntityType() == EntityType.GAME.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> merch_visits = visits.stream().filter(v->v.getEntityType() == EntityType.MERCH.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> music_visits = visits.stream().filter(v->v.getEntityType() == EntityType.MUSIC.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> product_visits = visits.stream().filter(v->v.getEntityType() == EntityType.PRODUCT.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
//        Map<Integer, Long> franchise_visits = visits.stream().filter(v->v.getEntityType() == EntityType.FRANCHISE.getId()).collect(Collectors.toMap(Visit::getEntityId, Visit::getVisitCount));
        visits.forEach(visit -> {
            visitUtil.setEntityVisitRanking(visit.getEntityType(), visit.getEntityId(), visit.getVisitCount());
        });
        System.out.println("------浏览排名更新完毕------");
    }

}