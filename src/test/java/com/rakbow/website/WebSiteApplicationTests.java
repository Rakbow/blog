package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.Image;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.entry.EntryCategory;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.album.AlbumVOGamma;
import com.rakbow.website.entity.*;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.EntityService;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.service.SearchService;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.website.util.convertMapper.entry.EntryConvertMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import redis.clients.jedis.search.Schema;

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
    @Resource
    private SearchService searchService;

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
    public void entryTests() {
        List<Entry> entries = entryMapper.getEntryByCategory(EntryCategory.PERSONNEL.getId());
        AlbumVOGamma albumVOGamma = new AlbumVOGamma();
    }

    @Test
    public void redisSearchTest() {
        AlbumVOGamma album = albumVOMapper.toVOGamma(albumService.getAlbum(11));
        Schema SCHEMA = new Schema()
                .addTextField("catalogNo", 1.0)  //专辑编号，权重为1
                .addTextField("name", 1.0)  //专辑名称（日语），权重为1
                .addTextField("nameEn", 0.5)  //专辑英文名称，权重为0.5
                .addTextField("nameZh", 0.5)  //专辑中文名称，权重为0.5
                .addTextField("franchises", 0.2)  //所属系列，权重为0.2
                .addTextField("albumFormat", 0.3)  //专辑分类，权重为0.3
                .addNumericField("id")  //表主键
                .addNumericField("hasBonus")  //是否包含特典内容
                .addTagField("products")  //所属产品id，在mysql中以JSON格式存储
                .addTextField("releaseDate", 0)  //发行日期，权重为0.4
                .addTextField("cover", 0)  //封面，权重为0.3
                .addNumericField("visitCount")  //浏览数
                .addNumericField("likeCount");  //点赞数

        //创建索引
        searchService.createIndex("Album", "album:", SCHEMA);

        searchService.addAlbum("album:", album);

        // searchService.search("Album", "ひぐらしのなく頃に");
    }

}
