package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.music.AudioType;
import com.rakbow.website.data.vo.music.MusicVO;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.MusicService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 21:06
 * @Description:
 */
public class MusicUtil {

    @Autowired
    private static MusicService musicService;

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
     * @param musicVO 音乐
     * */
    public static JSONObject getMusicAudioInfo(MusicVO musicVO) {
        JSONArray files = musicVO.getFiles();
        if (files.size() == 0) {
            return null;
        }
        JSONObject audioInfo = new JSONObject();
        for (int i = 0; i < files.size(); i++) {
            //判断是否有音频文件
            if (files.getJSONObject(i).getString("type").contains("audio")) {
                audioInfo.put("name", musicVO.getName());
                audioInfo.put("artist", "artist");
                audioInfo.put("url", files.getJSONObject(i).getString("url"));
                audioInfo.put("cover", musicVO.getCover());
            }
            //判断是否有歌词文件
            if (files.getJSONObject(i).getString("type").contains("text")) {
                audioInfo.put("lrc", files.getJSONObject(i).getString("url"));
            }
        }
        return audioInfo;
    }

}
