package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.SimpleSearchResult;
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
import com.rakbow.website.data.pageInfo;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.entity.*;
import com.rakbow.website.util.common.*;
import com.rakbow.website.util.convertMapper.*;
import com.rakbow.website.util.entity.MusicUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import com.rakbow.website.util.file.QiniuFileUtil;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
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
    private RedisUtil redisUtil;
    @Resource
    private LikeUtil likeUtil;
    @Resource
    private EntityMapper entityMapper;
    @Resource
    private QiniuImageUtil qiniuImageUtil;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;
    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;
    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;
    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    private final List<Integer> searchEntityTypes = new ArrayList<>(){{
        add(EntityType.ALBUM.getId());
        add(EntityType.BOOK.getId());
        add(EntityType.DISC.getId());
        add(EntityType.GAME.getId());
        add(EntityType.MERCH.getId());
    }};

    //endregion

    //region common get data

    /**
     * 获取页面数据
     * @param entityType,entityId,addedTime,editedTime 实体类型，实体id,收录时间,编辑时间
     * @Author Rakbow
     */
    public pageInfo getPageInfo(int entityType, int entityId, Object entity, HttpServletRequest request) {

        JSONObject json = JSON.parseObject(JSON.toJSONString(entity));

        Timestamp addedTime = new Timestamp(json.getDate("addedTime").getTime());
        Timestamp editedTime = new Timestamp(json.getDate("editedTime").getTime());

        pageInfo pageInfo = new pageInfo();

        // 从cookie中获取点赞token
        String likeToken = CookieUtil.getValue(request, "like_token");
        if(likeToken == null) {
            pageInfo.setLiked(false);
        }else {
            pageInfo.setLiked(likeUtil.isLike(entityType, entityId, likeToken));
        }

        // 从cookie中获取访问token
        String visitToken = CookieUtil.getValue(request, "visit_token");

        pageInfo.setAddedTime(CommonUtil.timestampToString(addedTime));
        pageInfo.setEditedTime(CommonUtil.timestampToString(editedTime));
        pageInfo.setVisitCount(visitUtil.incVisit(entityType, entityId, visitToken));
        pageInfo.setLikeCount(likeUtil.getLike(entityType, entityId));

        return pageInfo;
    }

    /**
     * 获取实体表数据
     *
     * @return JSONObject
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONObject getItemAmount() {
        JSONObject entityAmounts = new JSONObject();
        for (EntityType type : EntityType.ENTITY_TYPES) {
            Object amount;
            try {
                amount = redisUtil.get("entity_amount:" + type.getId());
            }
            catch (Exception e) {
                amount = 0;
            }
            entityAmounts.put(type.getNameEn().toLowerCase() + "Amount", amount);
        }
        return entityAmounts;
    }

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

        if(!ids.isEmpty()) {
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

    //endregion

    //region refresh redis data

    /**
     * 刷新Redis缓存中的枚举类数据
     *
     * @author rakbow
     */
    public void refreshRedisEmunData () {

        redisUtil.set(RedisCacheConstant.ALBUM_FORMAT_SET, AlbumFormat.getAlbumFormatSet());
        redisUtil.set(RedisCacheConstant.PUBLISH_FORMAT_SET, PublishFormat.getPublishFormatSet());
        redisUtil.set(RedisCacheConstant.MEDIA_FORMAT_SET, MediaFormat.getMediaFormatSet());
        redisUtil.set(RedisCacheConstant.BOOK_TYPE_SET, BookType.getBookTypeSet());
        redisUtil.set(RedisCacheConstant.REGION_SET, Region.getRegionSet());
        redisUtil.set(RedisCacheConstant.LANGUAGE_SET, Language.getLanguageSet());
        redisUtil.set(RedisCacheConstant.MERCH_CATEGORY_SET, MerchCategory.getMerchCategorySet());
        redisUtil.set(RedisCacheConstant.GAME_PLATFORM_SET, GamePlatform.getGamePlatformSet());
        redisUtil.set(RedisCacheConstant.RELEASE_TYPE_SET, ReleaseType.getReleaseTypeSet());
        redisUtil.set(RedisCacheConstant.PRODUCT_CATEGORY_SET, ProductCategory.getProductCategorySet());
        redisUtil.set(RedisCacheConstant.AUDIO_TYPE_SET, MusicUtil.getAudioTypeSet());

    }

    /**
     * 刷新Redis缓存中的搜索页首页图片连接地址
     *
     * @author rakbow
     */
    public void refreshIndexCoverUrls () {

        JSONObject indexCoverUrl = new JSONObject();

        List<String> bookUrls = new ArrayList<>();
        List<String> albumUrls = new ArrayList<>();
        List<String> discUrls = new ArrayList<>();
        List<String> gameUrls = new ArrayList<>();
        List<String> merchUrls = new ArrayList<>();

        albumMapper.getAlbums(new ArrayList<>(){{
            add(11);add(13);add(109);add(10);add(6);
        }}).forEach(album -> albumUrls.add(QiniuImageUtil.getThumbUrl(CommonImageUtil.getCoverUrl(album.getImages()), 500)));

        bookMapper.getBooks(new ArrayList<>(){{
            add(148);add(173);add(121);add(18);add(36);
        }}).forEach(book -> bookUrls.add(QiniuImageUtil.getCustomThumbUrl(CommonImageUtil.getCoverUrl(book.getImages()), 240, 0)));

        indexCoverUrl.put("album", albumUrls);
        indexCoverUrl.put("book", bookUrls);
        indexCoverUrl.put("disc", discUrls);
        indexCoverUrl.put("game", gameUrls);
        indexCoverUrl.put("merch", merchUrls);

        redisUtil.set(RedisCacheConstant.INDEX_COVER_URL, indexCoverUrl);

    }

    //endregion

    //region common CRUD

    /**
     * 更新数据库实体激活状态
     * @param entityName,entityId,status 实体表名,实体id,状态
     * @author rakbow
     */
    public void updateItemStatus(String entityName, int entityId, int status) {
        entityMapper.updateItemStatus(entityName, entityId, status);
    }

    /**
     * 批量更新数据库实体激活状态
     * @param entityName,ids,status 实体表名,ids,状态
     * @author rakbow
     */
    public void updateItemsStatus(String entityName, List<Integer> ids, int status) {
        entityMapper.updateItemsStatus(entityName, ids, status);
    }

    /**
     * 点赞实体
     * @param entityType,entityId,likeToken 实体表名,实体id,点赞token
     * @author rakbow
     */
    public boolean entityLike(int entityType, int entityId, String likeToken) {
        //点过赞
        if(likeUtil.isLike(entityType, entityId, likeToken)) {
            return false;
        }else {//没点过赞,自增
            likeUtil.incLike(entityType, entityId, likeToken);
            return true;
        }
    }

    /**
     * 更新描述
     *
     * @param entityName,entityId 实体表名,实体id
     * @param description 描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateItemDescription(String entityName, int entityId, String description) {
        entityMapper.updateItemDescription(entityName, entityId, description, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_DESCRIPTION_SUCCESS;
    }

    /**
     * 更新特典信息
     *
     * @param entityName,entityId 实体表名,实体id
     * @param bonus 描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateItemBonus(String entityName, int entityId, String bonus) {
        entityMapper.updateItemBonus(entityName, entityId, bonus, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_BONUS_SUCCESS;
    }

    //endregion

    //region image operation

    /**
     * 根据实体类型和实体Id获取图片
     *
     * @param entityName,entityId 实体表名 实体id
     * @return JSONArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public JSONArray getItemImages(String entityName, int entityId) {
        return JSON.parseArray(entityMapper.getItemImages(entityName, entityId));
    }

    /**
     * 新增图片
     *
     * @param entityId           实体id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addItemImages(String entityName, int entityId, MultipartFile[] images, JSONArray originalImagesJson,
                                JSONArray imageInfos, User user) throws IOException {

        JSONArray finalImageJson = qiniuImageUtil.commonAddImages
                (entityId, entityName, images, originalImagesJson, imageInfos, user);

        entityMapper.updateItemImages(entityName, entityId, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));

        return ApiInfo.INSERT_IMAGES_SUCCESS;
    }

    /**
     * 更新图片
     *
     * @param entityId     图书id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateItemImages(String entityName, int entityId, String images) {
        entityMapper.updateItemImages(entityName, entityId, images, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_IMAGES_SUCCESS;
    }

    /**
     * 删除图片
     *
     * @param entityName,entityId,images,deleteImages 实体表名,实体id,原图片信息,删除图片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteItemImages(String entityName, int entityId, JSONArray deleteImages) throws Exception {

        JSONArray images = getItemImages(entityName, entityId);

        JSONArray finalImageJson = qiniuFileUtil.commonDeleteFiles(images, deleteImages);

        entityMapper.updateItemImages(entityName, entityId, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return ApiInfo.DELETE_IMAGES_SUCCESS;
    }

    //endregion

    //region search

    /**
     * 查询
     *
     * @param keyword 关键字
     * @author rakbow
     */
    public SimpleSearchResult simpleSearch(String keyword, int entityType, int offset, int limit) {

        // String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();

        SimpleSearchResult res = new SimpleSearchResult(keyword, entityType, EntityType.getItemNameZhByIndex(entityType), offset, limit);

        if (keyword.isEmpty()) {
            return res;
        }

        if (!searchEntityTypes.contains(entityType)) {
            return res;
        }

        if(entityType == EntityType.ALBUM.getId()) {
            List<Album> albums = albumMapper.simpleSearch(keyword, limit, offset);
            if(!albums.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(albumVOMapper.album2VOGamma(albums))));
                res.setTotal(albumMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == EntityType.BOOK.getId()) {
            List<Book> books = bookMapper.simpleSearch(keyword, limit, offset);
            if(!books.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(bookVOMapper.book2VOGamma(books))));
                res.setTotal(bookMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == EntityType.DISC.getId()) {
            List<Disc> discs = discMapper.simpleSearch(keyword, limit, offset);
            if(!discs.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(discVOMapper.disc2VOGamma(discs))));
                res.setTotal(discMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == EntityType.GAME.getId()) {
            List<Game> games = gameMapper.simpleSearch(keyword, limit, offset);
            if(!games.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(gameVOMapper.game2VOGamma(games))));
                res.setTotal(gameMapper.simpleSearchCount(keyword));
            }
        }
        if(entityType == EntityType.MERCH.getId()) {
            List<Merch> merchs = merchMapper.simpleSearch(keyword, limit, offset);
            if(!merchs.isEmpty()) {
                res.setData(JSON.parseArray(JSON.toJSONString(merchVOMapper.merch2VOGamma(merchs))));
                res.setTotal(merchMapper.simpleSearchCount(keyword));
            }
        }
        return res;
    }

    //endregion

}
