package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;

import java.util.Comparator;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 3:39
 * @Description:
 */
public class DataSorter {

    public static AlbumSortById albumSortById = new AlbumSortById();
    public static MusicSortById musicSortById = new MusicSortById();
    public static MusicSortByTrackSerial musicSortByTrackSerial = new MusicSortByTrackSerial();
    public static JsonSortById jsonSortById = new JsonSortById();
    public static JsonSetSortByValue jsonSetSortByValue = new JsonSetSortByValue();
    // public static VisitSortByEntityId visitSortByEntityId = new VisitSortByEntityId();

}

class AlbumSortById implements Comparator<Album> {

    @Override
    public int compare(Album a, Album b) {
        return Integer.compare(a.getId(), b.getId());
    }
}

class MusicSortById implements Comparator<Music> {

    @Override
    public int compare(Music a, Music b) {
        return Integer.compare(a.getId(), b.getId());
    }
}

class MusicSortByTrackSerial implements Comparator<Music> {

    @Override
    public int compare(Music a, Music b) {
        return Integer.compare(Integer.parseInt(a.getTrackSerial()), Integer.parseInt(b.getTrackSerial()));
    }
}

class JsonSortById implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject a, JSONObject b) {
        return Integer.compare(a.getInteger("id"), b.getInteger("id"));
    }
}

class JsonSetSortByValue implements Comparator<JSONObject> {

    @Override
    public int compare(JSONObject a, JSONObject b) {
        return Integer.compare(a.getInteger("value"), b.getInteger("value"));
    }
}

// class VisitSortByEntityId implements Comparator<EntityInfo> {
//     @Override
//     public int compare(EntityInfo a, EntityInfo b) {
//         return Integer.compare(a.getEntityId(), b.getEntityId());
//     }
// }

