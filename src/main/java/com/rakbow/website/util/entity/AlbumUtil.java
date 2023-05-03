package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.emun.entity.album.AlbumFormat;
import com.rakbow.website.entity.Music;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-19 23:44
 * @Description:
 */
public class AlbumUtil {

    /**
     * 获取可供编辑的editDiscList
     *
     * @param trackInfoJson 专辑的音轨信息json
     * @param allMusics     该专辑的曲目music列表
     * @return editDiscList
     * @author rakbow
     */
    public static JSONArray getEditDiscList(String trackInfoJson, List<Music> allMusics) {
        JSONArray editDiscList = new JSONArray();
        if (JSONObject.parseObject(trackInfoJson) != null
                && !Objects.equals(JSONObject.parseObject(trackInfoJson).toJSONString(), "{}")) {
            JSONArray tmpEditDiscList = JSONObject.parseObject(trackInfoJson).getJSONArray("discList");
            //临时ID，用于前端分辨碟片
            int tmpDiscId = 0;
            for (int i = 0; i < tmpEditDiscList.size(); i++) {
                JSONObject disc = tmpEditDiscList.getJSONObject(i);
                JSONArray trackList = disc.getJSONArray("trackList");
                JSONArray editTrackList = new JSONArray();
                //临时ID，用于前端分辨曲目
                int tmpTracId = 0;
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.getInteger(j);
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        JSONObject track = new JSONObject();
                        track.put("tmpDiscId", tmpDiscId);
                        track.put("tmpTrackId", tmpTracId);
                        track.put("musicId", musicId);
                        track.put("name", music.getName());
                        track.put("length", music.getAudioLength());
                        editTrackList.add(track);
                        tmpTracId++;
                    }
                }

                JSONArray tmpAlbumFormatList = new JSONArray();
                String[] tmpAlbumFormat = StringUtils.split(
                        AlbumFormat.getNamesByIds(disc.getJSONArray("albumFormat")), ",");
                tmpAlbumFormatList.addAll(Arrays.asList(tmpAlbumFormat));

                JSONArray tmpMediaFormatList = new JSONArray();
                String[] tmpMediaFormat = StringUtils.split(
                        MediaFormat.getNamesByIds(disc.getJSONArray("mediaFormat")), ",");
                tmpMediaFormatList.addAll(Arrays.asList(tmpMediaFormat));

                disc.put("tmpDiscId", tmpDiscId);
                disc.put("trackList", editTrackList);
                disc.put("albumFormat", tmpAlbumFormatList);
                disc.put("mediaFormat", tmpMediaFormatList);
                disc.remove("serial");
                disc.remove("catalogNo");
                disc.remove("discLength");
                editDiscList.add(disc);
                tmpDiscId++;
            }
        }

        return editDiscList;
    }

    /**
     * 对数据库中的trackInfo音轨信息进行封装
     *
     * @param trackInfoJson 专辑的音轨信息json
     * @param allMusics     该专辑的曲目music列表
     * @return finalTrackInfo
     * @author rakbow
     */
    public static JSONObject getFinalTrackInfo(String trackInfoJson, List<Music> allMusics) {
        JSONObject trackInfo = JSONObject.parseObject(trackInfoJson);
        List<String> times = new ArrayList<>();
        int totalTrack = 0;
        if (trackInfo != null && !Objects.equals(trackInfo.toJSONString(), "{}")) {
            JSONArray discList = trackInfo.getJSONArray("discList");
            JSONArray newDiscList = new JSONArray();
            for (int i = 0; i < discList.size(); i++) {
                List<String> _times = new ArrayList<>();
                JSONObject disc = discList.getJSONObject(i);
                JSONArray trackList = disc.getJSONArray("trackList");
                JSONArray newTrackList = new JSONArray();
                totalTrack += trackList.size();
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.getInteger(j);
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        JSONObject track = new JSONObject();
                        track.put("serial", music.getTrackSerial());
                        track.put("musicId", musicId);
                        track.put("name", music.getName());
                        track.put("nameEn", music.getNameEn());
                        track.put("length", music.getAudioLength());
                        String _time = track.get("length").toString();
                        if (_time.contains("\t")) {
                            _times.add(_time.replace("\t", ""));
                        } else {
                            _times.add(_time);
                        }
                        newTrackList.add(track);
                    }
                }
                times.addAll(_times);
                disc.put("trackList", newTrackList);
                disc.put("albumFormat", AlbumFormat.getNamesByIds(disc.getJSONArray("albumFormat")));
                disc.put("mediaFormat", MediaFormat.getNamesByIds(disc.getJSONArray("mediaFormat")));
                disc.put("discLength", CommonUtil.countTotalTime(_times));
                newDiscList.add(disc);
            }
            trackInfo.put("discList", newDiscList);
            trackInfo.put("totalLength", CommonUtil.countTotalTime(times));
            trackInfo.put("totalTracks", totalTrack);
        }

        return trackInfo;
    }

}
