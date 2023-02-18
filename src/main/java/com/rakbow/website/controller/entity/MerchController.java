package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.convertMapper.MerchVOMapper;
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
    private EntityUtils entityUtils;

    private final MerchVOMapper merchVOMapper = MerchVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个周边商品详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getMerchDetail(@PathVariable int id, Model model, HttpServletRequest request) {
        Merch merch = merchService.getMerchWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (merch == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.MERCH.getNameZh()));
            return "/error/404";
        }
        model.addAttribute("merch", merchVOMapper.merch2VO(merch));
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.PRODUCT.getId()));
        //实体类通用信息
        model.addAttribute("detailInfo", entityUtils.getItemDetailInfo(merch, EntityType.MERCH.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityUtils.getPageInfo(EntityType.MERCH.getId(), id, merch.getAddedTime(), merch.getEditedTime()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(merch.getImages(), 200, EntityType.MERCH, false));
        //获取相关周边
        model.addAttribute("relatedMerchs", merchService.getRelatedMerchs(id));
        return "/database/itemDetail/merch-detail";
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
            //检测数据
            String errorMsg = merchService.checkMerchJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Merch merch = merchService.json2Merch(merchService.handleMerchJson(param));

            //保存新增周边
            res.message = merchService.addMerch(merch);
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
        try {
            List<Merch> merchs = JSON.parseArray(json).toJavaList(Merch.class);
            for (Merch merch : merchs) {
                //从数据库中删除专辑
                merchService.deleteMerch(merch);
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.MERCH.getNameZh());
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
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = merchService.checkMerchJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Merch merch = merchService.json2Merch(merchService.handleMerchJson(param));

            //修改编辑时间
            merch.setEditedTime(new Timestamp(System.currentTimeMillis()));

            res.message = merchService.updateMerch(merch.getId(), merch);
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
                userService.getUserOperationAuthority(userService.getUserByRequest(request)));

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
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            Merch merch = merchService.getMerch(id);

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(merch.getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            res.message = merchService.addMerchImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
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
            //获取周边id
            int id = JSON.parseObject(json).getInteger("id");
            int action = JSON.parseObject(json).getIntValue("action");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
            }

            Merch merch = merchService.getMerch(id);

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    res.setErrorMessage(errorMsg);
                    return JSON.toJSONString(res);
                }

                res.message = merchService.updateMerchImages(id, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = merchService.deleteMerchImages(merch, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
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
            int id = JSON.parseObject(json).getInteger("id");
            String spec = JSON.parseObject(json).getJSONArray("spec").toString();

            res.message = merchService.updateMerchSpec(id, spec);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新周边描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateMerchDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String description = JSON.parseObject(json).get("description").toString();

            res.message = merchService.updateMerchDescription(id, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
