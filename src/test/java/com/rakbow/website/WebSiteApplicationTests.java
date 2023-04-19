package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.SimpleSearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.album.AlbumVO;
import com.rakbow.website.entity.*;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.EntityService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import com.rakbow.website.util.entity.BookUtil;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

//import com.rakbow.website.util.convertMapper.GameVoMapper;

@SpringBootTest
class WebSiteApplicationTests {

    @Resource
    private AlbumService albumService;
    @Resource
    private EntityService entityService;
    @Resource
    private MusicService musicService;
    @Resource
    private MusicMapper musicMapper;

    @Resource
    private ProductMapper productMapper;

    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private BookMapper bookMapper;
    @Resource
    private DiscMapper discMapper;
    @Resource
    private GameMapper gameMapper;
    @Resource
    private MerchMapper merchMapper;
    @Resource
    private FranchiseMapper franchiseMapper;

    @Resource
    private LikeUtil likeUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private StatisticMapper statisticMapper;

    @Test
    void contextLoads() {
    }

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
        System.out.println(BookUtil.getISBN10("9784776722779"));
    }

    @Test
    public void mapStructTest () {

//        List<Album> albums = albumMapper.getAlbumsByFilter(null, null, null, null,
//                null, null, null, null,
//                null, -1, 0, 10);

        Album album = albumService.getAlbum(11);

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

    @Test
    public void addStatisticInfo() {
        // List<Album> albums = albumMapper.getAll();
        List<Book> books = bookMapper.getAll();
        List<Disc> discs = discMapper.getAll();
        List<Game> games = gameMapper.getAll();
        List<Merch> merches = merchMapper.getAll();
        List<Product> products = productMapper.getAll();
        List<Franchise> franchises = franchiseMapper.getAll();
        List<Music> musics = musicMapper.getAll();

        books.forEach(book -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.BOOK.getId(), book.getId(), book.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.BOOK.getId(), book.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.BOOK.getId(), book.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        discs.forEach(disc -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.DISC.getId(), disc.getId(), disc.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.DISC.getId(), disc.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.DISC.getId(), disc.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        games.forEach(game -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.GAME.getId(), game.getId(), game.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.GAME.getId(), game.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.GAME.getId(), game.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        merches.forEach(merch -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.MERCH.getId(), merch.getId(), merch.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.MERCH.getId(), merch.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.MERCH.getId(), merch.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        products.forEach(product -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.PRODUCT.getId(), product.getId(), product.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.PRODUCT.getId(), product.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.PRODUCT.getId(), product.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        franchises.forEach(franchise -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.FRANCHISE.getId(), franchise.getId(), franchise.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.FRANCHISE.getId(), franchise.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.FRANCHISE.getId(), franchise.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
        musics.forEach(music -> {
            EntityStatistic entityStatistic = new EntityStatistic(EntityType.MUSIC.getId(), music.getId(), music.getStatus());
            entityStatistic.setVisitCount(visitUtil.getVisit(EntityType.MUSIC.getId(), music.getId()));
            entityStatistic.setLikeCount(likeUtil.getLike(EntityType.MUSIC.getId(), music.getId()));
            statisticMapper.addStatistic(entityStatistic);
        });
    }

    @Test
    public void searchTest() {
        long t1 = new Date().getTime();
        SimpleSearchResult result = entityService.simpleSearch("i believe", 9, 0, 10);
        long t2 = new Date().getTime();
        System.out.println(t2 - t1);
    }

    @Test
    public void test999() {
        List<Book> books = bookMapper.getAll();

        books.forEach(book -> {
            JSONArray authors = JSON.parseArray(book.getAuthors());
            if(authors.size() != 0) {
                for (int i = 0; i < authors.size(); i++) {
                    JSONObject author = authors.getJSONObject(i);
                    author.put("main", 0);
                }
                bookMapper.updateBookAuthors(book.getId(), authors.toJSONString(), book.getEditedTime());
            }
        });

    }

}
