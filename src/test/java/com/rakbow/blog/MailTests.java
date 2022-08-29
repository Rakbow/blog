package com.rakbow.blog;

import com.rakbow.blog.util.MailClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-02 1:47
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Test
    public void testTextMail(){
        mailClient.sendMail("rakbow@qq.com","test","hello!");
    }

}
