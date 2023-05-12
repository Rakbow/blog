package com.rakbow.website.controller.interceptor;

import org.springframework.web.servlet.i18n.CookieLocaleResolver;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-04-29 21:54
 * @Description: get locate from cookie
 */
public class LocaleResolver extends CookieLocaleResolver {

    //重写构造方法,改变cookie信息
    public LocaleResolver(){
        this.setCookieName("locale");
        //cookie有效期30天
        this.setCookieMaxAge(30*24*60*60);
    }

}
