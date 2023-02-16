package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.album.AlbumVOBeta;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.disc.DiscVOBeta;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.data.vo.game.GameVOBeta;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOBeta;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;

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
    public static VisitSortByEntityId visitSortByEntityId = new VisitSortByEntityId();

    public static AlbumSortByVisitNum albumSortByVisitNum = new AlbumSortByVisitNum();
    public static BookSortByVisitNum bookSortByVisitNum = new BookSortByVisitNum();
    public static DiscSortByVisitNum discSortByVisitNum = new DiscSortByVisitNum();
    public static GameSortByVisitNum gameSortByVisitNum = new GameSortByVisitNum();
    public static MerchSortByVisitNum merchSortByVisitNum = new MerchSortByVisitNum();

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

class VisitSortByEntityId implements Comparator<Visit> {
    @Override
    public int compare(Visit a, Visit b) {
        return Integer.compare(a.getEntityId(), b.getEntityId());
    }
}

class AlbumSortByVisitNum implements Comparator<AlbumVOAlpha> {
    @Override
    public int compare(AlbumVOAlpha a, AlbumVOAlpha b) {
        return Long.compare(a.getVisitNum(), b.getVisitNum());
    }
}

class BookSortByVisitNum implements Comparator<BookVOBeta> {
    @Override
    public int compare(BookVOBeta a, BookVOBeta b) {
        return Long.compare(a.getVisitNum(), b.getVisitNum());
    }
}

class DiscSortByVisitNum implements Comparator<DiscVOAlpha> {
    @Override
    public int compare(DiscVOAlpha a, DiscVOAlpha b) {
        return Long.compare(a.getVisitNum(), b.getVisitNum());
    }
}

class GameSortByVisitNum implements Comparator<GameVOAlpha> {
    @Override
    public int compare(GameVOAlpha a, GameVOAlpha b) {
        return Long.compare(a.getVisitNum(), b.getVisitNum());
    }
}

class MerchSortByVisitNum implements Comparator<MerchVOAlpha> {
    @Override
    public int compare(MerchVOAlpha a, MerchVOAlpha b) {
        return Long.compare(a.getVisitNum(), b.getVisitNum());
    }
}

