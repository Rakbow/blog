package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.service.VisitService;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.DataSorter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@SpringBootTest
class WebSiteApplicationTests {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private VisitService visitService;
    @Test
    void contextLoads() {
    }

    // @Test
    // public void test1() {
    //     Album album = albumService.findAlbumById(55);
    //     List<Album> albums = new ArrayList<>();
    //     albums.add(album);
    //     albums.add(album);
    //     albums.add(album);
    //     albums.add(album);
    //     System.out.println("原数组：" + albums);
    //     albums = CommonUtil.removeDuplicateList(albums);
    //     System.out.println("后数组：" + albums);
    // }

    // @Test
    // public void test2() {
    //     System.out.println(CommonUtil.getCurrentTime());
    //     DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //     System.out.println(sdf.format(new Timestamp(System.currentTimeMillis())));
    //     System.out.println(CommonUtil.timestampToString(new Timestamp(System.currentTimeMillis())));
    //     // List<Album> albums = albumService.getAll();
    //     // for (Album album : albums) {
    //     // 	JSONArray images = JSON.parseArray(album.getImages());
    //     // 	if (!images.isEmpty()) {
    //     // 		for (int j = 0; j < images.size(); j++) {
    //     // 			JSONObject jo = images.getJSONObject(j);
    //     // 			jo.put("uploadTime", CommonUtil.timestampToString(new Timestamp(System.currentTimeMillis()), "yyyy/MM/dd hh:mm:ss"));
    //     // 		}
    //     // 	}
    //     // }
    // }

    @Test
    public void test3() {
        List<Music> musics = musicService.getAll();
        System.out.println(DataFinder.findMusicById(5, musics));
        // List<Music> musics = musicService.getAll();
		// List<Album> albums = albumService.getAll();
        // albums.sort(DataSorter.albumSortById);
        // for (int i = 0; i < albums.size(); i++) {
        //     Album album = albums.get(i);
        //     JSONObject trackInfo = JSON.parseObject(album.getTrackInfo());
        //
        //     if(trackInfo != null && !Objects.equals(trackInfo.toJSONString(), "{}")) {
        //         JSONArray discList = trackInfo.getJSONArray("disc_list");
        //         for (int j = 0; j < discList.size(); j++) {
        //             JSONObject disc = discList.getJSONObject(j);
        //             JSONArray trackList = disc.getJSONArray("track_list");
        //             List<Integer> _trackList = new ArrayList<>();
        //             for (int k = 0; k < trackList.size(); k++) {
        //                 JSONObject track = trackList.getJSONObject(k);
        //                 Music music = DataFinder.findMusicByNameAndAlbumId(track.getString("name"), "nameJp", album.getId(), musics);
        //                 if(music != null) {
        //                     _trackList.add(music.getId());
        //                 }
        //             }
        //             disc.put("track_list", _trackList);
        //         }
        //         trackInfo.put("disc_list", discList);
        //         albumMapper.updateAlbumTrackInfo(album.getId(), trackInfo.toJSONString(), album.getEditedTime());
        //     }
        // }
    }

    @Test
    public void test4() {
        Music music = new Music();
        System.out.println(musicMapper.insertMusic(music));
        System.out.println(music.getId());
    }

    @Test
    public void test5() {
        musicService.getAll().forEach(music -> {
            //新增访问量实体
            visitService.insertVisit(new Visit(EntityType.MUSIC.getId(), music.getId()));
        });
    }

}
