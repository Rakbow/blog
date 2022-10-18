package com.rakbow.website;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-17 23:34
 * @Description:
 */
public class WebsiteServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(WebSiteApplication.class);
    }

}
