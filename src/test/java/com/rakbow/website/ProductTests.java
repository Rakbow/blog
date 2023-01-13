package com.rakbow.website;

import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.service.VisitService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-26 20:25
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class ProductTests {

    @Autowired
    private ProductService productService;
    @Autowired
    private VisitService visitService;

    @Test
    public void addVisitData () {
        productService.getAllProduct().forEach(product -> {
            visitService.insertVisit(new Visit(EntityType.PRODUCT.getId(), product.getId()));
        });
    }

}
