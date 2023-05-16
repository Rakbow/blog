package com.rakbow.website.data.bo;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rakbow.website.util.common.JsonUtil;
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
    private ArrayNode trackList;
    private ArrayNode mediaFormat;
    private ArrayNode albumFormat;

    public AlbumDiscBO() {
        serial = 0;
        trackList = JsonUtil.emptyArrayNode();
        mediaFormat = JsonUtil.emptyArrayNode();
        albumFormat = JsonUtil.emptyArrayNode();
    }

}
