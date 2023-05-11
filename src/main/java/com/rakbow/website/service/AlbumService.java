package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.bo.AlbumDiscBO;
import com.rakbow.website.data.dto.AlbumDiscDTO;
import com.rakbow.website.data.dto.AlbumTrackDTO;
import com.rakbow.website.data.dto.QueryParams;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.emun.entity.album.AlbumFormat;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.system.UserAuthority;
import com.rakbow.website.data.vo.album.AlbumVO;
import com.rakbow.website.data.vo.album.AlbumVOBeta;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.util.common.*;
import com.rakbow.website.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.website.util.entity.AlbumUtil;
import com.rakbow.website.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    @Resource
    private HostHolder hostHolder;

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
    public Album getAlbumWithAuth(int id) {
        if(UserAuthority.isSenior(hostHolder.getUser())) {
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

    public AlbumVO buildVO(Album album, List<Music> musics) {
        AlbumVO VO = albumVOMapper.toVO(album);

        if(UserAuthority.isJunior(hostHolder.getUser())) {
            //可供编辑的editDiscList
            JSONArray editDiscList = AlbumUtil.getEditDiscList(album.getTrackInfo(), musics);
            VO.setEditDiscList(editDiscList);
            //可供编辑的editCompanies
            VO.setEditCompanies(JSON.parseArray(album.getCompanies()));
            //可供编辑的editPersonnel
            VO.setEditPersonnel(JSON.parseArray(album.getPersonnel()));
        }
        //音轨信息
        JSONObject trackInfo = AlbumUtil.getFinalTrackInfo(album.getTrackInfo(), musics);
        VO.setTrackInfo(trackInfo);

        return VO;
    }

    /**
     * 检测数据合法性
     *
     * @param albumJson 专辑json
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkAlbumJson(JsonNode albumJson) {
        if (StringUtils.isBlank(albumJson.get("name").asText())) {
            return ApiInfo.ALBUM_NAME_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.get("releaseDate").asText())) {
            return ApiInfo.ALBUM_RELEASE_DATE_EMPTY;
        }
        if (albumJson.get("franchises").isEmpty()) {
            return ApiInfo.FRANCHISES_EMPTY;
        }
        if (albumJson.get("products").isEmpty()) {
            return ApiInfo.PRODUCTS_EMPTY;
        }
        if (albumJson.get("publishFormat").isEmpty()) {
            return ApiInfo.ALBUM_PUBLISH_FORMAT_EMPTY;
        }
        if (albumJson.get("albumFormat").isEmpty()) {
            return ApiInfo.ALBUM_ALBUM_FORMAT_EMPTY;
        }
        if (albumJson.get("mediaFormat").isEmpty()) {
            return ApiInfo.ALBUM_MEDIA_FORMAT_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送专辑数据
     *
     * @param json 专辑json
     * @return 处理后的album json格式数据
     * @author rakbow
     */
    public ObjectNode handleAlbumJson(ObjectNode json) {

        String[] products = JsonUtil.toStringArray(json.get("products"));
        String[] franchises = JsonUtil.toStringArray(json.get("franchises"));
        String[] publishFormat = JsonUtil.toStringArray(json.get("publishFormat"));
        String[] albumFormat = JsonUtil.toStringArray(json.get("albumFormat"));
        String[] mediaFormat = JsonUtil.toStringArray(json.get("mediaFormat"));

        //处理时间
        // String releaseDate = DateUtil.dateToString(albumJson.getDate("releaseDate"));

        json.put("releaseDate", json.get("releaseDate").asText());
        json.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");
        json.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        json.put("publishFormat", "{\"ids\":[" + StringUtils.join(publishFormat, ",") + "]}");
        json.put("albumFormat", "{\"ids\":[" + StringUtils.join(albumFormat, ",") + "]}");
        json.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return json;
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
        albumMapper.updateAlbumArtists(id, artists, DateUtil.NOW_TIMESTAMP);
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
            albumDiscBO.setMediaFormat(MediaFormat.getIdsByNames(albumDiscDTO.getMediaFormat()));
            albumDiscBO.setAlbumFormat(AlbumFormat.getIdsByNames(albumDiscDTO.getAlbumFormat()));
            albumDiscBO.setTrackList(trackList);

            discSerial++;
            discList.add(albumDiscBO);
        }
        if(discList.size() != 0) {
            trackInfo.put("discList", discList);
        }
        albumMapper.updateAlbumTrackInfo(id, trackInfo.toJSONString(), DateUtil.NOW_TIMESTAMP);

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
    public SearchResult getAlbumsByFilter(QueryParams param) {

        JSONObject filter = param.getFilters();

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
                albumFormat, mediaFormat, hasBonus, UserAuthority.isSenior(hostHolder.getUser()), param.getSortField(), param.getSortOrder(),  param.getFirst(), param.getRows());

        int total = albumMapper.getAlbumRowsByFilter(catalogNo, name, franchises, products, publishFormat,
                albumFormat, mediaFormat, hasBonus, UserAuthority.isSenior(hostHolder.getUser()));

        return new SearchResult(total, albums);
    }

    /**
     * 获取相关联专辑
     *
     * @param id 专辑id
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<AlbumVOBeta> getRelatedAlbums(int id) {

        List<Album> result = new ArrayList<>();

        Album album = getAlbum(id);

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

        return albumVOMapper.toVOBeta(CommonUtil.removeDuplicateList(result));
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

        return albumVOMapper.toVOBeta(albums);
    }

    //endregion

}
