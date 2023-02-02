package com.rakbow.website;

import com.rakbow.website.util.file.QiniuImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-01 23:56
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class otherTests {

    @Test
    public void tmpTest() {
        String url = "https://img.rakbow.com/album/6/f57fa8a51b594e30.jpg";
        System.out.println(QiniuImageUtils.getImageKeyByFullUrl(url));
    }

}
