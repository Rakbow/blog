package com.rakbow.blog;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
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

import java.text.ParseException;

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

    @Test
    public void albumTest() throws ParseException {
        JSONObject jo = new JSONObject();
        jo.put("id",1);
        jo.put("releaseDate", "2002/01/09");
        System.out.println(albumService.json2Album(jo));
    }

    @Test
    public void test2(){
        albumService.getRelatedAlbums(3).stream().forEach(i -> System.out.println(i));
    }

    @Test
    public void test3(){
        JSONObject album = albumService.album2Json(albumService.findAlbumById(110));
        JSONArray imgUrl = JSONArray.parseArray(album.get("imgUrl").toString());
        for(int i=0;i<imgUrl.size();i++){
            JSONObject img = imgUrl.getJSONObject(i);
            if(img.get("name").toString().equals("Cover") || img.get("name").toString().equals("Front")){
                System.out.println(img.get("url").toString());
            }
        }
    }


}
