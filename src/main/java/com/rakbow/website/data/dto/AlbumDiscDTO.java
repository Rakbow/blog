package com.rakbow.website.data.dto;

import com.alibaba.fastjson2.JSONArray;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.rakbow.website.util.common.JsonUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-03-28 16:11
 * @Description: 专辑-音轨增删改-中间对象
 */
@Data
public class AlbumDiscDTO {

    private List<AlbumTrackDTO> trackList;
    private ArrayNode mediaFormat;
    private ArrayNode albumFormat;

    public AlbumDiscDTO() {
        trackList = new ArrayList<>();
        mediaFormat = JsonUtil.emptyArrayNode();
        albumFormat = JsonUtil.emptyArrayNode();
    }

}
