package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-10-15 19:18
 * @Description:
 */
@Controller
@RequestMapping(path = "/db/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    //region ------获取页面------
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getProductListPage(Model model) {
        model.addAttribute("products", productService.product2json(productService.getAllProduct()));
        return "/product/product-list";
    }
    //endregion

    /**
     * 根据系列id获取该系列所有产品
     * */
    @RequestMapping(path = "/get-products-by-series-id", method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> getAllProductBySeriesId(@RequestBody String json){
        JSONObject param = JSON.parseObject(json);
        return productService.getAllProductSetBySeriesId(param.getInteger("series"));
    }

}
