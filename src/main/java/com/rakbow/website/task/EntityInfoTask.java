package com.rakbow.website.task;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-22 0:36
 * @Description:
 */
public class EntityInfoTask extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
        // 定时任务逻辑
        // TODO
    }

}
