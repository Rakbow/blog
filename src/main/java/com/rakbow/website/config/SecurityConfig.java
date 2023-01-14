package com.rakbow.website.config;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.CommonConstant;
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
                        "/user/setting",
                        "/user/upload"
                )
                .hasAnyAuthority(
                        CommonConstant.AUTHORITY_USER,
                        CommonConstant.AUTHORITY_ADMIN
                )
                .antMatchers(
                        // "/db/album/insert",
                        // "/db/album/update",
                        // "/db/album/delete",
                        // "/db/album/upload",
                        // "/db/album/updateAlbumArtists",
                        // "/db/album/updateAlbumTrackInfo"
                )
                .hasAnyAuthority(
                        CommonConstant.AUTHORITY_ADMIN
                )
                .anyRequest().permitAll()
                .and().csrf().disable();

        // 权限不够时的处理
        // 没有登录
        http.exceptionHandling()
                .authenticationEntryPoint((request, response, e) -> {
                    String xRequestedWith = request.getHeader("x-requested-with");
                    if ("XMLHttpRequest".equals(xRequestedWith)) {
                        response.setContentType("application/plain;charset=utf-8");
                        PrintWriter writer = response.getWriter();
                        writer.write(JSON.toJSONString(new ApiResult(0, ApiInfo.NOT_LOGIN)));
                    } else {
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
