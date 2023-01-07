package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.MediaFormat;
import com.rakbow.website.data.common.DataActionType;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.entity.Disc;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.Image.CommonImageHandleUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.common.HostHolder;
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
 * @Create: 2022-12-15 20:57
 * @Description:
 */
@Controller
@RequestMapping("/db/disc")
public class DiscController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final List<JSONObject> mediaFormatSet = MediaFormat.getMediaFormatSet();

    @Autowired
    private DiscService discService;
    @Autowired
    private UserService userService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private HostHolder hostHolder;

    //endregion

    //region ------获取页面------

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ModelAndView getDiscListPage(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("mediaFormatSet", mediaFormatSet);
        model.addAttribute("seriesSet", franchiseService.getAllFranchiseSet());
        view.setViewName("/disc/disc-list");
        return view;
    }

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int id, Model model) {
        if (discService.getDiscById(id) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.DISC.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.DISC.getId(), id);

        Disc disc = discService.getDiscById(id);

        model.addAttribute("mediaFormatSet", mediaFormatSet);
        model.addAttribute("productSet", productService.getProductSet
                (disc.getSeries(), EntityType.DISC.getId()));
        model.addAttribute("seriesSet", franchiseService.getAllFranchiseSet());

        model.addAttribute("disc", discService.disc2Json(disc));
        model.addAttribute("user", hostHolder.getUser());
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.DISC.getId(), id).getVisitNum());
        //获取相关碟片
        model.addAttribute("relatedDiscs", discService.getRelatedDiscs(id));
        return "/disc/disc-detail";
    }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取碟片--列表界面
    @RequestMapping(value = "/get-discs", method = RequestMethod.POST)
    @ResponseBody
    public String getDiscsByFilterList(@RequestBody String json) {

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<JSONObject> discs = new ArrayList<>();

        Map<String, Object> map = discService.getDiscsByFilterList(queryParams);

        if (StringUtils.equals(pageLabel, "list")) {
            discs = discService.disc2JsonList((List<Disc>) map.get("data"));
        }
        if (StringUtils.equals(pageLabel, "index")) {
            discs = discService.disc2JsonIndex((List<Disc>) map.get("data"));
        }

        JSONObject result = new JSONObject();
        result.put("data", discs);
        result.put("total", map.get("total"));

        return JSON.toJSONString(result);
    }

    //新增碟片
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDisc(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(discService.checkDiscJson(param))) {
                    res.setErrorMessage(discService.checkDiscJson(param));
                    return JSON.toJSONString(res);
                }

                Disc disc = discService.json2Disc(discService.handleDiscJson(param));

                //保存新增专辑
                discService.addDisc(disc);

                //将新增的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.DISC.getId(), disc.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.DISC.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除碟片(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteDisc(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONArray discs = JSON.parseArray(json);
        try {
            if (userService.checkAuthority(request).state) {
                for (int i = 0; i < discs.size(); i++) {

                    int id = discs.getJSONObject(i).getInteger("id");

                    //从数据库中删除专辑
                    discService.deleteDiscById(id);

                    //从Elasticsearch服务器索引中删除专辑
                    // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                    //删除访问量实体
                    visitService.deleteVisit(EntityType.DISC.getId(), id);
                }
                res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.DISC.getNameZh());
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新碟片基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateDisc(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(discService.checkDiscJson(param))) {
                    res.setErrorMessage(discService.checkDiscJson(param));
                    return JSON.toJSONString(res);
                }

                Disc disc = discService.json2Disc(discService.handleDiscJson(param));

                //修改编辑时间
                disc.setEditedTime(new Timestamp(System.currentTimeMillis()));

                discService.updateDisc(disc.getId(), disc);

                //将更新的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.DISC.getNameZh());

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

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(discService.getDiscById(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageHandleUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                discService.addDiscImages(id, images, imagesJson, imageInfosJson);

                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.DISC.getNameZh());

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
    public String updateDiscImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取专辑id
                int id = JSON.parseObject(json).getInteger("id");
                JSONArray images = JSON.parseObject(json).getJSONArray("images");
                for (int i = 0; i < images.size(); i++) {
                    images.getJSONObject(i).remove("thumbUrl");
                }

                //更新图片信息
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.id) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageHandleUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = discService.updateDiscImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.id) {
                    res.message = discService.deleteDiscImages(id, images);
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

    //更新专辑规格信息
    @RequestMapping(path = "/update-spec", method = RequestMethod.POST)
    @ResponseBody
    public String updateDiscSpec(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String spec = JSON.parseObject(json).get("spec").toString();
                discService.updateDiscSpec(id, spec);
                res.message = ApiInfo.UPDATE_DISC_SPEC_SUCCESS;
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

    //更新专辑描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateDiscDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                discService.updateDiscDescription(id, description);
                res.message = ApiInfo.UPDATE_DISC_DESCRIPTION_SUCCESS;
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

    //更新专辑特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateDiscBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String bonus = JSON.parseObject(json).get("bonus").toString();
                discService.updateDiscBonus(id, bonus);
                res.message = ApiInfo.UPDATE_DISC_BONUS_SUCCESS;
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

    //endregion

}
