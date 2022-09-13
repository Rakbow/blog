package com.rakbow.database;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rakbow.database.service.AlbumService;
import com.rakbow.database.service.util.common.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-09-01 1:38
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DatabaseApplication.class)
public class serviceTests {
    @Autowired
    private AlbumService albumService;

    @Test
    public void jsonTest(){
        String json = "{\"id\":122}";
        JSONObject param = JSON.parseObject(json);
        System.out.println(CommonUtil.getAllContentFromJson(param));;
    }

}
