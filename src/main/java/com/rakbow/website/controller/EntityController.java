package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ActionResult;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.RedisCacheConstant;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.service.EntityService;
import com.rakbow.website.service.UserService;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.CookieUtil;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.common.RedisUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-12 16:38
 * @Description:
 */
@Controller
@RequestMapping("/db")
public class EntityController {

    //region ------引入实例------

    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Resource
    private EntityUtils entityUtils;
    @Resource
    private UserService userService;
    @Resource
    private EntityService entityService;
    @Resource
    private RedisUtil redisUtil;

    //endregion

    //获取在线数据库首页
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getDatabasePage(Model model) {
        model.addAttribute("indexCoverUrl", redisUtil.get(RedisCacheConstant.INDEX_COVER_URL));
        return "database/database";
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
        initData.put("justAddedItems", entityService.getJustAddedItems(entityType, 5));
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

    @RequestMapping(path = "/get-entity-amount-info", method = RequestMethod.GET)
    @ResponseBody
    public String getEntityAmountInfo() {
        ApiResult res = new ApiResult();
        try {
            res.data = entityService.getItemAmount();
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(path = "/simple-search", method = RequestMethod.POST)
    @ResponseBody
    public String simpleSearch(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            String keyword = JSON.parseObject(json).getString("keyword");
            int entityType = JSON.parseObject(json).getInteger("entityType");
            int offset = JSON.parseObject(json).getInteger("offset");
            int limit = JSON.parseObject(json).getInteger("limit");

            res.data = entityService.simpleSearch(keyword, entityType, offset, limit);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更改状态
    @RequestMapping(value = "/update-item-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemStatus(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            int entityId = JSON.parseObject(json).getIntValue("entityId");
            boolean status = JSON.parseObject(json).getBoolean("status");

            entityService.updateItemStatus(entityName, entityId, status?1:0);

            res.message = String.format(ApiInfo.UPDATE_ITEM_STATUS_URL, EntityType.getItemNameZhByIndex(entityType));
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //批量更改状态
    @RequestMapping(value = "/update-items-status", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsStatus(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            JSONArray items = JSON.parseObject(json).getJSONArray("items");
            boolean status = JSON.parseObject(json).getBoolean("status");

            List<Integer> ids = new ArrayList<>();

            for (Object item : items) {
                ids.add(((JSONObject) item).getIntValue("id"));
            }

            entityService.updateItemsStatus(entityName, ids, status?1:0);
            res.message = String.format(ApiInfo.UPDATE_ITEM_STATUS_URL, EntityType.getItemNameZhByIndex(entityType));
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemsDescription(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = JSON.parseObject(json).getInteger("entityId");
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            String description = JSON.parseObject(json).get("description").toString();
            res.message = entityService.updateItemDescription(entityName, entityId, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemBonus(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = JSON.parseObject(json).getInteger("entityId");
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            String bonus = JSON.parseObject(json).get("bonus").toString();
            res.message = entityService.updateItemBonus(entityName, entityId, bonus);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新规格信息
    @RequestMapping(path = "/update-spec", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemSpec(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int entityId = JSON.parseObject(json).getInteger("entityId");
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            String spec = JSON.parseObject(json).get("spec").toString();
            res.message = entityService.updateItemSpec(entityName, entityId, spec);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //点赞
    @RequestMapping(path = "/like", method = RequestMethod.POST)
    @ResponseBody
    public String likeEntity(@RequestBody String json, HttpServletRequest request, HttpServletResponse response) {
        ApiResult res = new ApiResult();
        try {
            int entityType = JSON.parseObject(json).getIntValue("entityType");
            int entityId = JSON.parseObject(json).getIntValue("entityId");

            // 从cookie中获取点赞token
            String likeToken = CookieUtil.getValue(request, "like_token");
            if(likeToken == null) {
                //生成likeToken,并返回
                likeToken = CommonUtil.generateUUID();
                Cookie cookie = new Cookie("like_token", likeToken);
                cookie.setPath(contextPath);
                response.addCookie(cookie);
            }
            if(entityService.entityLike(entityType, entityId, likeToken)) {
                res.message = ApiInfo.LIKE_SUCCESS;
            }else {
                res.setErrorMessage(ApiInfo.LIKE_FAILED);
                return JSON.toJSONString(res);
            }
        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //region image

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addItemImages(int entityType, int entityId, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();

            //原始图片信息json数组
            JSONArray imagesJson = entityService.getItemImages(entityName, entityId);
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            ActionResult ar = entityService.addItemImages(entityName, entityId, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
            if(ar.state) {
                res.message = ar.message;
            }else {
                res.setErrorMessage(ar.message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateItemImages(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            //获取图书id
            int entityType = JSON.parseObject(json).getInteger("entityType");
            int entityId = JSON.parseObject(json).getInteger("entityId");
            int action = JSON.parseObject(json).getInteger("action");

            String entityName = EntityType.getItemNameEnByIndex(entityType).toLowerCase();
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
                images.getJSONObject(i).remove("thumbUrl50");
            }

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    res.setErrorMessage(errorMsg);
                    return JSON.toJSONString(res);
                }

                res.message = entityService.updateItemImages(entityName, entityId, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = entityService.deleteItemImages(entityName, entityId, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
