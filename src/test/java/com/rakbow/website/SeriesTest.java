package com.rakbow.website;

import com.rakbow.website.data.ProductClass;
import com.rakbow.website.entity.Product;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.service.SeriesService;
import com.rakbow.website.util.common.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-20 2:35
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class SeriesTest {
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;

}
