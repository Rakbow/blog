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
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

    //reg|ion ------引入实例------
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private VisitService visitService;
    @Autowired
    private AlbumService albumservice;
    //endregion

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Music selectMusicById(int id) {
        return musicMapper.selectMusicById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Music> getAll() {
        return musicMapper.getAll();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Music> selectMusicsByAlbumId(int albumId) {
        return musicMapper.selectMusicsByAlbumId(albumId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public int selectMusicRows() {
        return musicMapper.selectMusicRows();
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String insertMusic(Music music) throws Exception {
        try {

            //新增访问量实体
            visitService.insertVisit(new Visit(EntityType.MUSIC.getId(), music.getId()));

            musicMapper.insertMusic(music);
            return String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.MUSIC.getName());
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void insertMusicsFromAlbum(JSONObject albumJson){

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
                music.setNameJp(track.getString("name"));
                music.setAlbumId(albumId);
                music.setDiscSerial(disc.getInteger("serial"));
                music.setTrackSerial(track.getString("serial"));
                music.setCoverUrl(cover.getString("url"));
                music.setAudioLength(track.getString("audioLength"));
                music.setAddedTime(CommonUtil.stringToTimestamp(albumJson.getString("addedTime")));
                music.setEditedTime(CommonUtil.stringToTimestamp(albumJson.getString("editedTime")));
                musicMapper.insertMusic(music);
            }
        }

    }

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

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateStatusById(int id) throws Exception {
        try {
            musicMapper.updateStatusById(id);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    //music对象转json
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public JSONObject music2Json(Music music) {
        JSONObject musicJson = (JSONObject) JSON.toJSON(music);

        JSONObject audioTypeObj = new JSONObject();
        audioTypeObj.put("id", music.getAudioType());
        audioTypeObj.put("nameJp", AudioType.getNameByIndex(music.getAudioType()));
        audioTypeObj.put("nameEn", AudioType.getNameEnByIndex(music.getAudioType()));

        //音乐创作
        JSONArray artists = JSON.parseArray(music.getArtists());

        //所属专辑信息
        JSONObject relatedAlbum = albumservice.album2JsonSimple(albumservice.findAlbumById(music.getAlbumId()));

        musicJson.put("audioTypeObj", audioTypeObj);
        musicJson.put("addedTime", CommonUtil.timestampToString(music.getAddedTime()));
        musicJson.put("editedTime", CommonUtil.timestampToString(music.getEditedTime()));
        musicJson.put("relatedAlbum", relatedAlbum);
        musicJson.put("artists", artists);

        return musicJson;
    }

    //music对象转json（极简）
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public JSONObject music2JsonSimple(Music music) {
        JSONObject musicJson = new JSONObject();

        musicJson.put("id", music.getId());
        musicJson.put("nameJp", music.getNameJp());
        musicJson.put("nameEb", music.getNameEn());
        musicJson.put("audioLength", music.getAudioLength());
        musicJson.put("discSerial", music.getDiscSerial());
        musicJson.put("trackSerial", music.getTrackSerial());

        return musicJson;
    }

    //获取同属一张碟片的音频findMusicByDiscSerial
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedMusics(Music music) {
        List<JSONObject> relatedMusics = new ArrayList<>();

        //获取同属一张专辑的音频
        List<Music> sameAlbumMusics = selectMusicsByAlbumId(music.getAlbumId());
        //筛选出同一张碟片的音频，并按照序号排序
        DataFinder.findMusicByDiscSerial(music.getDiscSerial(), sameAlbumMusics)
                .forEach(m -> relatedMusics.add(music2JsonSimple(m)));

        return relatedMusics;
    }

    //更新创作人员信息
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicArtists(int id, String artists) {
        musicMapper.updateMusicArtists(id, artists, new Timestamp(System.currentTimeMillis()));
    }

    //更新歌词文本
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicLyricsText(int id, String lrcText) {
        musicMapper.updateMusicLyricsText(id, lrcText, new Timestamp(System.currentTimeMillis()));
    }

    //更新描述信息
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateMusicDescription(int id, String description) {
        musicMapper.updateMusicDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

}
