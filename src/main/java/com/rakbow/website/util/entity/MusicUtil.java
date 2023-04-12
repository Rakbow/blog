package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.data.emun.music.AudioType;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 21:06
 * @Description:
 */
public class MusicUtil {

    @Resource
    private static MusicService musicService;

    private static final String[] VOCAL_LIST = new String[] {"vocal", "vocals", "演唱"};

    /**
     * 获取音乐分类数组
     * @author rakbow
     * @return list
     * */
    public static JSONArray getAudioTypeSet(){
        JSONArray audioTypeSet = new JSONArray();
        for(AudioType audioType : AudioType.values()){
            JSONObject audioTypeJson = new JSONObject();
            audioTypeJson.put("label",audioType.getName());
            audioTypeJson.put("labelEn",audioType.getNameEn());
            audioTypeJson.put("value",audioType.getIndex());
            audioTypeSet.add(audioTypeJson);
        }
        return audioTypeSet;
    }

    /**
     * 根据专辑id删除该专辑对应所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * */
    public static void deleteAllMusicByAlbumId(int albumId) throws Exception {
        musicService.deleteMusicByAlbumId(albumId);
    }

    /**
     * 获取音频信息
     * @author rakbow
     * @param music 音乐
     * */
    public static JSONObject getMusicAudioInfo(Music music, String coverUrl) {
        JSONArray files = JSON.parseArray(music.getFiles());
        if (files.size() == 0) {
            return null;
        }
        JSONObject audioInfo = new JSONObject();
        for (int i = 0; i < files.size(); i++) {
            //判断是否有音频文件
            if (files.getJSONObject(i).getString("type").contains("audio")) {
                audioInfo.put("name", music.getName());
                audioInfo.put("artist", getArtists(music));
                audioInfo.put("url", files.getJSONObject(i).getString("url"));
                audioInfo.put("cover", QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80));
                if (StringUtils.isBlank(coverUrl)) {
                    audioInfo.put("cover", QiniuImageUtil.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 80));
                }else {
                    audioInfo.put("cover", QiniuImageUtil.getThumbUrl(coverUrl, 80));
                }
            }
            //判断是否有歌词文件
            if (files.getJSONObject(i).getString("type").contains("text")) {
                audioInfo.put("lrc", files.getJSONObject(i).getString("url"));
            }
        }
        return audioInfo;
    }

    /**
     * 获取音频信息
     * @author rakbow
     * @param musics 音乐
     * */
    public static JSONArray getMusicAudioInfo(List<Music> musics, String coverUrl) {
        if (musics.size() == 0) {
            return null;
        }
        JSONArray audioInfos = new JSONArray();
        musics.forEach(music -> {
            JSONObject audioInfo = getMusicAudioInfo(music, coverUrl);
            if (audioInfo != null) {
                audioInfos.add(audioInfo);
            }
        });
        if (audioInfos.size() == 0) {
            return null;
        }
        return audioInfos;
    }

    /**
     * 获取音频信息中的演唱者
     * @author rakbow
     * @param music 音乐
     * */
    public static String getArtists(Music music) {
        JSONArray artists = JSON.parseArray(music.getArtists());
        if (artists.size() == 0) {
            return "unknown";
        }
        for (int i = 0; i < artists.size(); i++) {
            for (int j = 0; j < VOCAL_LIST.length; j++) {
                if (StringUtils.equals(artists.getJSONObject(i).getString("pos"), VOCAL_LIST[j])) {
                    List<String> vocals = artists.getJSONObject(i).getList("name", String.class);
                    return String.join("/", vocals);
                }
            }
        }
        return "unknown";
    }

}
