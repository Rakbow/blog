package com.rakbow.website;

import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.service.VisitService;
import com.rakbow.website.util.BookUtils;
import com.rakbow.website.util.common.DataFinder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    @Value("${website.path.img}")
    private String imgPath;
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
        System.out.println(musicMapper.addMusic(music));
        System.out.println(music.getId());
    }

    // @Test
    // public void getAlbumsByFilterListTest() throws IOException {
    //     List<Integer> products = new ArrayList<>();
    //     products.add(1);
    //     List<Integer> publishFormat = new ArrayList<>();
    //     publishFormat.add(1);
    //     List<Integer> albumFormat = new ArrayList<>();
    //     albumFormat.add(4);
    //     List<Integer> mediaFormat = new ArrayList<>();
    //     mediaFormat.add(1);
    //     String orderField = "id";
    //     int desc = 0;
    //     List<Album> albums = albumMapper.getAlbumsByFilter("", "", 1, products, publishFormat,
    //             albumFormat, mediaFormat, "0", orderField, desc,  0, 5);
    //
    //     albums.forEach(album -> {
    //         System.out.println(album.getId());
    //     });
    //
    // }

    @Test
    public void test111() {
        String tmp = "しゅー カズー 連 珠洲城くるみ 剛田ナギ 片岡みちる ほりやゆ 凪妖女 両角潤香 相沢 山斗 楠見らんま 橘りた ドバト らすぷ～ あづま笙子 KANZUME にゃん味噌 にくばなれ";
        System.out.println(tmp.replaceAll(" ", ","));
    }

    @Test
    public void test112() {
        System.out.println(BookUtils.getISBN10("9784776722779"));
    }

}
