package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.DataActionType;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.data.common.SearchResult;
import com.rakbow.website.data.merch.MerchCategory;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.Image.CommonImageHandleUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.system.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-04 16:57
 * @Description:
 */

@Controller
@RequestMapping("/db/merch")
public class MerchController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final List<JSONObject> merchCategorySet = MerchCategory.getMerchCategorySet();

    @Autowired
    private MerchService merchService;
    @Autowired
    private UserService userService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisUtil redisUtil;

    //endregion

    //region ------获取页面------

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ModelAndView getMerchListPage(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("merchCategorySet", merchCategorySet);
        model.addAttribute("franchiseSet", redisUtil.get("franchises"));
        view.setViewName("/merch/merch-list");
        return view;
    }

    //获取单个周边商品详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getMerchDetail(@PathVariable int id, Model model) {
        if (merchService.getMerch(id) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.MERCH.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.MERCH.getId(), id);

        Merch merch = merchService.getMerch(id);

        model.addAttribute("merchCategorySet", merchCategorySet);
        model.addAttribute("franchiseSet", redisUtil.get("franchises"));
        model.addAttribute("merch", merchService.merch2Json(merch));
        model.addAttribute("user", hostHolder.getUser());
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.MERCH.getId(), id).getVisitNum());
        //获取相关周边
        model.addAttribute("relatedMerchs", merchService.getRelatedMerchs(id));
        return "/merch/merch-detail";
    }

    //endregion

    //region ------增删改查------

    //新增周边
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addMerch(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(merchService.checkMerchJson(param))) {
                    res.setErrorMessage(merchService.checkMerchJson(param));
                    return JSON.toJSONString(res);
                }

                Merch merch = merchService.json2Merch(merchService.handleMerchJson(param));

                //保存新增周边
                merchService.addMerch(merch);

                //将新增的周边保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.MERCH.getId(), merch.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.MERCH.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除周边(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteMerch(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONArray merchs = JSON.parseArray(json);
        try {
            if (userService.checkAuthority(request).state) {
                for (int i = 0; i < merchs.size(); i++) {

                    int id = merchs.getJSONObject(i).getInteger("id");

                    //从数据库中删除周边
                    merchService.deleteMerch(id);

                    //从Elasticsearch服务器索引中删除周边
                    // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                    //删除访问量实体
                    visitService.deleteVisit(EntityType.MERCH.getId(), id);
                }
                res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.MERCH.getNameZh());
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新周边基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerch(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(merchService.checkMerchJson(param))) {
                    res.setErrorMessage(merchService.checkMerchJson(param));
                    return JSON.toJSONString(res);
                }

                Merch merch = merchService.json2Merch(merchService.handleMerchJson(param));

                //修改编辑时间
                merch.setEditedTime(new Timestamp(System.currentTimeMillis()));

                merchService.updateMerch(merch.getId(), merch);

                //将更新的周边保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.MERCH.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取周边--列表界面
    @RequestMapping(value = "/get-merchs", method = RequestMethod.POST)
    @ResponseBody
    public String getMerchsByFilterList(@RequestBody String json) {
        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<JSONObject> merchs = new ArrayList<>();

        SearchResult searchResult = merchService.getMerchsByFilterList(queryParams);

        if (StringUtils.equals(pageLabel, "list")) {
            merchs = merchService.merch2JsonList((List<Merch>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            merchs = merchService.merch2JsonIndex((List<Merch>) searchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", merchs);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addMerchImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(merchService.getMerch(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageHandleUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                merchService.addMerchImages(id, images, imagesJson, imageInfosJson);

                //更新elasticsearch中的周边
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.MERCH.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerchImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取周边id
                int id = JSON.parseObject(json).getInteger("id");
                JSONArray images = JSON.parseObject(json).getJSONArray("images");
                for (int i = 0; i < images.size(); i++) {
                    images.getJSONObject(i).remove("thumbUrl");
                }

                //更新图片信息
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.getId()) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageHandleUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = merchService.updateMerchImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.getId()) {
                    res.message = merchService.deleteMerchImages(id, images);
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

    //更新周边规格信息
    @RequestMapping(path = "/update-spec", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerchSpec(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String spec = JSON.parseObject(json).getJSONArray("spec").toString();
                merchService.updateMerchSpec(id, spec);
                res.message = ApiInfo.UPDATE_MERCH_SPEC_SUCCESS;
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

    //更新周边描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerchDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                merchService.updateMerchDescription(id, description);
                res.message = ApiInfo.UPDATE_MERCH_DESCRIPTION_SUCCESS;
                //更新elasticsearch中的周边
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

}
