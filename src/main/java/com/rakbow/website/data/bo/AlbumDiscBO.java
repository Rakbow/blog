package com.rakbow.website.data.bo;

import com.alibaba.fastjson2.JSONArray;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-03-28 17:11
 * @Description: 专辑-音轨增删改-业务对象
 */

@Data
public class AlbumDiscBO {

    private int serial;
    private JSONArray trackList;
    private JSONArray mediaFormat;
    private JSONArray albumFormat;

    public AlbumDiscBO() {
        serial = 0;
        trackList = new JSONArray();
        mediaFormat = new JSONArray();
        albumFormat = new JSONArray();
    }

}
