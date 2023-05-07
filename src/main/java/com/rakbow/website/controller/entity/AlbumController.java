package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.controller.interceptor.TokenInterceptor;
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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        //前端选项数据
        model.addAttribute("options", entityUtil.getDetailOptions(EntityType.ALBUM.getId()));
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
    public String getAlbumsByFilter(@RequestBody String json) {

        JSONObject param = JSON.parseObject(json);

        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));

        String pageLabel = param.getString("pageLabel");

        List<AlbumVOAlpha> albums = new ArrayList<>();

        SearchResult searchResult = albumService.getAlbumsByFilter(queryParam);

        if (StringUtils.equals(pageLabel, "list")) {
            albums = albumVOMapper.toVOAlpha((List<Album>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            albums = albumVOMapper.toVOAlpha((List<Album>) searchResult.data);
        }

        JSONObject result = new JSONObject();
        result.put("data", albums);
        result.put("total", searchResult.total);

        return JSON.toJSONString(result);
    }

    //新增专辑
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String checkMsg = albumService.checkAlbumJson(param);
            if(!StringUtils.isBlank(checkMsg)) {
                res.setErrorMessage(checkMsg);
                return JSON.toJSONString(res);
            }

            Album album = entityService.json2Entity(albumService.handleAlbumJson(param), Album.class);

            //保存新增专辑
            res.message = albumService.addAlbum(album);

            // //将新增的专辑保存到meilisearch服务器索引中
            // meiliSearchUtils.saveSingleData(album, EntityType.ALBUM);

        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            List<Album> albums = JSON.parseArray(json).toJavaList(Album.class);

            for (Album album : albums) {
                //从数据库中删除专辑
                albumService.deleteAlbumById(album);

                //删除专辑对应的music
                try {
                    musicService.deleteMusicByAlbumId(album.getId());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.ALBUM.getNameZh());
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新专辑基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            String checkMsg = albumService.checkAlbumJson(param);
            if(!StringUtils.isBlank(checkMsg)) {
                res.setErrorMessage(checkMsg);
                return JSON.toJSONString(res);
            }

            Album album = entityService.json2Entity(albumService.handleAlbumJson(param), Album.class);

            //修改编辑时间
            album.setEditedTime(DateUtil.NOW_TIMESTAMP);
            res.message =  albumService.updateAlbum(album.getId(), album);

        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶增删改------

    //更新专辑音乐创作相关Artists
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumArtists(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String artists = JSON.parseObject(json).getJSONArray("artists").toString();
            if (StringUtils.isBlank(artists)) {
                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                return JSON.toJSONString(res);
            }
            res.message = albumService.updateAlbumArtists(id, artists);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑音轨信息TrackInfo
    @RequestMapping(path = "/update-trackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");

            String discList = JSON.parseObject(json).get("discList").toString();

//            //若discList为空
//            if (Objects.equals(discList, "[]")) {
//                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
//                return JSON.toJSONString(res);
//            }

            albumService.updateAlbumTrackInfo(id, discList);

            res.message = ApiInfo.UPDATE_ALBUM_TRACK_INFO_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    @RequestMapping(value = "/get-related-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedAlbums(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = albumService.getRelatedAlbums(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
