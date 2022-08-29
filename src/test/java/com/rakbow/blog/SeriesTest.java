package com.rakbow.blog;

import com.rakbow.blog.data.ProductClass;
import com.rakbow.blog.entity.Product;
import com.rakbow.blog.entity.Series;
import com.rakbow.blog.service.ProductService;
import com.rakbow.blog.service.SeriesService;
import com.rakbow.blog.util.CommonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.util.Date;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-20 2:35
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = BlogApplication.class)
public class SeriesTest {
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;

    @Test
    public void test() throws ParseException {
        Product product = new Product();
        product.setSeriesId(1);

        product.setNameZh("寒蝉鸣泣之时卒");
        product.setNameJp("ひぐらしのなく頃に卒");
        product.setNameEn("Higurashi: When They Cry - SOTSU");
        product.setClassification(ProductClass.ANIMATION.getIndex());
        product.setReleaseDate(CommonUtil.stringToDate("2021/07/02","yyyy/MM/dd"));
        product.setImgUrl("{\"\":\"\"}");
        //product.setImgUrl("{\"0\":\"/img/product/3-01.png\"}");

        product.setAddedTime(new Date());
        product.setEditedTime(new Date());
        product.setDescription("");
        product.setRemark("");
        productService.insertProduct(product);
    }

}
