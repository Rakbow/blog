package com.rakbow.website.data.emun.system;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-10 21:01
 * @Description:
 */
@AllArgsConstructor
public enum UserAuthority {

    ADMIN(0,"超级管理员", "Admin"),//未登录
    USER(1, "普通用户", "User"),
    JUNIOR_EDITOR(2, "初级管理员", "JuniorEditor"),
    SENIOR_EDITOR(3, "高级管理员", "SeniorEditor");

    @Getter
    private final int index;
    @Getter
    private final String nameZh;
    @Getter
    private final String nameEn;

}
