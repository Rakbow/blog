package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.GameMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.game.GameVOAlpha;
import com.rakbow.website.data.vo.game.GameVOBeta;
import com.rakbow.website.entity.Game;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.convertMapper.GameVOMapper;
import com.rakbow.website.util.file.QiniuImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-06 14:43
 * @Description: game业务层
 */
@Service
public class GameService {

    //region ------注入依赖------

    @Autowired
    private GameMapper gameMapper;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    @Autowired
    private VisitService visitService;

    private final GameVOMapper gameVOMapper = GameVOMapper.INSTANCES;

    //endregion

    //region ------更删改查------

    /**
     * 新增游戏
     *
     * @param game 新增的游戏
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addGame(Game game) {
        gameMapper.addGame(game);
    }

    /**
     * 根据Id获取游戏
     *
     * @param id 游戏id
     * @return game
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Game getGame(int id) {
        return gameMapper.getGame(id);
    }

    /**
     * 根据Id删除游戏
     *
     * @param id 游戏id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteGame(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllGameImages(id);

        gameMapper.deleteGame(id);
    }

    /**
     * 更新游戏基础信息
     *
     * @param id 游戏id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateGame(int id, Game game) {
        gameMapper.updateGame(id, game);
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Game，以便保存到数据库
     *
     * @param gameJson gameJson
     * @return game
     * @author rakbow
     */
    public Game json2Game(JSONObject gameJson) {
        return JSON.to(Game.class, gameJson);
    }

    /**
     * 检测数据合法性
     *
     * @param gameJson gameJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkGameJson(JSONObject gameJson) {
        if (StringUtils.isBlank(gameJson.getString("name"))) {
            return ApiInfo.GAME_NAME_EMPTY;
        }

        if (StringUtils.isBlank(gameJson.getString("releaseDate"))) {
            return ApiInfo.GAME_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(gameJson.getString("releaseType"))) {
            return ApiInfo.GAME_RELEASE_TYPE_EMPTY;
        }
        if (StringUtils.isBlank(gameJson.getString("platform"))) {
            return ApiInfo.GAME_PLATFORM_EMPTY;
        }
        if (StringUtils.isBlank(gameJson.getString("region"))) {
            return ApiInfo.GAME_REGION_EMPTY;
        }
        if (StringUtils.isBlank(gameJson.getString("franchises"))) {
            return ApiInfo.GAME_FRANCHISES_EMPTY;
        }
        if (StringUtils.isBlank(gameJson.getString("products"))
                || StringUtils.equals(gameJson.getString("products"), "[]")) {
            return ApiInfo.GAME_PRODUCTS_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送游戏数据
     *
     * @param gameJson gameJson
     * @return 处理后的game json格式数据
     * @author rakbow
     */
    public JSONObject handleGameJson(JSONObject gameJson) {

        String[] products = CommonUtils.str2SortedArray(gameJson.getString("products"));
        String[] franchises = CommonUtils.str2SortedArray(gameJson.getString("franchises"));
        gameJson.put("releaseDate", gameJson.getDate("releaseDate"));
        gameJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        gameJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");

        return gameJson;
    }

    //endregion

    //region ------图片操作------

    /**
     * 新增游戏图片
     *
     * @param id                 游戏id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addGameImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                              JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.GAME, images, originalImagesJson, imageInfos);

        gameMapper.updateGameImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新游戏图片
     *
     * @param id     游戏id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateGameImages(int id, String images) {
        gameMapper.updateGameImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.GAME.getNameZh());
    }

    /**
     * 删除游戏图片
     *
     * @param id           游戏id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteGameImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getGame(id).getImages());

        JSONArray finalImageJson = qiniuImageUtils.commonDeleteImages(id, images, deleteImages);

        gameMapper.updateGameImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.GAME.getNameZh());
    }

    /**
     * 删除该游戏所有图片
     *
     * @param id 游戏id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAllGameImages(int id) {
        Game game = getGame(id);
        JSONArray images = JSON.parseArray(game.getImages());
        qiniuImageUtils.commonDeleteAllImages(EntityType.GAME, images);
    }

    //endregion

    //region ------更新game数据------

    /**
     * 更新游戏关联组织信息
     *
     * @param id      游戏id
     * @param organizations 游戏的关联组织json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateGameOrganizations(int id, String organizations) {
        gameMapper.updateGameOrganizations(id, organizations, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新游戏开发制作人员信息
     *
     * @param id   游戏id
     * @param staffs 游戏的开发制作人员信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateGameStaffs(int id, String staffs) {
        gameMapper.updateGameStaffs(id, staffs, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新游戏描述
     *
     * @param id          游戏id
     * @param description 游戏的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateGameDescription(int id, String description) {
        gameMapper.updateGameDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新特典信息
     *
     * @param id    游戏id
     * @param bonus 游戏的特典信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateGameBonus(int id, String bonus) {
        gameMapper.updateGameBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getGamesByFilter(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String name = filter.getJSONObject("name").getString("value");
        String region = filter.getJSONObject("region").getString("value");

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        } else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }


        int platform = 100;
        if (filter.getJSONObject("platform").getInteger("value") != null) {
            platform = filter.getJSONObject("platform").getIntValue("value");
        }

        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);



        List<Game> games = gameMapper.getGamesByFilter(name, hasBonus, franchises, products, platform, region,
                sortField, sortOrder, first, row);

        int total = gameMapper.getGamesRowsByFilter(name, hasBonus, franchises, products, platform, region);

        return new SearchResult(total, games);
    }

    /**
     * 根据作品id获取关联游戏
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<GameVOBeta> getGamesByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Game> games = gameMapper.getGamesByFilter(null, null, null, products,
                100, null, "releaseDate", 1,  0, 0);

        return gameVOMapper.game2VOBeta(games);
    }

    /**
     * 获取相关联Game
     *
     * @param id 游戏id
     * @return list封装的Game
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<GameVOBeta> getRelatedGames(int id) {

        List<Game> result = new ArrayList<>();

        Game game = getGame(id);

        //该Game包含的作品id
        List<Integer> productIds = JSONObject.parseObject(game.getProducts()).getList("ids", Integer.class);

        //该系列所有Game
        List<Game> allGames = gameMapper.getGamesByFilter(null, null, CommonUtils.ids2List(game.getFranchises()),
                        null, 100, null, "releaseDate", 1, 0, 0)
                .stream().filter(tmpGame -> tmpGame.getId() != game.getId()).collect(Collectors.toList());

        List<Game> queryResult = allGames.stream().filter(tmpGame ->
                StringUtils.equals(tmpGame.getProducts(), game.getProducts())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Game> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Game> tmpQueryResult = allGames.stream().filter(tmpGame ->
                        JSONObject.parseObject(tmpGame.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Game> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allGames.stream().filter(tmpGame ->
                                JSONObject.parseObject(tmpGame.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtils.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return gameVOMapper.game2VOBeta(CommonUtils.removeDuplicateList(result));
    }

    /**
     * 获取最新收录的游戏
     *
     * @param limit 获取条数
     * @return list封装的游戏
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<GameVOAlpha> getJustAddedGames(int limit) {
        return gameVOMapper.game2VOAlpha(gameMapper.getGamesOrderByAddedTime(limit));
    }

    /**
     * 获取最近编辑的Game
     *
     * @param limit 获取条数
     * @return list封装的Game
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<GameVOAlpha> getJustEditedGames(int limit) {
        return gameVOMapper.game2VOAlpha(gameMapper.getGamesOrderByEditedTime(limit));
    }

    /**
     * 获取浏览量最高的Game
     *
     * @param limit 获取条数
     * @return list封装的Game
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<GameVOAlpha> getPopularGames(int limit) {
        List<GameVOAlpha> popularGames = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.GAME.getId(), limit);

        visits.forEach(visit -> {
            GameVOAlpha game = gameVOMapper.game2VOAlpha(getGame(visit.getEntityId()));
            game.setVisitNum(visit.getVisitNum());
            popularGames.add(game);
        });
        return popularGames;
    }

    //endregion

}
