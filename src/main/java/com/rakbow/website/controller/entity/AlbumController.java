package com.rakbow.website.controller.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.data.dto.QueryParams;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.system.UserAuthority;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.*;
import com.rakbow.website.util.convertMapper.entity.AlbumVOMapper;
import com.rakbow.website.util.entity.MusicUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-07 21:53
 * @Description:
 */
@Controller
@RequestMapping("/db/album")
public class AlbumController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(AlbumController .class);

    @Resource
    private AlbumService albumService;
    @Resource
    private MusicService musicService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;
    @Resource
    private HostHolder hostHolder;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    private final ObjectMapper jsonMapper = new ObjectMapper();
    //endregion

    //region ------获取页面------

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @UniqueVisitor
    public String getAlbumDetail(@PathVariable("id") int id, Model model) {
        Album album = albumService.getAlbumWithAuth(id);
        if (album == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getNameZh()));
            return "/error/404";
        }

        List<Music> musics = musicService.getMusicsByAlbumId(id);

        String coverUrl = CommonImageUtil.getCoverUrl(album.getImages());
        model.addAttribute("album", albumService.buildVO(album, musics));
        if(UserAuthority.isUser(hostHolder.getUser())) {
            model.addAttribute("audioInfos", MusicUtil.getMusicAudioInfo(musicService.getMusicsByAlbumId(id), coverUrl));
        }
        if(UserAuthority.isUser(hostHolder.getUser())) {
            //前端选项数据
            model.addAttribute("options", entityUtil.getDetailOptions(EntityType.ALBUM.getId()));
        }
        //实体类通用信息
        model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(album, EntityType.ALBUM.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.ALBUM.getId(), id, album));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(album.getImages(), 185, EntityType.ALBUM, false));

        return "/database/itemDetail/album-detail";
    }

    //endregion

    //region ------基础增删改------

    //根据搜索条件获取专辑
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbumsByFilter(@RequestBody String json) throws JsonProcessingException {

        JsonNode param = JsonUtil.toNode(json);
        QueryParams queryParam = JsonUtil.to(param.get("queryParams"), QueryParams.class);
        String pageLabel = param.get("pageLabel").asText();

        List<AlbumVOAlpha> albums = new ArrayList<>();

        SearchResult searchResult = albumService.getAlbumsByFilter(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            albums = albumVOMapper.toVOAlpha((List<Album>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            albums = albumVOMapper.toVOAlpha((List<Album>) searchResult.data);
        }

        ObjectNode result = JsonUtil.emptyObjectNode();
        result.set("data", JsonUtil.toNode(albums));
        result.put("total", searchResult.total);

        return JsonUtil.toJson(result);
    }

    //新增专辑
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            ObjectNode param = JsonUtil.toObjectNode(json);
            //检测数据
            String checkMsg = albumService.checkAlbumJson(param);
            if(!StringUtils.isBlank(checkMsg)) {
                throw new Exception(checkMsg);
            }

            Album album = JsonUtil.to(albumService.handleAlbumJson(param), Album.class);

            //保存新增专辑
            res.message = albumService.addAlbum(album);

            // //将新增的专辑保存到meilisearch服务器索引中
            // meiliSearchUtils.saveSingleData(album, EntityType.ALBUM);

        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JsonUtil.toJson(res);
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            List<Integer> ids = new ArrayList<>();
            ArrayNode arrayNode = JsonUtil.toArrayNode(json);
            arrayNode.forEach(node -> {
                ids.add(node.get("id").asInt());
            });

            //从数据库中删除专辑
            albumService.deleteAlbums(ids);

            //删除专辑对应的music
            musicService.deleteMusicsByAlbumIds(ids);

            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.ALBUM.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JsonUtil.toJson(res);
    }

    //更新专辑基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            ObjectNode param = JsonUtil.toObjectNode(json);
            String checkMsg = albumService.checkAlbumJson(param);
            if(!StringUtils.isBlank(checkMsg)) {
                throw new Exception(checkMsg);
            }

            Album album = JsonUtil.to(albumService.handleAlbumJson(param), Album.class);

            //修改编辑时间
            album.setEditedTime(DateUtil.NOW_TIMESTAMP);
            res.message =  albumService.updateAlbum(album.getId(), album);

        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JsonUtil.toJson(res);
    }

    //endregion

    //region ------进阶增删改------

    //更新专辑音乐创作相关Artists
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumArtists(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JsonNode param = JsonUtil.toNode(json);
            int id = param.get("id").asInt();
            String artists = param.get("artists").asText();
            if (StringUtils.isBlank(artists)) {
                throw new Exception(ApiInfo.INPUT_TEXT_EMPTY);
            }
            res.message = albumService.updateAlbumArtists(id, artists);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JsonUtil.toJson(res);
    }

    //更新专辑音轨信息TrackInfo
    @RequestMapping(path = "/update-trackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JsonNode param = JsonUtil.toNode(json);
            int id = param.get("id").asInt();

            String discList = param.get("discList").asText();

            albumService.updateAlbumTrackInfo(id, discList);

            res.message = ApiInfo.UPDATE_ALBUM_TRACK_INFO_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JsonUtil.toJson(res);
    }

    @RequestMapping(value = "/get-related-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedAlbums(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JsonNode param = JsonUtil.toNode(json);
            int id = param.get("id").asInt();
            res.data = albumService.getRelatedAlbums(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JsonUtil.toJson(res);
    }

    //endregion

}
