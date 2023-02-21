package com.rakbow.website.config;

import com.rakbow.website.task.EntityInfoTask;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-22 0:37
 * @Description:
 */
@Configuration
public class QuartzConfig {
    @Bean
    public JobDetail quartzDetail(){
        // withIdentity指定的是这个job的id
        return JobBuilder.newJob(EntityInfoTask.class).withIdentity("ENTITY_INFO_TASK_IDENTITY").storeDurably().build();
    }


    @Bean
    public Trigger quartzTrigger(){ //触发器
        SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule()
                // .withIntervalInSeconds(20)  //设置时间周期单位秒
               .withIntervalInHours(24)  //两个小时执行一次
                .repeatForever();
        return TriggerBuilder.newTrigger().forJob(quartzDetail())
//                .forJob(quartzDetail_2())
                .withIdentity("ENTITY_INFO_TRIGGER")
                .withSchedule(scheduleBuilder)
                .build();
    }

}

