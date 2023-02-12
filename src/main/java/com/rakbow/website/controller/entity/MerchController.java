package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.vo.merch.MerchVO;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.convertMapper.MerchVOMapper;
import com.rakbow.website.util.file.CommonImageUtils;
import com.rakbow.website.util.common.RedisUtil;
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

    @Autowired
    private MerchService merchService;
    @Autowired
    private UserService userService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisUtil redisUtil;

    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

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

        model.addAttribute("merchCategorySet", redisUtil.get("merchCategorySet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("regionSet", redisUtil.get("regionSet"));
        model.addAttribute("merch", merchVOMapper.merch2VO(merch));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getItemDetailInfo(merch, EntityType.MERCH.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", visitService.getPageInfo(EntityType.MERCH.getId(), id, merch.getAddedTime(), merch.getEditedTime()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtils.segmentImages(merch.getImages(), 200, false));
        //获取相关周边
        model.addAttribute("relatedMerchs", merchService.getRelatedMerchs(id));
        return "/itemDetail/merch-detail";
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
    public String getMerchsByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<MerchVOAlpha> merchs = new ArrayList<>();

        SearchResult searchResult = merchService.getMerchsByFilterList(queryParams,
                userService.getUserEditAuthority(userService.getUserByRequest(request)));

        if (StringUtils.equals(pageLabel, "list")) {
            merchs = merchVOMapper.merch2VOAlpha((List<Merch>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            merchs = merchVOMapper.merch2VOAlpha((List<Merch>) searchResult.data);
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
                    res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(merchService.getMerch(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                merchService.addMerchImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));

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
                    String errorMessage = CommonImageUtils.checkUpdateImages(images);
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
