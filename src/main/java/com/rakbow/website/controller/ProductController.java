package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.DataActionType;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Product;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.ProductUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
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

    //region ------注入实例------

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private UserService userService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private HostHolder hostHolder;
    @Value("${website.path.img}")
    private String imgPath;
    //endregion

    //region ------获取页面------
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getProductListPage(Model model) {
        model.addAttribute("products", productService.product2json(productService.getAllProduct()));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("productClassSet", ProductUtils.getProductClassSet());
        return "/product/product-list";
    }

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getProductDetail(@PathVariable("id") int productId, Model model) throws IOException {
        if (productService.getProductById(productId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.PRODUCT.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.PRODUCT.getId(), productId);

        Product product = productService.getProductById(productId);
        model.addAttribute("product", productService.product2Json(product));
        model.addAttribute("user", hostHolder.getUser());
        model.addAttribute("productClassSet", ProductUtils.getProductClassSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbumsByProductId(productId));
        model.addAttribute("relatedProducts", productService.getRelatedProducts(productId));
        //获取页面访问量
        model.addAttribute("visitNum",
                visitService.getVisit(EntityType.PRODUCT.getId(), productId).getVisitNum());

        return "/product/product-detail";
    }
    //endregion

    //region ------基础增删改查------

    //新增作品
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addProduct(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(productService.checkProductJson(param))) {
                    res.setErrorMessage(productService.checkProductJson(param));
                    return JSON.toJSONString(res);
                }

                Product product = productService.json2Product(param);

                //保存新增专辑
                productService.addProduct(product);

                // //将新增的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.PRODUCT.getId(), product.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.PRODUCT.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新作品基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(productService.checkProductJson(param))) {
                    res.setErrorMessage(productService.checkProductJson(param));
                    return JSON.toJSONString(res);
                }

                Product product = productService.json2Product(param);

                //修改编辑时间
                product.setEditedTime(new Timestamp(System.currentTimeMillis()));

                productService.updateProduct(product.getId(), product);

                //将更新的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(product);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.PRODUCT.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                productService.updateProductDescription(id, description);
                res.message = ApiInfo.UPDATE_PRODUCT_DESCRIPTION_SUCCESS;
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
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

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addProductImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                JSONArray imagesJson = JSON.parseArray(productService.getProductById(id).getImages());
                JSONArray imageInfosTmp = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonUtils.checkAddImages(imageInfosTmp, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                //创建存储图片的文件夹
                Path productImgPath = Paths.get(imgPath + "/product/" + id);

                //存储图片链接的json
                JSONArray imgJson = new JSONArray();

                if (Files.notExists(productImgPath)) {
                    try {
                        Files.createDirectory(productImgPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < images.length; i++) {
                    //获取json中的jo对象
                    JSONObject imageInfo = imageInfosTmp.getJSONObject(i);

                    String fileName = images[i].getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf("."));
                    if (StringUtils.isBlank(suffix)) {
                        res.setErrorMessage(ApiInfo.INCORRECT_FILE_FORMAT);
                        return JSON.toJSONString(res);
                    }
                    fileName = (imageInfo.getString("nameEn") + suffix).replaceAll(" ", "");
                    // 确定文件存放的路径
                    File dest = new File(productImgPath + "/" + fileName);
                    try {
                        // 存储文件
                        images[i].transferTo(dest);
                    } catch (IOException e) {
                        logger.error("上传文件失败: " + e.getMessage());
                        // throw new RuntimeException("上传文件失败,服务器发生异常!", e);
                        res.setErrorMessage(ApiInfo.UPLOAD_EXCEPTION);
                        return JSON.toJSONString(res);
                    }

                    //将数据存至数据库
                    JSONObject jo = new JSONObject();
                    jo.put("url", "/db/product/" + id + "/" + fileName);
                    jo.put("nameEn", imageInfo.getString("nameEn"));
                    jo.put("nameZh", imageInfo.getString("nameZh"));
                    jo.put("type", imageInfo.getString("type"));
                    jo.put("uploadTime", CommonUtils.getCurrentTime());
                    if (imageInfo.getString("description") == null) {
                        jo.put("description", "");
                    }
                    imgJson.add(jo);
                }

                imagesJson.addAll(imgJson);
                productService.addProductImages(id, imagesJson.toJSONString());

                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取id
                int id = JSON.parseObject(json).getInteger("id");
                JSONArray images = JSON.parseObject(json).getJSONArray("images");
                for (int i = 0; i < images.size(); i++) {
                    images.getJSONObject(i).remove("thumbUrl");
                }

                //更改图片
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.id) {

                    //检测是否存在多张封面
                    String errorMessage = CommonUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = productService.updateProductImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.id) {
                    res.message = productService.deleteProductImages(id, images);
                }else {
                    res.setErrorMessage(ApiInfo.NOT_ACTION);
                }

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

}
