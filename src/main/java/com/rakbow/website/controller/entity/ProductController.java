package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.product.ProductCategory;
import com.rakbow.website.data.vo.product.ProductVOAlpha;
import com.rakbow.website.entity.Product;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.convertMapper.ProductVOMapper;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    private AlbumService albumService;
    @Autowired
    private BookService bookService;
    @Autowired
    private DiscService discService;
    @Autowired
    private GameService gameService;
    @Autowired
    private EntityUtils entityUtils;

    private final ProductVOMapper productVOMapper = ProductVOMapper.INSTANCES;
    //endregion

    //region ------获取页面------

    //获取单个产品详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getProductDetail(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        Product product = productService.getProductWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (product == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.PRODUCT.getNameZh()));
            return "/error/404";
        }
        model.addAttribute("product", productVOMapper.product2VO(product));
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.PRODUCT.getId()));
        model.addAttribute("relatedProducts", productService.getRelatedProducts(id));

        if (product.getCategory() == ProductCategory.ANIMATION.getIndex()
                || product.getCategory() == ProductCategory.LIVE_ACTION_MOVIE.getIndex()) {
            model.addAttribute("albums", albumService.getAlbumsByProductId(id));
            model.addAttribute("discs", discService.getDiscsByProductId(id));
        }
        if (product.getCategory() == ProductCategory.BOOK.getIndex()) {
            model.addAttribute("books", bookService.getBooksByProductId(id));
        }
        if (product.getCategory() == ProductCategory.GAME.getIndex()) {
            model.addAttribute("albums", albumService.getAlbumsByProductId(id));
            model.addAttribute("games", gameService.getGamesByProductId(id));
        }

        //获取页面数据
        model.addAttribute("pageInfo",
                entityUtils.getPageInfo(EntityType.PRODUCT.getId(), id, product.getAddedTime(), product.getEditedTime()));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getMetaDetailInfo(product, EntityType.PRODUCT.getId()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(product.getImages(), 200, EntityType.PRODUCT, true));

        return "/database/itemDetail/product-detail";
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
            //检测数据
            String errorMsg = productService.checkProductJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Product product = productService.json2Product(param);

            //保存新增专辑
            res.message = productService.addProduct(product);

            //刷新redis缓存
            productService.refreshRedisProducts();
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
            //检测数据
            String errorMsg = productService.checkProductJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Product product = productService.json2Product(param);

            //修改编辑时间
            product.setEditedTime(new Timestamp(System.currentTimeMillis()));

            res.message = productService.updateProduct(product.getId(), product);

            //刷新redis缓存
            productService.refreshRedisProducts();
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
            int id = JSON.parseObject(json).getInteger("id");
            String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();

            res.message = productService.updateProductOrganizations(id, organizations);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String description = JSON.parseObject(json).get("description").toString();

            res.message = productService.updateProductDescription(id, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新staff
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateProductStaffs(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();
            if (StringUtils.isBlank(staffs)) {
                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                return JSON.toJSONString(res);
            }

            res.message = productService.updateProductStaffs(id, staffs);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------图片操纵-------

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addProductImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            Product product = productService.getProduct(id);

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(product.getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMessage = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.equals("", errorMessage)) {
                res.setErrorMessage(errorMessage);
                return JSON.toJSONString(res);
            }

            productService.addProductImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));

            res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.PRODUCT.getNameZh());
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
            //获取id
            int id = JSON.parseObject(json).getInteger("id");
            int action = JSON.parseObject(json).getIntValue("action");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
            }

            //更改图片
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    res.setErrorMessage(errorMsg);
                    return JSON.toJSONString(res);
                }

                res.message = productService.updateProductImages(id, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = productService.deleteProductImages(id, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
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
    public String getAllProductByFranchiseId(@RequestBody String json){
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            res.data = productService.getProductSet(param.getList("franchises", Integer.class), param.getInteger("entityType"));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(path = "/get-products", method = RequestMethod.POST)
    @ResponseBody
    public String getProductsByFilter(@RequestBody String json, HttpServletRequest request){

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");

        SearchResult searchResult = productService.getProductsByFilter(queryParams,
                userService.getUserOperationAuthority(userService.getUserByRequest(request)));

        List<ProductVOAlpha> products = productVOMapper.product2VOAlpha((List<Product>) searchResult.data);

        JSONObject result = new JSONObject();
        result.put("data", products);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //endregion
}
