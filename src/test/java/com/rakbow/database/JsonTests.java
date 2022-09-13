package com.rakbow.database;

import com.rakbow.database.service.AlbumService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-09-03 19:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = DatabaseApplication.class)
public class JsonTests {

    @Autowired
    private AlbumService albumService;




}
