package com.rakbow.website.entity;

import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 8:00
 * @Description:
 */
@Data
public class Visit {

    private int entityType;//实体类型
    private int entityId;//实体id
    private long visitNum;//访问数

    public Visit(int entityType, int entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.visitNum = 0;
    }

    public void increaseVisitNum() {
        this.visitNum++;
    }
}
