package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.meilisearch.sdk.Client;
import com.meilisearch.sdk.Config;
import com.meilisearch.sdk.Index;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.meilisearch.sdk.model.SearchResult;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.vo.album.AlbumVOGamma;
import com.rakbow.website.entity.*;
import com.rakbow.website.util.common.MeiliSearchUtils;
import com.rakbow.website.util.file.CommonImageUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class MeiliSearchTests {

    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private DiscMapper discMapper;
    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private MerchMapper merchMapper;
    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private MeiliSearchUtils meiliSearchUtils;

    @Test
    public void test1() throws MeilisearchException {

        JSONArray items = new JSONArray();

        List<Album> albums = albumMapper.getAll();

        albums.forEach(album -> {
            JSONObject albumJson = new JSONObject();
            albumJson.put("id", album.getId());
            albumJson.put("catalogNo", album.getCatalogNo());
            albumJson.put("name", album.getName());
            albumJson.put("releaseDate", album.getReleaseDate());
            albumJson.put("price", album.getPrice());
            segmentImagesResult segmentImages = CommonImageUtils.segmentImages(album.getImages(), 250, false);
            albumJson.put("cover", segmentImages.cover.getString("url"));
            items.add(albumJson);
        });

        JSONArray array = new JSONArray();
        array.add(items);
        String documents = array.getJSONArray(0).toString();
        Client client = new Client(new Config("http://localhost:7700", "masterKey"));

        Index index = client.index("albums");

        index.addDocuments(items.toJSONString());

    }

    @Test
    public void searchTest() throws MeilisearchException {
//        Client client = new Client(new Config("http://localhost:7700", "masterKey"));
//        meiliSearchUtils.deleteData(156, EntityType.ALBUM);


    }

    @Test
    public void saveTests() throws MeilisearchException {
       List<Album> albums = albumMapper.getAll();
       List<Book> books = bookMapper.getAll();
       List<Disc> discs = discMapper.getAll();
       List<Game> games = gameMapper.getAll();
       List<Merch> merches = merchMapper.getAll();
//        List<Product> products = productMapper.getAll();



       meiliSearchUtils.saveMultiData(albums, EntityType.ALBUM);
       meiliSearchUtils.saveMultiData(books, EntityType.BOOK);
       meiliSearchUtils.saveMultiData(discs, EntityType.DISC);
       meiliSearchUtils.saveMultiData(games, EntityType.GAME);
       meiliSearchUtils.saveMultiData(merches, EntityType.MERCH);
//        meiliSearchUtils.saveMultiData(products, EntityType.PRODUCT);
    }


}
