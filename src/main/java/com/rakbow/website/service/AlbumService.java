package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.bo.AlbumDiscBO;
import com.rakbow.website.data.dto.AlbumDiscDTO;
import com.rakbow.website.data.dto.AlbumTrackDTO;
import com.rakbow.website.data.emun.MediaFormat;
import com.rakbow.website.data.emun.album.AlbumFormat;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.album.AlbumVOBeta;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import com.rakbow.website.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-25 1:42
 * @Description: album业务层
 */
@Service
public class AlbumService {

    //region ------依赖注入------
    @Resource
    private AlbumMapper albumMapper;
    @Resource
    private MusicService musicService;
    @Resource
    private QiniuFileUtil qiniuFileUtil;
    @Resource
    private VisitUtil visitUtil;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    //endregion

    //region ------更删改查------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 新增专辑
     *
     * @param album 新增的专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addAlbum(Album album) {
        int id = albumMapper.addAlbum(album);
        return String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.ALBUM.getNameZh());
    }

    /**
     * 根据Id获取专辑,泛用
     *
     * @param id 专辑id
     * @return album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Album getAlbum(int id) {
        return albumMapper.getAlbum(id, true);
    }

    /**
     * 根据Id获取Album,需要判断权限
     *
     * @param id id
     * @return Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Album getAlbumWithAuth(int id, int userAuthority) {
        if(userAuthority > 2) {
            return albumMapper.getAlbum(id, true);
        }
        return albumMapper.getAlbum(id, false);
    }

    /**
     * 根据Id删除专辑
     *
     * @param album 专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAlbumById(Album album) {
        //删除前先把服务器上对应图片全部删除
        qiniuFileUtil.commonDeleteAllFiles(JSON.parseArray(album.getImages()));
        //删除专辑
        albumMapper.deleteAlbumById(album.getId());
        visitUtil.deleteVisit(EntityType.ALBUM.getId(), album.getId());
    }

    /**
     * 更新专辑基础信息
     *
     * @param id 专辑id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateAlbum(int id, Album album) {
        albumMapper.updateAlbum(id, album);
        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.ALBUM.getNameZh());
    }

    //endregion

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param albumJson 专辑json
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkAlbumJson(JSONObject albumJson) {
        if (StringUtils.isBlank(albumJson.getString("name"))) {
            return ApiInfo.ALBUM_NAME_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("releaseDate"))) {
            return ApiInfo.ALBUM_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("franchises"))
                || StringUtils.equals(albumJson.getString("franchises"), "[]")) {
            return ApiInfo.FRANCHISES_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("products"))
                || StringUtils.equals(albumJson.getString("products"), "[]")) {
            return ApiInfo.PRODUCTS_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("publishFormat"))
                || StringUtils.equals(albumJson.getString("publishFormat"), "[]")) {
            return ApiInfo.ALBUM_PUBLISH_FORMAT_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("albumFormat"))
                || StringUtils.equals(albumJson.getString("albumFormat"), "[]")) {
            return ApiInfo.ALBUM_ALBUM_FORMAT_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("mediaFormat"))
                || StringUtils.equals(albumJson.getString("mediaFormat"), "[]")) {
            return ApiInfo.ALBUM_MEDIA_FORMAT_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送专辑数据
     *
     * @param albumJson 专辑json
     * @return 处理后的album json格式数据
     * @author rakbow
     */
    public JSONObject handleAlbumJson(JSONObject albumJson) {

        String[] products = CommonUtil.str2SortedArray(albumJson.getString("products"));
        String[] franchises = CommonUtil.str2SortedArray(albumJson.getString("franchises"));
        String[] publishFormat = CommonUtil.str2SortedArray(albumJson.getString("publishFormat"));
        String[] albumFormat = CommonUtil.str2SortedArray(albumJson.getString("albumFormat"));
        String[] mediaFormat = CommonUtil.str2SortedArray(albumJson.getString("mediaFormat"));

        //处理时间
        // String releaseDate = CommonUtil.dateToString(albumJson.getDate("releaseDate"));

        albumJson.put("releaseDate", albumJson.getDate("releaseDate"));
        albumJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");
        albumJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        albumJson.put("publishFormat", "{\"ids\":[" + StringUtils.join(publishFormat, ",") + "]}");
        albumJson.put("albumFormat", "{\"ids\":[" + StringUtils.join(albumFormat, ",") + "]}");
        albumJson.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return albumJson;
    }

    //endregion

    //region ------更新复杂数据------

    /**
     * 更新专辑Artists
     *
     * @param id      专辑id
     * @param artists 专辑的创作相关信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateAlbumArtists(int id, String artists) {
        albumMapper.updateAlbumArtists(id, artists, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_ALBUM_ARTISTS_SUCCESS;
    }

    /**
     * 更新音轨信息
     *
     * @param id        专辑id
     * @param _discList 专辑的音轨信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateAlbumTrackInfo(int id, String _discList) throws Exception {

        List<AlbumDiscDTO> albumDiscDTOs = JSON.parseArray(_discList).toJavaList(AlbumDiscDTO.class);

        //获取该专辑对应的音乐合集
        List<Music> musics = musicService.getMusicsByAlbumId(id);

        JSONObject trackInfo = new JSONObject();

        List<AlbumDiscBO> discList = new ArrayList<>();

        int discSerial = 1;

        String trackSerial;

        for (AlbumDiscDTO albumDiscDTO : albumDiscDTOs) {
            AlbumDiscBO albumDiscBO = new AlbumDiscBO();
            JSONArray trackList = new JSONArray();
            int i = 1;
            for (AlbumTrackDTO _track : albumDiscDTO.getTrackList()) {
                if (i < 10) {
                    trackSerial = "0" + i;
                } else {
                    trackSerial = Integer.toString(i);
                }
                _track.setSerial(trackSerial);

                //往music表中添加新数据
                //若musicId为0则代表该条数据为新增数据
                if (_track.getMusicId() == 0) {
                    Music music = new Music();
                    music.setName(_track.getName());
                    music.setAlbumId(id);
                    music.setDiscSerial(discSerial);
                    music.setTrackSerial(trackSerial);

                    //去除时间中含有的\t影响
                    String _time = _track.getLength();
                    if (_time.contains("\t")) {
                        music.setAudioLength(_time.replace("\t", ""));
                    }else {
                        music.setAudioLength(_time);
                    }

                    musicService.addMusic(music);
                    trackList.add(music.getId());
                } else {
                    //若musicId不为0则代表之前已经添加进music表中，需要进一步更新
                    Music currentMusic = DataFinder.findMusicById(_track.getMusicId(), musics);
                    assert currentMusic != null;
                    currentMusic.setName(_track.getName());
                    currentMusic.setAudioLength(_track.getLength());
                    currentMusic.setDiscSerial(discSerial);
                    currentMusic.setTrackSerial(trackSerial);
                    //更新对应music
                    musicService.updateMusic(currentMusic.getId(), currentMusic);
                    trackList.add(currentMusic.getId());
                    musics.remove(currentMusic);
                }

                i++;
            }

            albumDiscBO.setSerial(discSerial);
            albumDiscBO.setMediaFormat(MediaFormat.nameEn2IndexArray(albumDiscDTO.getMediaFormat()));
            albumDiscBO.setAlbumFormat(AlbumFormat.nameEn2IndexArray(albumDiscDTO.getAlbumFormat()));
            albumDiscBO.setTrackList(trackList);

            discSerial++;
            discList.add(albumDiscBO);
        }
        if(discList.size() != 0) {
            trackInfo.put("discList", discList);
        }
        albumMapper.updateAlbumTrackInfo(id, trackInfo.toJSONString(), new Timestamp(System.currentTimeMillis()));

        //删除对应music
        if (musics.size() != 0) {
            for (Music music : musics) {
                musicService.deleteMusic(music);
            }
        }
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getAlbumsByFilter(JSONObject queryParams, int userAuthority) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String catalogNo = filter.getJSONObject("catalogNo").getString("value");
        String name = filter.getJSONObject("name").getString("value");

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        }else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        List<Integer> publishFormat = filter.getJSONObject("publishFormat").getList("value", Integer.class);
        List<Integer> albumFormat = filter.getJSONObject("albumFormat").getList("value", Integer.class);
        List<Integer> mediaFormat = filter.getJSONObject("mediaFormat").getList("value", Integer.class);

        List<Album> albums = albumMapper.getAlbumsByFilter(catalogNo, name, franchises, products, publishFormat,
                albumFormat, mediaFormat, hasBonus, userAuthority > 2, sortField, sortOrder,  first, row);

        int total = albumMapper.getAlbumRowsByFilter(catalogNo, name, franchises, products, publishFormat,
                albumFormat, mediaFormat, hasBonus, userAuthority > 2);

        return new SearchResult(total, albums);
    }

    /**
     * 生成关联专辑信息
     *
     * @param album 专辑
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public int[] generateRelatedAlbumIds(Album album) {

        List<Album> result = new ArrayList<>();

        //该专辑包含的作品id
        List<Integer> productIds = JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class);

        //该系列所有专辑
        List<Album> allAlbums = albumMapper.getAlbumsByFilter(null, null, CommonUtil.ids2List(album.getFranchises()),
                null, null, null, null, null, false, "releaseDate",
                1, 0, 0).stream().filter(tmpAlbum -> tmpAlbum.getId() != album.getId()).collect(Collectors.toList());

        List<Album> queryResult = allAlbums.stream().filter(tmpAlbum ->
                StringUtils.equals(tmpAlbum.getProducts(), album.getProducts())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Album> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Album> tmpQueryResult = allAlbums.stream().filter(tmpAlbum ->
                        JSONObject.parseObject(tmpAlbum.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Album> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allAlbums.stream().filter(tmpAlbum ->
                                JSONObject.parseObject(tmpAlbum.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtil.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }
        //去重
        CommonUtil.removeDuplicateList(result);

        List<Integer> ids = new ArrayList<>();

        result.forEach(a -> ids.add(a.getId()));

        return ids.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * 获取相关联专辑
     *
     * @param album 专辑
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<AlbumVOBeta> getRelatedAlbums(int id) {



        return albumVOMapper.album2VOBeta(CommonUtil.removeDuplicateList(result));
    }

    /**
     * 根据作品id获取关联专辑
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<AlbumVOBeta> getAlbumsByProductId(int productId) {

        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Album> albums = albumMapper.getAlbumsByFilter(null, null, null, products,
                null, null, null, null, false, "releaseDate",
                -1,  0, 0);

        return albumVOMapper.album2VOBeta(albums);
    }

    //endregion

}
