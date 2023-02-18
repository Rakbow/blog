package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.emun.MediaFormat;
import com.rakbow.website.data.emun.album.AlbumFormat;
import com.rakbow.website.data.emun.album.PublishFormat;
import com.rakbow.website.data.emun.book.BookType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.common.Language;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.emun.game.GamePlatform;
import com.rakbow.website.data.emun.game.ReleaseType;
import com.rakbow.website.data.emun.merch.MerchCategory;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.util.common.DataSorter;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.*;
import com.rakbow.website.util.entity.MusicUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-18 17:30
 * @Description:
 */
@Service
public class EntityService {

    //region instance

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
    private RedisUtil redisUtil;
    @Autowired
    private LikeUtil likeUtil;
    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private VisitUtil visitUtil;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;
    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;
    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;
    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    //endregion

    /**
     * 获取浏览量最高的item
     *
     * @param limit 获取条数
     * @return list
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONArray getPopularItems(int entityType, int limit) {

        SortedMap<Integer, Long> visits = visitUtil.getEntityVisitRanking(entityType, limit);

        List<Integer> ids = new ArrayList<>(visits.keySet());

        if(entityType == EntityType.ALBUM.getId()) {
            List<AlbumVOAlpha> items = albumVOMapper.album2VOAlpha(albumMapper.getAlbums(ids));

            for (AlbumVOAlpha item : items) {
                item.setVisitNum(visits.get(item.getId()));
            }

            items.sort(Collections.reverseOrder(DataSorter.albumSortByVisitNum));
            return JSON.parseArray(JSON.toJSONString(items));
        }
        if(entityType == EntityType.BOOK.getId()) {
            List<BookVOBeta> items = bookVOMapper.book2VOBeta(bookMapper.getBooks(ids));

            for (BookVOBeta item : items) {
                item.setVisitNum(visits.get(item.getId()));
            }

            items.sort(Collections.reverseOrder(DataSorter.bookSortByVisitNum));
            return JSON.parseArray(JSON.toJSONString(items));
        }
        if(entityType == EntityType.DISC.getId()) {
            List<DiscVOAlpha> items = discVOMapper.disc2VOAlpha(discMapper.getDiscs(ids));

            for (DiscVOAlpha item : items) {
                item.setVisitNum(visits.get(item.getId()));
            }

            items.sort(Collections.reverseOrder(DataSorter.discSortByVisitNum));
            return JSON.parseArray(JSON.toJSONString(items));
        }
        if(entityType == EntityType.GAME.getId()) {
            List<GameVOAlpha> items = gameVOMapper.game2VOAlpha(gameMapper.getGames(ids));

            for (GameVOAlpha item : items) {
                item.setVisitNum(visits.get(item.getId()));
            }

            items.sort(Collections.reverseOrder(DataSorter.gameSortByVisitNum));
            return JSON.parseArray(JSON.toJSONString(items));
        }
        if(entityType == EntityType.MERCH.getId()) {
            List<MerchVOAlpha> items = merchVOMapper.merch2VOAlpha(merchMapper.getMerchs(ids));

            for (MerchVOAlpha item : items) {
                item.setVisitNum(visits.get(item.getId()));
            }

            items.sort(Collections.reverseOrder(DataSorter.merchSortByVisitNum));
            return JSON.parseArray(JSON.toJSONString(items));
        }

        return null;
    }

    /**
     * 获取最新收录的item
     *
     * @param limit 获取条数
     * @return list封装的item
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONArray getJustAddedItems(int entityType, int limit) {
        if(entityType == EntityType.ALBUM.getId()) {
            return JSON.parseArray(JSON.toJSONString(albumVOMapper.album2VOBeta(albumMapper.getAlbumOrderByAddedTime(limit))));
        }
        if(entityType == EntityType.BOOK.getId()) {
            return JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOBeta(bookMapper.getBooksOrderByAddedTime(limit))));
        }
        if(entityType == EntityType.DISC.getId()) {
            return JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOBeta(discMapper.getDiscsOrderByAddedTime(limit))));
        }
        if(entityType == EntityType.GAME.getId()) {
            return JSON.parseArray(JSON.toJSONString(gameVOMapper.game2VOBeta(gameMapper.getGamesOrderByAddedTime(limit))));
        }
        if(entityType == EntityType.MERCH.getId()) {
            return JSON.parseArray(JSON.toJSONString(merchVOMapper.merch2VOAlpha(merchMapper.getMerchsOrderByAddedTime(limit))));
        }
        return null;
    }

    /**
     * 刷新Redis缓存中的枚举类数据
     *
     * @author rakbow
     */
    public void refreshRedisEmunData () {

        redisUtil.set("albumFormatSet", AlbumFormat.getAlbumFormatSet());
        redisUtil.set("publishFormatSet", PublishFormat.getPublishFormatSet());
        redisUtil.set("mediaFormatSet", MediaFormat.getMediaFormatSet());
        redisUtil.set("bookTypeSet", BookType.getBookTypeSet());
        redisUtil.set("regionSet", Region.getRegionSet());
        redisUtil.set("languageSet", Language.getLanguageSet());
        redisUtil.set("merchCategorySet", MerchCategory.getMerchCategorySet());
        redisUtil.set("platformSet", GamePlatform.getGamePlatformSet());
        redisUtil.set("releaseTypeSet", ReleaseType.getReleaseTypeSet());
        redisUtil.set("productCategorySet", ProductCategory.getProductCategorySet());
        redisUtil.set("audioTypeSet", MusicUtil.getAudioTypeSet());

    }

    /**
     * 更新数据库实体激活状态
     * @param entityName,entityId,status 实体表名,实体id,状态
     * @author rakbow
     */
    public void updateItemStatus(String entityName, int entityId, int status) {
        entityMapper.updateItemStatus(entityName, entityId, status);
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    public void updateItemStatus(int entityType, int entityId, String likeToken) {
        //若点赞token存在
        if(likeToken != null) {
            String likeTmpTokenKey = likeUtil.getEntityLikeTmpTokenKey(entityType, entityId, likeToken);
        }else {

        }

    }

}
