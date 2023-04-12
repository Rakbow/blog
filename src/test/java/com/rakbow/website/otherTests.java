package com.rakbow.website;

import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.entity.MusicUtil;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

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

    @Resource
    private AlbumService albumService;
    @Resource
    private MusicService musicService;

    @Test
    public void tmpTest() {
        String url = "https://img.rakbow.com/album/6/f57fa8a51b594e30.jpg";
        System.out.println(QiniuImageUtil.getImageKeyByFullUrl(url));
    }

    @Test
    public void tmpTest1() {
        Music music = musicService.getMusic(93);

        System.out.println(MusicUtil.getArtists(music));
    }

}
