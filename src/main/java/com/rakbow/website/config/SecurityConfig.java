package com.rakbow.website.config;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.data.emun.system.UserAuthority;
import com.rakbow.website.util.common.CommonUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-27 0:12
 * @Description:
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) throws Exception{
        web.ignoring().antMatchers("/resources/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 授权
        http.authorizeRequests()
                .antMatchers(
                        //需要登陆权限
                        "/user/setting",
                        "/user/upload"
                )
                .hasAnyAuthority(
                        "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        "ROLE_" + UserAuthority.JUNIOR_EDITOR.getNameEn(),
                        "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn(),
                        "ROLE_" + UserAuthority.USER.getNameEn()
                )
                .antMatchers(
                        //region 需要初级编辑权限
                        "/db/album/add",
                        "/db/album/update",
                        "/db/album/update-artists",
                        "/db/album/update-description",
                        "/db/album/update-bonus",

                        "/db/book/add",
                        "/db/book/update",
                        "/db/book/update-authors",
                        "/db/book/update-description",
                        "/db/book/update-spec",
                        "/db/book/update-bonus",

                        "/db/disc/add",
                        "/db/disc/update",
                        "/db/disc/update-spec",
                        "/db/disc/update-description",
                        "/db/disc/update-bonus",

                        "/db/game/add",
                        "/db/game/update",
                        "/db/game/update-organizations",
                        "/db/game/update-staffs",
                        "/db/game/update-description",
                        "/db/game/update-bonus",

                        "/db/merch/add",
                        "/db/merch/update",
                        "/db/merch/update-spec",
                        "/db/merch/update-description",

                        "/db/music/update",
                        "/db/music/update-artists",
                        "/db/music/update-lyrics-text",
                        "/db/music/update-description"
                        //endregion
                )
                .hasAnyAuthority(
                        "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn(),
                        "ROLE_" + UserAuthority.JUNIOR_EDITOR.getNameEn()
                        )
                .antMatchers(
                        //region 需要高级编辑权限 如文件操作,更改上传文件(图片,音频)状态,删除文件等
                        "/db/album/add-images",
                        "/db/album/update-images",
                        "/db/album/update-trackInfo",
                        "/db/album/delete",

                        "/db/book/delete",
                        "/db/book/add-images",
                        "/db/book/update-images",

                        "/db/disc/delete",
                        "/db/disc/add-images",
                        "/db/disc/update-images",

                        "/db/game/delete",
                        "/db/game/add-images",
                        "/db/game/update-images",

                        "/db/merch/delete",
                        "/db/merch/add-images",
                        "/db/merch/update-images",

                        "/db/product/add",
                        "/db/product/update",
                        "/db/product/update-organizations",
                        "/db/product/update-description",
                        "/db/product/update-staffs",
                        "/db/product/add-images",
                        "/db/product/update-images",

                        "/db/franchise/add",
                        "/db/franchise/update",
                        "/db/franchise/update-description",
                        "/db/franchise/add-images",
                        "/db/franchise/update-images",

                        "/db/music/upload-file",
                        "/db/music/delete-file-file"
                        //endregion
                )
                .hasAnyAuthority(
                        "ROLE_" + UserAuthority.ADMIN.getNameEn(),
                        "ROLE_" + UserAuthority.SENIOR_EDITOR.getNameEn()
                )
                .antMatchers(
                        //超级管理员权限
                )
                .hasAnyAuthority(
                        "ROLE_" + UserAuthority.ADMIN.getNameEn()
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        // 权限不够时的处理
        // 没有登录
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {

                    String xRequestedWith = request.getHeader("x-requested-with");
                    //识别请求为同步请求还是异步请求
                    if ("XMLHttpRequest".equals(xRequestedWith)) {//异步请求
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(JSON.toJSONString(new ApiResult(0, ApiInfo.NOT_LOGIN)));
                    } else {//同步请求
                        response.sendRedirect("/login");
                    }
                })
                .accessDeniedHandler(new AccessDeniedHandler() {
                    // 权限不足
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException e) throws IOException, ServletException {

                        String xRequestedWith = request.getHeader("x-requested-with");
                        if ("XMLHttpRequest".equals(xRequestedWith)) {
                            response.setContentType("application/plain;charset=utf-8");
                            PrintWriter writer = response.getWriter();
                            writer.write(JSON.toJSONString(new ApiResult(0, ApiInfo.NOT_AUTHORITY)));
                        } else {
                            response.sendRedirect("/denied");
                        }
                    }
                });

        // Security底层默认会拦截/logout请求,进行退出处理.
        // 覆盖它默认的逻辑,才能执行我们自己的退出代码.
        http.logout().logoutUrl("/securitylogout");
    }

}
