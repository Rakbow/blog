package com.rakbow.website;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.data.vo.album.AlbumVO;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
//import com.rakbow.website.util.convertMapper.GameVoMapper;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.*;
import com.rakbow.website.util.entity.BookUtils;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.RedisUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@SpringBootTest
class WebSiteApplicationTests {

    @Autowired
    private AlbumService albumService;
    @Autowired
    private GameService gameService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private VisitService visitService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private RedisUtil redisUtil;
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
        // albums.sort(, rollbackFor = Exception.classorter.albumSortById);
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
    public void test112() {
        System.out.println(BookUtils.getISBN10("9784776722779"));
    }

    @Test
    public void redisTest() {

       productService.refreshRedisProducts();

       franchiseService.refreshRedisFranchises();

       commonService.refreshRedisEmunData();

    }

    @Test
    public void mapStructTest () {

//        List<Album> albums = albumMapper.getAlbumsByFilter(null, null, null, null,
//                null, null, null, null,
//                null, -1, 0, 10);

        Album album = albumService.getAlbumById(11);

        Timestamp t1 = new Timestamp(System.currentTimeMillis());



//        List<JSONObject> albumJsons = albumService.album2JsonList(albums);

        Timestamp t2 = new Timestamp(System.currentTimeMillis());
        System.out.println(t2.getTime() - t1.getTime());

        Timestamp t3 = new Timestamp(System.currentTimeMillis());

        AlbumVO albumVo = AlbumVOMapper.INSTANCES.album2VO(album);

//        List<AlbumVoList> albumVoLists = AlbumVoMapper.INSTANCES.album2VoLists(albums);

        Timestamp t4 = new Timestamp(System.currentTimeMillis());

        System.out.println(t4.getTime() - t3.getTime());
    }

}
