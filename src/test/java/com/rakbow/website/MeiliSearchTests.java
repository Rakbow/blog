package com.rakbow.website;

import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.entity.*;
import com.rakbow.website.util.common.MeiliSearchUtils;
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
