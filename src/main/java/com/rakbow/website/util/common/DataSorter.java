package com.rakbow.website.util.common;

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