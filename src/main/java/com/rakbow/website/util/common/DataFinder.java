package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Product;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 5:54
 * @Description: 数据查找
 */
public class DataFinder {

    //region album



    //endregion

    //region music

    /**
     * 根据musicId从指定music列表中查找
     *
     * @param id,musics 查找id和music列表
     * @return music
     * @author rakbow
     */
    public static Music findMusicById(int id, List<Music> musics) {
        Music musicFinder = new Music();
        musicFinder.setId(id);
        int idx = Collections.binarySearch(musics, musicFinder, DataSorter.musicSortById);
        if (idx >= 0) {
            return musics.get(idx);
        } else {
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
        if (StringUtils.equals(nameType, "nameJp")) {
            for (Music music : musics) {
                if (music.getAlbumId() == albumId && StringUtils.equals(music.getName(), name)) {
                    return music;
                }
            }
        } else {
            for (Music music : musics) {
                if (music.getAlbumId() == albumId && StringUtils.equals(music.getNameEn(), name)) {
                    return music;
                }
            }
        }

        return null;
    }

    //endregion

    //region product

    public static List<Product> findProductsByClassification(int classification, List<Product> products) {
        List<Product> result = new ArrayList<>();
        for (Product product : products) {
            if (product.getCategory() == classification) {
                if (!result.contains(product)) {
                    result.add(product);
                }
            }
        }
        return result;
    }

    /**
     * 根据jsonId从指定json列表中查找
     *
     * @param id,jsons 查找id和json列表
     * @return json
     * @author rakbow
     */
    public static JSONObject findJsonById(int id, List<JSONObject> jsons) {
        JSONObject json = new JSONObject();
        json.put("id", id);
        int idx = Collections.binarySearch(jsons, json, DataSorter.jsonSortById);
        if (idx >= 0) {
            return jsons.get(idx);
        } else {
            return null;
        }
    }

    /**
     * 根据jsonValue从指定json列表中查找
     *
     * @param id,jsons 查找id和json列表
     * @return json
     * @author rakbow
     */
    public static JSONObject findJsonByIdInSet(int id, List<JSONObject> jsons) {
        JSONObject json = new JSONObject();
        json.put("value", id);
        int idx = Collections.binarySearch(jsons, json, DataSorter.jsonSetSortByValue);
        if (idx >= 0) {
            return jsons.get(idx);
        } else {
            return null;
        }
    }

    //endregion

}
