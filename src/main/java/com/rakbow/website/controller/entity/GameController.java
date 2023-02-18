package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.entity.Game;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.convertMapper.GameVOMapper;
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
 * @Create: 2023-01-06 15:50
 * @Description:
 */

@Controller
@RequestMapping("/db/game")
public class GameController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private GameService gameService;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityUtils entityUtils;
    @Autowired
    private EntityService entityService;

    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个游戏详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getGameDetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        Game game = gameService.getGameWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (game == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.GAME.getNameZh()));
            return "/error/404";
        }

        model.addAttribute("game", gameVOMapper.game2VO(game));
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.GAME.getId()));
        //实体类通用信息
        model.addAttribute("detailInfo", entityUtils.getItemDetailInfo(game, EntityType.GAME.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.GAME.getId(), id, game.getAddedTime(), game.getEditedTime(), request));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(game.getImages(), 140, EntityType.GAME, false));
        //获取相关游戏
        model.addAttribute("relatedGames", gameService.getRelatedGames(id));
        return "/database/itemDetail/game-detail";
    }

    //endregion

    //region ------增删改查------

    //新增游戏
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addGame(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Game game = gameService.json2Game(gameService.handleGameJson(param));

            //保存新增游戏
            res.message = gameService.addGame(game);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除游戏(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteGame(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            List<Game> games = JSON.parseArray(json).toJavaList(Game.class);
            for (Game game : games) {
                //从数据库中删除专辑
                gameService.deleteGame(game);
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.GAME.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新游戏基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateGame(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Game game = gameService.json2Game(gameService.handleGameJson(param));

            //修改编辑时间
            game.setEditedTime(new Timestamp(System.currentTimeMillis()));

            res.message = gameService.updateGame(game.getId(), game);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取游戏--列表界面
    @RequestMapping(value = "/get-games", method = RequestMethod.POST)
    @ResponseBody
    public String getGamesByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<GameVOAlpha> games = new ArrayList<>();

        SearchResult serchResult = gameService.getGamesByFilter(queryParams,
                userService.getUserOperationAuthority(userService.getUserByRequest(request)));

        if (StringUtils.equals(pageLabel, "list")) {
            games = gameVOMapper.game2VOAlpha((List<Game>) serchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            games = gameVOMapper.game2VOAlpha((List<Game>) serchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", games);
        result.put("total", serchResult.total);

        return JSON.toJSONString(result);
    }

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addGameImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            Game game = gameService.getGame(id);

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(game.getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            res.message = gameService.addGameImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            //获取游戏id
            int id = JSON.parseObject(json).getInteger("id");
            int action = JSON.parseObject(json).getIntValue("action");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
            }

            Game game = gameService.getGame(id);

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    res.setErrorMessage(errorMsg);
                    return JSON.toJSONString(res);
                }

                res.message = gameService.updateGameImages(id, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = gameService.deleteGameImages(game, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameOrganizations(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();

            res.message = gameService.updateGameOrganizations(id, organizations);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新游戏规格信息
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameStaffs(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();

            res.message = gameService.updateGameStaffs(id, staffs);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新游戏描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String description = JSON.parseObject(json).get("description").toString();

            res.message = gameService.updateGameDescription(id, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新游戏特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String bonus = JSON.parseObject(json).get("bonus").toString();

            res.message = gameService.updateGameBonus(id, bonus);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
