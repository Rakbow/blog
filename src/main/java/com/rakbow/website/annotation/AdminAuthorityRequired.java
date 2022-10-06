package com.rakbow.website.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-30 9:49
 * @Description: 该注解表示该操作需要管理员权限
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AdminAuthorityRequired {
}
