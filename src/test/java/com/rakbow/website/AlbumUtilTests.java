package com.rakbow.website;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.VisitService;
import com.rakbow.website.util.AlbumUtil;
import com.rakbow.website.util.common.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 0:15
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class AlbumUtilTests {

    @Autowired
    private AlbumService albumService;

    @Autowired
    private AlbumMapper albumMapper;

    @Autowired
    private VisitService visitService;

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

    @Test
    public void test5(){
        System.out.println(albumService.album2Json(albumService.findAlbumById(110)).toJSONString());
    }

    @Test
    public void test6(){
        List<Album> tmp = new ArrayList<>();
        String productId = "10";
        albumMapper.selectAlbumByFilter(null, null, null, productId)
                .stream().forEach(i -> {
                    Arrays.stream(i.getProductId().split(",")).forEach(j -> {
                        if(j.equals(productId)){
                            tmp.add(i);
                            System.out.println(i.getProductId());
                        }
                    });
                });
    }

    @Test
    public void test7() throws ParseException {
        List<String> times = new ArrayList<>();
        times.add("01:03:08");
        times.add("1:31");
        times.add("3:08");
        times.add("22:14");
        times.add("03:44:21");
        times.add("00:40");
        System.out.println(CommonUtil.countTotalTime(times));
    }

    @Test
    public void test8(){
        Arrays.stream(albumService.findAlbumById(100).getProductId().split(",")).forEach(i -> System.out.println(i));
        // albumService.getRelatedAlbums(100).stream().forEach(i -> System.out.println(i));
    }

    @Test
    public void test10(){
        albumService.getJustAddedAlbums(5).forEach(i -> System.out.println(i));
        // albumService.getJustEditedAlbums(5).forEach(i -> System.out.println(i.getString("editedTime")));
    }

}
