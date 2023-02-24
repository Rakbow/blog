package com.rakbow.website.data.common;

import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-22 20:06
 * @Description:
 */
@Data
public class Collect {

    private int entityType;
    private int entityId;
    private long collectCount;

}
