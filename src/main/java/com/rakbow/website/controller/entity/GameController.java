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
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.convertMapper.GameVOMapper;
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
    private VisitService visitService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisUtil redisUtil;

    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个游戏详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getGameDetail(@PathVariable("id") Integer id, Model model) {
        if (gameService.getGame(id) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.GAME.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.GAME.getId(), id);

        Game game = gameService.getGame(id);

        model.addAttribute("releaseTypeSet", redisUtil.get("releaseTypeSet"));
        model.addAttribute("regionSet", redisUtil.get("regionSet"));
        model.addAttribute("gamePlatformSet", redisUtil.get("platformSet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("game", gameVOMapper.game2VO(game));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getItemDetailInfo(game, EntityType.GAME.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", visitService.getPageInfo(EntityType.GAME.getId(), id, game.getAddedTime(), game.getEditedTime()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtils.segmentImages(game.getImages(), 140, false));
        //获取相关游戏
        model.addAttribute("relatedGames", gameService.getRelatedGames(id));
        return "/itemDetail/game-detail";
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
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(gameService.checkGameJson(param))) {
                    res.setErrorMessage(gameService.checkGameJson(param));
                    return JSON.toJSONString(res);
                }

                Game game = gameService.json2Game(gameService.handleGameJson(param));

                //保存新增游戏
                gameService.addGame(game);

                //将新增的游戏保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.GAME.getId(), game.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.GAME.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
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
        JSONArray games = JSON.parseArray(json);
        try {
            if (userService.checkAuthority(request).state) {
                for (int i = 0; i < games.size(); i++) {

                    int id = games.getJSONObject(i).getInteger("id");

                    //从数据库中删除游戏
                    gameService.deleteGame(id);

                    //从Elasticsearch服务器索引中删除游戏
                    // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                    //删除访问量实体
                    visitService.deleteVisit(EntityType.GAME.getId(), id);
                }
                res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.GAME.getNameZh());
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
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
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(gameService.checkGameJson(param))) {
                    res.setErrorMessage(gameService.checkGameJson(param));
                    return JSON.toJSONString(res);
                }

                Game game = gameService.json2Game(gameService.handleGameJson(param));

                //修改编辑时间
                game.setEditedTime(new Timestamp(System.currentTimeMillis()));

                gameService.updateGame(game.getId(), game);

                //将更新的游戏保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.GAME.getNameZh());

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

    //根据搜索条件获取游戏--列表界面
    @RequestMapping(value = "/get-games", method = RequestMethod.POST)
    @ResponseBody
    public String getGamesByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<GameVOAlpha> games = new ArrayList<>();

        SearchResult serchResult = gameService.getGamesByFilter(queryParams,
                userService.getUserEditAuthority(userService.getUserByRequest(request)));

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
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(gameService.getGame(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                gameService.addGameImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));

                //更新elasticsearch中的游戏
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.GAME.getNameZh());

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
    public String updateGameImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取游戏id
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

                    res.message = gameService.updateGameImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.getId()) {
                    res.message = gameService.deleteGameImages(id, images);
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

    //更新游戏作者信息
    @RequestMapping(path = "/update-organizations", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameOrganizations(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String organizations = JSON.parseObject(json).getJSONArray("organizations").toString();
                gameService.updateGameOrganizations(id, organizations);
                res.message = ApiInfo.UPDATE_GAME_ORGANIZATIONS_SUCCESS;
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

    //更新游戏规格信息
    @RequestMapping(path = "/update-staffs", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameStaffs(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String staffs = JSON.parseObject(json).getJSONArray("staffs").toString();
                gameService.updateGameStaffs(id, staffs);
                res.message = ApiInfo.UPDATE_GAME_STAFFS_SUCCESS;
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

    //更新游戏描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                gameService.updateGameDescription(id, description);
                res.message = ApiInfo.UPDATE_GAME_DESCRIPTION_SUCCESS;
                //更新elasticsearch中的游戏
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

    //更新游戏特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateGameBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String bonus = JSON.parseObject(json).get("bonus").toString();
                gameService.updateGameBonus(id, bonus);
                res.message = ApiInfo.UPDATE_GAME_BONUS_SUCCESS;
                //更新elasticsearch中的游戏
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
