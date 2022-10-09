package com.rakbow.website.entity;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-07 8:00
 * @Description:
 */
public class Visit {

    private int entityType;//实体类型
    private int entityId;//实体id
    private long visitNum;//访问数

    public Visit(int entityType, int entityId) {
        this.entityType = entityType;
        this.entityId = entityId;
        this.visitNum = 0;
    }

    public int getEntityType() {
        return entityType;
    }

    public void setEntityType(int entityType) {
        this.entityType = entityType;
    }

    public int getEntityId() {
        return entityId;
    }

    public void setEntityId(int entityId) {
        this.entityId = entityId;
    }

    public long getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(long visitNum) {
        this.visitNum = visitNum;
    }

    public void increaseVisitNum() {
        this.visitNum++;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "entityType=" + entityType +
                ", entityId=" + entityId +
                ", visitNum=" + visitNum +
                '}';
    }
}
