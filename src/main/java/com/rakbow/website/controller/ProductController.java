package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.data.vo.product.ProductVOAlpha;
import com.rakbow.website.entity.Product;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.convertMapper.ProductVOMapper;
import com.rakbow.website.util.file.CommonImageUtils;
import com.rakbow.website.util.common.RedisUtil;
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
    private UserService userService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private BookService bookService;
    @Autowired
    private DiscService discService;
    @Autowired
    private GameService gameService;
    @Autowired
    private HostHolder hostHolder;
    @Value("${website.path.img}")
    private String imgPath;
    @Autowired
    private RedisUtil redisUtil;

    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
    //endregion

    //region ------获取页面------
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getProductListPage(Model model) {
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("productCategorySet", redisUtil.get("ProductCategorySet"));
        return "/product/product-list";
    }

    //获取单个产品详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getProductDetail(@PathVariable("id") int productId, Model model) {
        if (productService.getProduct(productId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.PRODUCT.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.PRODUCT.getId(), productId);

        Product product = productService.getProduct(productId);
        model.addAttribute("product", productVOMapper.product2VO(product));
        model.addAttribute("user", hostHolder.getUser());
        model.addAttribute("productCategorySet", redisUtil.get("ProductCategorySet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("regionSet", redisUtil.get("regionSet"));
        model.addAttribute("platformSet", redisUtil.get("platformSet"));
        model.addAttribute("relatedProducts", productService.getRelatedProducts(productId));

        if (product.getCategory() == ProductCategory.ANIMATION.getIndex()
                || product.getCategory() == ProductCategory.LIVE_ACTION_MOVIE.getIndex()) {
            model.addAttribute("albums", albumService.getAlbumsByProductId(productId));
            model.addAttribute("discs", discService.getDiscsByProductId(productId));
        }
        if (product.getCategory() == ProductCategory.BOOK.getIndex()) {
            model.addAttribute("books", bookService.getBooksByProductId(productId));
        }
        if (product.getCategory() == ProductCategory.GAME.getIndex()) {
            model.addAttribute("albums", albumService.getAlbumsByProductId(productId));
            model.addAttribute("games", gameService.getGamesByProductId(productId));
        }

        //获取页面数据
        model.addAttribute("pageInfo",
                visitService.getPageInfo(EntityType.PRODUCT.getId(), productId, product.getAddedTime(), product.getEditedTime()));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getMetaDetailInfo(product, EntityType.PRODUCT.getId()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtils.segmentImages(product.getImages(), 200, true));

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

                //刷新redis缓存
                productService.refreshRedisProducts();

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
    public String updateProduct(@RequestBody String json, HttpServletRequest request) {
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

                //刷新redis缓存
                productService.refreshRedisProducts();

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

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductOrganizations(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();
                productService.updateProductOrganizations(id, organizations);
                res.message = ApiInfo.UPDATE_PRODUCT_ORGANIZATIONS_SUCCESS;
                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
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
                //刷新redis缓存
                productService.refreshRedisProducts();

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

    //更新staff
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductStaffs(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();
                if (StringUtils.isBlank(staffs)) {
                    res.state = 0;
                    res.message = "输入信息为空";
                    return JSON.toJSONString(res);
                }
                productService.updateProductStaffs(id, staffs);
                //刷新redis缓存
                productService.refreshRedisProducts();

                res.message = ApiInfo.UPDATE_PRODUCT_STAFFS_SUCCESS;
                // //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
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

    //region ------图片操纵-------

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addProductImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(productService.getProduct(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                productService.addProductImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));

                //刷新redis缓存
                productService.refreshRedisProducts();

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
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.getId()) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = productService.updateProductImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.getId()) {
                    res.message = productService.deleteProductImages(id, images);
                }else {
                    res.setErrorMessage(ApiInfo.NOT_ACTION);
                }

                //刷新redis缓存
                productService.refreshRedisProducts();

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------特殊查询------

    /**
     * 根据系列id获取该系列所有产品
     * */
    @RequestMapping(path = "/get-product-set", method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> getAllProductByFranchiseId(@RequestBody String json){
        JSONObject param = JSON.parseObject(json);
        return productService.getProductSet(param.getList("franchises", Integer.class), param.getInteger("entityType"));
    }

    @RequestMapping(path = "/get-products", method = RequestMethod.POST)
    @ResponseBody
    public String getAllProductsByFilter(@RequestBody String json){

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");

        SearchResult searchResult = productService.getProductsByFilter(queryParams);

        List<ProductVOAlpha> products = productVOMapper.product2VOAlpha((List<Product>) searchResult.data);

        JSONObject result = new JSONObject();
        result.put("data", products);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //endregion
}
