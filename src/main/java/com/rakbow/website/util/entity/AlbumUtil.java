package com.rakbow.website.util.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.emun.entity.album.AlbumFormat;
import com.rakbow.website.entity.Music;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.common.JsonUtil;
import net.minidev.json.JSONUtil;
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
    public static ArrayNode getEditDiscList(String trackInfoJson, List<Music> allMusics) {
        ArrayNode editDiscList = JsonUtil.emptyArrayNode();
        ObjectNode trackInfo = JsonUtil.toObjectNode(trackInfoJson);
        if (trackInfo != null && !StringUtils.equals(trackInfoJson, "{}")) {
            ArrayNode tmpEditDiscList = (ArrayNode) trackInfo.get("discList");
            //临时ID，用于前端分辨碟片
            int tmpDiscId = 0;
            for (int i = 0; i < tmpEditDiscList.size(); i++) {
                ObjectNode disc = (ObjectNode)tmpEditDiscList.get(i);
                JsonNode trackList = disc.get("trackList");
                ArrayNode editTrackList = JsonUtil.emptyArrayNode();
                //临时ID，用于前端分辨曲目
                int tmpTrackId = 0;
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.get(j).asInt();
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        ObjectNode track = JsonUtil.emptyObjectNode();
                        track.put("tmpDiscId", tmpDiscId);
                        track.put("tmpTrackId", tmpTrackId);
                        track.put("musicId", musicId);
                        track.put("name", music.getName());
                        track.put("length", music.getAudioLength());
                        editTrackList.add(track);
                        tmpTrackId++;
                    }
                }

                ArrayNode tmpAlbumFormatList = JsonUtil.emptyArrayNode();
                String[] tmpAlbumFormat = StringUtils.split(
                        AlbumFormat.getNamesByIds(JsonUtil.toList(disc.get("albumFormat"), Integer.class)), ",");
                tmpAlbumFormatList.addAll(JsonUtil.toArrayNode(tmpAlbumFormat));

                ArrayNode tmpMediaFormatList = JsonUtil.emptyArrayNode();
                String[] tmpMediaFormat = StringUtils.split(
                        MediaFormat.getNamesByIds(JsonUtil.toList(disc.get("mediaFormat"), Integer.class)), ",");
                tmpMediaFormatList.addAll(JsonUtil.toArrayNode(tmpMediaFormat));

                disc.put("tmpDiscId", tmpDiscId);
                disc.set("trackList", editTrackList);
                disc.set("albumFormat", tmpAlbumFormatList);
                disc.set("mediaFormat", tmpMediaFormatList);
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
    public static ObjectNode getFinalTrackInfo(String trackInfoJson, List<Music> allMusics) {
        ObjectNode trackInfo = JsonUtil.toObjectNode(trackInfoJson);
        List<String> times = new ArrayList<>();
        int totalTrack = 0;
        if (trackInfo != null && !StringUtils.equals(trackInfoJson, "{}")) {
            JsonNode discList = trackInfo.get("discList");
            ArrayNode newDiscList = JsonUtil.emptyArrayNode();
            for (int i = 0; i < discList.size(); i++) {
                List<String> _times = new ArrayList<>();
                ObjectNode disc = (ObjectNode) discList.get(i);
                ArrayNode trackList = (ArrayNode) disc.get("trackList");
                ArrayNode newTrackList = JsonUtil.emptyArrayNode();
                totalTrack += trackList.size();
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.get(j).asInt();
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        ObjectNode track = JsonUtil.emptyObjectNode();
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
                disc.set("trackList", newTrackList);
                disc.put("albumFormat", AlbumFormat.getNamesByIds(JsonUtil.toList(disc.get("albumFormat"), Integer.class)));
                disc.put("mediaFormat", MediaFormat.getNamesByIds(JsonUtil.toList(disc.get("mediaFormat"), Integer.class)));
                disc.put("discLength", CommonUtil.countTotalTime(_times));
                newDiscList.add(disc);
            }
            trackInfo.set("discList", newDiscList);
            trackInfo.put("totalLength", CommonUtil.countTotalTime(times));
            trackInfo.put("totalTracks", totalTrack);
        }

        return trackInfo;
    }

}
