package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.service.*;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.common.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-12 16:38
 * @Description:
 */
@Controller
@RequestMapping("/db")
public class DatabaseController {

    //region ------引入实例------

    @Autowired
    private AlbumService albumService;
    @Autowired
    private DiscService discService;
    @Autowired
    private BookService bookService;
    @Autowired
    private MerchService merchService;
    @Autowired
    private GameService gameService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private EntityUtils entityUtils;
    @Autowired
    private UserService userService;
    @Autowired
    private CommonService commonService;
    @Autowired
    private EntityService entityService;

    //endregion

    //获取在线数据库首页
    @RequestMapping(path = "", method = RequestMethod.GET)
    public ModelAndView getDatabasePage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/database/database-search");
        return view;
    }

    //region 获取数据库搜索主页
    @RequestMapping(path = "/albums", method = RequestMethod.GET)
    public String getAlbumIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/books", method = RequestMethod.GET)
    public String getBookIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/discs", method = RequestMethod.GET)
    public String getDiscIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/games", method = RequestMethod.GET)
    public String getGameIndexPage() {
        return "/database/database-index";
    }
    @RequestMapping(path = "/merchs", method = RequestMethod.GET)
    public String getMerchIndexPage() {
        return "/database/database-index";
    }
    //endregion

    //region 获取数据库管理列表
    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public String getDatabaseListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/album", method = RequestMethod.GET)
    public String getAlbumListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/book", method = RequestMethod.GET)
    public String getBookListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/disc", method = RequestMethod.GET)
    public String getDiscListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/game", method = RequestMethod.GET)
    public String getGameListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/merch", method = RequestMethod.GET)
    public String getMerchListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/product", method = RequestMethod.GET)
    public String getProductListPage() {
        return "/database/database-list";
    }
    @RequestMapping(path = "/list/franchise", method = RequestMethod.GET)
    public String getFranchiseListPage() {
        return "/database/database-list";
    }
    //endregion

    //region 获取index初始数据
    @RequestMapping(value = "/get-index-init-data", method = RequestMethod.POST)
    @ResponseBody
    public String getIndexInitData(@RequestBody String json) {
        int entityType = JSON.parseObject(json).getIntValue("entityType");

        JSONObject initData = entityUtils.getDetailOptions(entityType);
        initData.put("justAddedItems", entityService.getJustAddedItems(entityType, 9));
        initData.put("popularItems", entityService.getPopularItems(entityType, 9));

        return initData.toJSONString();
    }

    //获取list初始数据
    @RequestMapping(value = "/get-list-init-data", method = RequestMethod.POST)
    @ResponseBody
    public String getListInitData(@RequestBody String json, HttpServletRequest request) {
        int entityType = JSON.parseObject(json).getIntValue("entityType");
        JSONObject initData = entityUtils.getDetailOptions(entityType);
        initData.put("editAuth", userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        return initData.toJSONString();
    }
    //endregion

    @RequestMapping(value = "/update-item-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemStatus(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            int entityId = JSON.parseObject(json).getIntValue("entityId");
            boolean status = JSON.parseObject(json).getBoolean("status");

            commonService.updateItemStatus(entityName, entityId, status?1:0);

            res.message = String.format(ApiInfo.UPDATE_ITEM_STATUS, EntityType.getItemNameZhByIndex(entityType));
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

}
