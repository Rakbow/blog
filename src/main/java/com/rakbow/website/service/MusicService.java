package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.music.AudioType;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 2:04
 * @Description:
 */
@Service
public class MusicService {

    //region ------引入实例------
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private VisitService visitService;
    //endregion

    /**
     * 根据音乐id获取音乐
     * @author rakbow
     * @param id 音乐id
     * @return music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Music getMusicById(int id) {
        return musicMapper.getMusicById(id);
    }

    /**
     * 获取所有音乐数据
     * @author rakbow
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Music> getAll() {
        return musicMapper.getAll();
    }

    /**
     * 根据专辑id获取该专辑所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Music> getMusicsByAlbumId(int albumId) {
        return musicMapper.getMusicsByAlbumId(albumId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public int getMusicRows() {
        return musicMapper.getMusicRows();
    }

    /**
     * 新增music
     * @param music
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addMusic(Music music) throws Exception {
        try {
            //若封面url为空，赋404图
            if (StringUtils.isBlank(music.getCoverUrl())) {
                music.setCoverUrl(CommonConstant.EMPTY_IMAGE_URL);
            }

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
     * @param albumJson
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
                music.setAddedTime(CommonUtil.stringToTimestamp(albumJson.getString("addedTime")));
                music.setEditedTime(CommonUtil.stringToTimestamp(albumJson.getString("editedTime")));
                musicMapper.addMusic(music);
            }
        }

    }

    /**
     * 更新music基础信息
     * @author rakbow
     * @param id,music music的id和music
     * @return apiInfo
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String updateMusic(int id, Music music) throws Exception {
        try {
            musicMapper.updateMusic(id, music);
            return String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.MUSIC.getName());
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteMusicById(int id) throws Exception {
        try {
            musicMapper.deleteMusicById(id);
            return String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.MUSIC.getName());
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据专辑id删除对应的music
     * @author rakbow
     * @param albumId 专辑id
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateStatusById(int id) throws Exception {
        try {
            musicMapper.updateStatusById(id);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * music对象转json
     * @author rakbow
     * @param music 音乐
     * @return JSONObject
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public JSONObject music2Json(Music music) {
        JSONObject musicJson = (JSONObject) JSON.toJSON(music);

        JSONObject audioTypeObj = new JSONObject();
        audioTypeObj.put("id", music.getAudioType());
        audioTypeObj.put("name", AudioType.getNameByIndex(music.getAudioType()));
        audioTypeObj.put("nameEn", AudioType.getNameEnByIndex(music.getAudioType()));

        //音乐创作
        JSONArray artists = JSON.parseArray(music.getArtists());

        musicJson.put("audioTypeObj", audioTypeObj);
        musicJson.put("addedTime", CommonUtil.timestampToString(music.getAddedTime()));
        musicJson.put("editedTime", CommonUtil.timestampToString(music.getEditedTime()));
        musicJson.put("artists", artists);

        return musicJson;
    }

    /**
     * music对象转json（极简）
     * @author rakbow
     * @param music 音乐
     * @return JSONObject
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public JSONObject music2JsonSimple(Music music) {
        JSONObject musicJson = new JSONObject();

        musicJson.put("id", music.getId());
        musicJson.put("name", music.getName());
        musicJson.put("nameEb", music.getNameEn());
        musicJson.put("coverUrl", music.getCoverUrl());
        musicJson.put("audioLength", music.getAudioLength());
        musicJson.put("discSerial", music.getDiscSerial());
        musicJson.put("trackSerial", music.getTrackSerial());

        return musicJson;
    }

    /**
     * 检测music数据
     * @author rakbow
     * @param musicJson
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
     * @param musicJson
     * @return music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Music json2Music(@RequestBody JSONObject musicJson) {
        return musicJson.toJavaObject(Music.class);
    }

    /**
     * 获取同属一张碟片的音频
     * @author rakbow
     * @param id 音乐id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedMusics(int id) {
        List<JSONObject> relatedMusics = new ArrayList<>();

        Music music = getMusicById(id);

        //获取同属一张专辑的音频
        List<Music> sameAlbumMusics = getMusicsByAlbumId(music.getAlbumId());
        //筛选出同一张碟片的音频，并按照序号排序
        DataFinder.findMusicByDiscSerial(music.getDiscSerial(), sameAlbumMusics)
                .forEach(m -> relatedMusics.add(music2JsonSimple(m)));

        return relatedMusics;
    }

    /**
     * 更新创作人员信息
     * @author rakbow
     * @param id 音乐id
     * @param artists 创作人员信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicArtists(int id, String artists) {
        musicMapper.updateMusicArtists(id, artists, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新歌词文本
     * @author rakbow
     * @param id 音乐id
     * @param lrcText 歌词文本
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicLyricsText(int id, String lrcText) {
        musicMapper.updateMusicLyricsText(id, lrcText, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新描述信息
     * @author rakbow
     * @param id 音乐id
     * @param description 描述信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicDescription(int id, String description) {
        musicMapper.updateMusicDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新封面url
     * @author rakbow
     * @param id 音乐id
     * @param coverUrl 封面url
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicCoverUrl(int id, String coverUrl) {
        musicMapper.updateMusicCoverUrl(id, coverUrl);
    }

}
