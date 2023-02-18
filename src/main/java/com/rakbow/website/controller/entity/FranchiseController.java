package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.franchise.FranchiseVOAlpha;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.service.FranchiseService;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.service.UserService;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.convertMapper.FranchiseVOMapper;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
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
 * @Create: 2023-01-14 17:07
 * @Description:
 */
@Controller
@RequestMapping("/db/franchise")
public class FranchiseController {

    //region ------注入依赖------

    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private ProductService productService;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityUtils entityUtils;

    private final FranchiseVOMapper franchiseVOMapper = FranchiseVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个系列详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getFranchiseDetail(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        Franchise franchise = franchiseService.getFranchiseWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (franchise == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.FRANCHISE.getNameZh()));
            return "/error/404";
        }
        model.addAttribute("franchise", franchiseVOMapper.franchise2VO(franchise));
        model.addAttribute("products", productService.getProductsByFranchiseId(franchise.getId()));
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.FRANCHISE.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityUtils.getPageInfo(EntityType.FRANCHISE.getId(), id, franchise.getAddedTime(), franchise.getEditedTime()));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getMetaDetailInfo(franchise, EntityType.FRANCHISE.getId()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(franchise.getImages(), 200, EntityType.FRANCHISE, false));
        return "/database/itemDetail/franchise-detail";
    }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取专辑
    @RequestMapping(value = "/get-franchises", method = RequestMethod.POST)
    @ResponseBody
    public String getFranchisesByFilterList(@RequestBody String json, HttpServletRequest request) {

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");

        SearchResult searchResult = franchiseService.getFranchisesByFilter(queryParams,
                 userService.getUserOperationAuthority(userService.getUserByRequest(request)));

        List<FranchiseVOAlpha> franchises = franchiseVOMapper.franchise2VOAlpha((List<Franchise>) searchResult.data);

        JSONObject result = new JSONObject();
        result.put("data", franchises);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //新增
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addFranchise(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = franchiseService.checkFranchiseJson(param);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Franchise franchise = franchiseService.json2Franchise(franchiseService.handleFranchiseJson(param));

            //保存新增
            res.message = franchiseService.addFranchise(franchise);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateFranchise(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = franchiseService.checkFranchiseJson(param);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Franchise franchise = franchiseService.json2Franchise(franchiseService.handleFranchiseJson(param));

            //修改编辑时间
            franchise.setEditedTime(new Timestamp(System.currentTimeMillis()));

            res.message = franchiseService.updateFranchise(franchise.getId(), franchise);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateFranchiseDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String description = JSON.parseObject(json).get("description").toString();

            res.message = franchiseService.updateFranchiseDescription(id, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------图片操作------

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addFranchiseImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(franchiseService.getFranchise(id).getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            res.message = franchiseService.addFranchiseImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateFranchiseImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            //获取id
            int id = JSON.parseObject(json).getInteger("id");
            int action = JSON.parseObject(json).getIntValue("action");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
            }

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMessage = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                res.message = franchiseService.updateFranchiseImages(id, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = franchiseService.deleteFranchiseImages(id, images);
            } else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
