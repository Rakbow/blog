package com.rakbow.blog.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-29 23:33
 * @Description: 该注解表示需要用户权限为管理员
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface RootRequired {
}
