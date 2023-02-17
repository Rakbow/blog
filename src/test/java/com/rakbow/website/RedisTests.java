package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.service.UserService;
import com.rakbow.website.service.VisitService;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.VisitUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class RedisTests {

    @Autowired
    private UserService userService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private VisitUtils visitUtils;
    @Autowired
    private RedisUtil redisUtil;

    @Test
    public void redisTest1() {
//        List<Visit> visits = visitService.getAll();
//        visits.forEach(visit -> {
//            visitUtils.addVisit(visit.getEntityType(), visit.getEntityId());
//        });
//        System.out.println(visitUtils.incVisit(1, 11));
        System.out.println(JSON.toJSON(redisUtil.searchTokenFirst("VISIT_1_")));
    }

}
