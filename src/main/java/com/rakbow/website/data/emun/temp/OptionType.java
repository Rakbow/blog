package com.rakbow.website.data.emun.temp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-22 11:02
 * @Description:
 */
@AllArgsConstructor
public enum  OptionType {

    PERSON_ROLE(1, "", "");

    @Getter
    private final int id;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
