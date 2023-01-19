package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.music.MusicVOAlpha;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.convertMapper.MusicVOMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.DataFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 2:04
 * @Description: music业务层
 */
@Service
public class MusicService {

    //region ------引入实例------
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private VisitService visitService;

    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;
    //endregion

    //region -----曾删改查------

    /**
     * 根据音乐id获取音乐
     * @author rakbow
     * @param id 音乐id
     * @return music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Music getMusicById(int id) {
        return musicMapper.getMusicById(id);
    }

    /**
     * 获取所有音乐数据
     * @author rakbow
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Music> getAll() {
        return musicMapper.getAll();
    }

    /**
     * 根据专辑id获取该专辑所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Music> getMusicsByAlbumId(int albumId) {
        return musicMapper.getMusicsByAlbumId(albumId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int getMusicRows() {
        return musicMapper.getMusicRows();
    }

    /**
     * 新增music
     * @param music music
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMusic(Music music) throws Exception {
        try {
            musicMapper.addMusic(music);

            //新增访问量实体
            visitService.insertVisit(new Visit(EntityType.MUSIC.getId(), music.getId()));

        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 从专辑数据中新增music
     * @author rakbow
     * @param albumJson albumJson
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMusicsFromAlbum(JSONObject albumJson){

        int albumId = albumJson.getInteger("id");

        JSONObject trackInfo = albumJson.getJSONObject("trackInfo");

        if (trackInfo == null || Objects.equals(trackInfo.toJSONString(), "{}")) {
            return;
        }

        JSONObject cover = albumJson.getJSONObject("cover");

        JSONArray discList = trackInfo.getJSONArray("disc_list");

        for (int i = 0; i < discList.size(); i++) {
            JSONObject disc = discList.getJSONObject(i);
            JSONArray trackList = disc.getJSONArray("track_list");
            for (int j = 0; j < trackList.size(); j++) {
                JSONObject track = trackList.getJSONObject(j);
                Music music = new Music();
                music.setName(track.getString("name"));
                music.setAlbumId(albumId);
                music.setDiscSerial(disc.getInteger("serial"));
                music.setTrackSerial(track.getString("serial"));
                music.setCoverUrl(cover.getString("url"));
                music.setAudioLength(track.getString("audioLength"));
                music.setAddedTime(CommonUtils.stringToTimestamp(albumJson.getString("addedTime")));
                music.setEditedTime(CommonUtils.stringToTimestamp(albumJson.getString("editedTime")));
                musicMapper.addMusic(music);
            }
        }

    }

    /**
     * 更新music基础信息
     * @author rakbow
     * @param id,music music的id和music */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusic(int id, Music music) throws Exception {
        try {
            musicMapper.updateMusic(id, music);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据id删除对应的music
     * @author rakbow
     * @param id id  */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusicById(int id) throws Exception {
        try {
            musicMapper.deleteMusicById(id);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据专辑id删除对应的music
     * @author rakbow
     * @param albumId 专辑id
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusicByAlbumId(int albumId) throws Exception {
        try {
            musicMapper.deleteMusicByAlbumId(albumId);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 修改状态
     * @author rakbow
     * @param id 音乐id
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateStatusById(int id) throws Exception {
        try {
            musicMapper.updateStatusById(id);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    //endregion

    //region ------处理数据------

    /**
     * 检测music数据
     * @author rakbow
     * @param musicJson musicJson
     * @return 错误信息
     * */
    public String checkMusicJson(JSONObject musicJson) {
        if (StringUtils.isBlank(musicJson.getString("name"))) {
            return ApiInfo.MUSIC_NAME_EMPTY;
        }
        if (StringUtils.isBlank(musicJson.getString("audioType"))) {
            return ApiInfo.MUSIC_AUDIO_TYPE_EMPTY;
        }
        if (StringUtils.isBlank(musicJson.getString("audioLength"))) {
            return ApiInfo.MUSIC_AUDIO_LENGTH_EMPTY;
        }
        return "";
    }

    /**
     * json转music
     * @author rakbow
     * @param musicJson musicJson
     * @return music
     * */
    public Music json2Music(@RequestBody JSONObject musicJson) {
        return JSON.to(Music.class, musicJson);
    }

    //endregion

    //region ------特殊查询------

    /**
     * 获取同属一张碟片的音频
     * @author rakbow
     * @param id 音乐id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MusicVOAlpha> getRelatedMusics(int id) {

        Music music = getMusicById(id);

        //获取同属一张专辑的音频
        List<Music> sameAlbumMusics = getMusicsByAlbumId(music.getAlbumId());

        if (sameAlbumMusics.size() >= 5) {
            sameAlbumMusics = sameAlbumMusics.subList(0, 5);
        }

        List<Music> tmpList = DataFinder.findMusicByDiscSerial(music.getDiscSerial(), sameAlbumMusics);
        for (int i = 0; i < tmpList.size(); i++) {
            if (tmpList.get(i).getId() == id) {
                tmpList.remove(tmpList.get(i));
            }
        }

        //筛选出同一张碟片的音频，并按照序号排序
        return musicVOMapper.music2VOAlpha(tmpList);
    }

    //endregion


    //region -----更新数据------

    /**
     * 更新创作人员信息
     * @author rakbow
     * @param id 音乐id
     * @param artists 创作人员信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicArtists(int id, String artists) {
        musicMapper.updateMusicArtists(id, artists, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新歌词文本
     * @author rakbow
     * @param id 音乐id
     * @param lrcText 歌词文本
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicLyricsText(int id, String lrcText) {
        musicMapper.updateMusicLyricsText(id, lrcText, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新描述信息
     * @author rakbow
     * @param id 音乐id
     * @param description 描述信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicDescription(int id, String description) {
        musicMapper.updateMusicDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新封面url
     * @author rakbow
     * @param id 音乐id
     * @param coverUrl 封面url
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicCoverUrl(int id, String coverUrl) {
        musicMapper.updateMusicCoverUrl(id, coverUrl);
    }

    //endregion

}
