package com.rakbow.blog;

import com.rakbow.blog.data.album.PublishFormat;
import com.rakbow.blog.entity.Album;
import com.rakbow.blog.service.AlbumService;
import com.rakbow.blog.util.AlbumUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-20 0:15
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogApplication.class)
public class AlbumUtilTests {

    @Autowired
    private AlbumService albumService;

    @Test
    public void testGetAlbumFormatSet(){
        System.out.println(AlbumUtil.getMediaFormatSet());
    }

    @Test
    public void test1(){
        //System.out.println(albumService.album2Json(albumService.findAlbumById(5)).toJSONString());
        //System.out.println(albumService.findAlbumById(5));
        Album album = albumService.findAlbumById(5);

        String publishFormat = "";
        String[] tmp = album.getPublishFormat().split(",");
        if(tmp.length == 1){
            publishFormat = PublishFormat.getNameByIndex(Integer.parseInt(tmp[0]));
        }else{
            for(int i=0;i<tmp.length;i++){
                System.out.println(tmp[i]);
                publishFormat += "/" + PublishFormat.getNameByIndex(Integer.parseInt(tmp[i]));
            }
        }
        System.out.println(publishFormat.substring(1));
    }

}
