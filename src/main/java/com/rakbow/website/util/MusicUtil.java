package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.music.AudioType;
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
    public static List<JSONObject> getAudioTypeSet(){
        List<JSONObject> audioTypeSet = new ArrayList<>();
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

}
