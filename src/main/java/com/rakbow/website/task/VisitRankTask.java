package com.rakbow.website.task;

import com.rakbow.website.dao.StatisticMapper;
import com.rakbow.website.data.common.Like;
import com.rakbow.website.data.common.Visit;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.service.EntityService;
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
    private EntityService entityService;

    private final StatisticPOMapper statisticPOMapper = StatisticPOMapper.INSTANCES;

    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) {
        // 定时任务逻辑
        // TODO
        entityService.refreshVisitData();
    }

}