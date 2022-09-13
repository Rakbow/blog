package com.rakbow.database.data;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-08-08 1:12
 * @Description:
 */
public enum InventoryStatus {

    INSTOCK(0,"库存正常"),
    LOWSTOCK(1,"库存低"),
    OUTOFSTOCK(2,"已售罄");

    private int index;
    private String status;

    InventoryStatus(int index, String status) {
        this.index = index;
        this.status = status;
    }

    public static String getStatus(int index){
        for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
            if (inventoryStatus.getIndex() == index) {
                return inventoryStatus.status;
            }
        }
        return "";
    }

    public static int getIndex(String status){
        for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
            if (inventoryStatus.getStatus().equals(inventoryStatus)) {
                return inventoryStatus.index;
            }
        }
        return -1;
    }

    public static List<String> getInventoryStatusList(){
        List<String> inventoryStatusList = new ArrayList<>();
        for (InventoryStatus inventoryStatus : InventoryStatus.values()) {
            inventoryStatusList.add(inventoryStatus.getStatus());
        }
        return inventoryStatusList;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
