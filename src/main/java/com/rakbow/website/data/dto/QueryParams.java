package com.rakbow.website.data.dto;

import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.rakbow.website.util.common.JsonUtil;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-04-06 10:34
 * @Description:
 */
@Data
public class QueryParams {

    private int first;
    private int rows;
    private String sortField;
    private int sortOrder;
    private JsonNode filters;

    public QueryParams() {
        first = 0;
        rows = 0;
        sortField = "";
        sortOrder = 0;
        filters = JsonUtil.emptyObjectNode();
    }

}
