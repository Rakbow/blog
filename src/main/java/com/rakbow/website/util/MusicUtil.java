package com.rakbow.website.util;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.music.AudioType;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 21:06
 * @Description:
 */
public class MusicUtil {

    //获取专辑分类数组
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

}
