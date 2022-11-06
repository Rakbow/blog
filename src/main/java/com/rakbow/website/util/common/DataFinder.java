package com.rakbow.website.util.common;

import com.rakbow.website.entity.Music;
import org.elasticsearch.search.suggest.phrase.Correction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 5:54
 * @Description:
 */
public class DataFinder {

    public static Music findMusicById(int id, List<Music> musics) {
        Music musicFinder = new Music();
        musicFinder.setId(id);
        int idx = Collections.binarySearch(musics, musicFinder, DataSorter.musicSortById);
        if(idx >= 0){
            return musics.get(idx);
        }else {
            return null;
        }
    }

    public static List<Music> findMusicByDiscSerial(int discSerial, List<Music> musics) {
        List<Music> result = new ArrayList<>();
        for (Music music : musics) {
            if (music.getDiscSerial() == discSerial) {
                if (!result.contains(music)) {
                    result.add(music);
                }
            }
        }
        result.sort(DataSorter.musicSortByTrackSerial);
        return result;
    }

    public static Music findMusicByNameAndAlbumId(String name, String nameType, int albumId, List<Music> musics) {
        if (Objects.equals(nameType, "nameJp")) {
            for (Music music : musics) {
                if (music.getAlbumId() == albumId && Objects.equals(music.getNameJp(), name)) {
                    return music;
                }
            }
        } else {
            for (Music music : musics) {
                if (music.getAlbumId() == albumId && Objects.equals(music.getNameEn(), name)) {
                    return music;
                }
            }
        }

        return null;
    }
}
