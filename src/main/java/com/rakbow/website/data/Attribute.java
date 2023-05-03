package com.rakbow.website.data;

import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-04-30 18:40
 * @Description:
 */
@Data
public class Attribute {

    private int value;
    private String label;

    public Attribute() {

    }

    public Attribute(int value, String label) {
        this.value = value;
        this.label = label;
    }

}
