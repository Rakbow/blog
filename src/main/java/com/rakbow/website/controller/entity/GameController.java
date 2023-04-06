package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.dto.QueryParams;
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

import javax.annotation.Resource;
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

    @Resource
    private GameService gameService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtils entityUtils;
    @Resource
    private EntityService entityService;

    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个游戏详细信息页面
    @UniqueVisitor
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
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.GAME.getId(), id, game, request));
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
    public String addGame(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Game game = entityService.json2Entity(gameService.handleGameJson(param), Game.class);

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
    public String deleteGame(@RequestBody String json) {
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
    public String updateGame(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = gameService.checkGameJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Game game = entityService.json2Entity(gameService.handleGameJson(param), Game.class);

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
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-games", method = RequestMethod.POST)
    @ResponseBody
    public String getGamesByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<GameVOAlpha> games = new ArrayList<>();

        SearchResult serchResult = gameService.getGamesByFilter(queryParam,
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

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameOrganizations(@RequestBody String json) {
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
    public String updateGameStaffs(@RequestBody String json) {
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

    //endregion

}
