package com.rakbow.website.data.emun.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-04-29 0:22
 * @Description:
 */
@AllArgsConstructor
public enum SystemLanguage {

    DEFAULT(0, "zh","default"),
    CHINESE(1, "zh","简体中文"),
    ENGLISH(2, "en","English"),
    JAPANESE(3, "ja", "日本語");

    @Getter
    private final int id;
    @Getter
    private final String code;
    @Getter
    private final String name;

}
