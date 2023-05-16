package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.data.image.Image;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Entry;
import com.rakbow.website.entity.Product;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.EntityService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.website.util.convertMapper.entry.EntryConvertMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
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
    private EntityMapper entityMapper;

    @Resource
    private LikeUtil likeUtil;
    @Resource
    private VisitUtil visitUtil;
    @Resource
    private StatisticMapper statisticMapper;
    @Resource
    private EntryMapper entryMapper;

    private final EntryConvertMapper entryConvertMapper = EntryConvertMapper.INSTANCES;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;

    @Test
    public void itemImageCovert() {
        List<Product> products = productMapper.getAll();
        products.forEach(product -> {
            JSONArray imageJsons = JSON.parseArray(product.getImages());
            if(!imageJsons.isEmpty()) {
                List<Image> images = new ArrayList<>();
                for (int i = 0; i < imageJsons.size(); i++) {
                    JSONObject imageJson = imageJsons.getJSONObject(i);
                    Image image = new Image();
                    image.setUrl(imageJson.getString("url"));
                    image.setNameZh(imageJson.getString("nameZh"));
                    image.setNameEn(imageJson.getString("nameEn"));
                    image.setType(imageJson.getIntValue("type"));
                    image.setDescription(imageJson.getString("description"));
                    image.setUploadTime(imageJson.getString("uploadTime"));
                    images.add(image);
                }
                entityMapper.updateItemImages(EntityType.PRODUCT.getNameEn().toLowerCase(), product.getId(), JSON.toJSONString(images), product.getEditedTime());
            }
        });

    }

    @Test
    public void mapStructTest() {

        List<Album> albums = albumMapper.getAll();

        long t1 = System.currentTimeMillis();

        List<AlbumVOAlpha> VOs = albumVOMapper.toVOAlpha(albums);

        long t2 = System.currentTimeMillis();
        
        System.out.println(t2 - t1);
    }

    @Test
    public void jacksonTest() throws JsonProcessingException {

//        List<Album> albums = albumMapper.getAll();

//         Album album = albumService.getAlbum(11);
// //        String json = JsonUtil.toJson(album);
//         JsonNode imagesJson = JsonUtil.toNode(album.getImages());
//         System.out.println(imagesJson);
//
//         List<Image> images = JsonUtil.toList(imagesJson, Image.class);
//         images.forEach(System.out::println);
    }

    @Test
    public void entryTests() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.PERSONNEL.getId());
    }

}
