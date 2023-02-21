package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.image.ImageType;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.*;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import com.rakbow.website.util.entity.MusicUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
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

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AlbumService albumService;
    @Autowired
    private MusicService musicService;
    @Autowired
    private UserService userService;
    @Autowired
    private EntityUtils entityUtils;
    @Autowired
    private EntityService entityService;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    //endregion

    //region ------获取页面------

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    @UniqueVisitor
    public String getAlbumDetail(@PathVariable("id") int id, Model model, HttpServletRequest request, HttpServletResponse response) {
        Album album = albumService.getAlbumWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (album == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getNameZh()));
            return "/error/404";
        }

        model.addAttribute("album", albumVOMapper.album2VO(album));
        if(userService.getUserOperationAuthority(userService.getUserByRequest(request)) > 0) {
            model.addAttribute("audioInfos", MusicUtil.getMusicAudioInfo(musicService.getMusicsByAlbumId(id)));
        }
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.ALBUM.getId()));
        //实体类通用信息
        model.addAttribute("detailInfo", entityUtils.getItemDetailInfo(album, EntityType.ALBUM.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.ALBUM.getId(), id, album, request));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(album.getImages(), 250, EntityType.ALBUM, false));
        //获取相关专辑
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(album));

        return "/database/itemDetail/album-detail";
    }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取专辑
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbumsByFilter(@RequestBody String json, HttpServletRequest request) {

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<AlbumVOAlpha> albums = new ArrayList<>();

        SearchResult searchResult = albumService.getAlbumsByFilter(queryParams,
                userService.getUserOperationAuthority(userService.getUserByRequest(request)));

        if (StringUtils.equals(pageLabel, "list")) {
            albums = albumVOMapper.album2VOAlpha((List<Album>) searchResult.data);
        }
        if (StringUtils.equals(pageLabel, "index")) {
            albums = albumVOMapper.album2VOAlpha((List<Album>) searchResult.data);
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

            Album album = albumService.json2Album(albumService.handleAlbumJson(param));

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

            Album album = albumService.json2Album(albumService.handleAlbumJson(param));

            //修改编辑时间
            album.setEditedTime(new Timestamp(System.currentTimeMillis()));
            res.message =  albumService.updateAlbum(album.getId(), album);

        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region 新增图片、Artists和音轨信息等

    //新增专辑图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addAlbumImages(int entityId, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            Album album = albumService.getAlbum(entityId);

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(album.getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtil.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.equals("", errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            albumService.addAlbumImages(entityId, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumImages(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            //获取专辑id
            int entityId = JSON.parseObject(json).getInteger("id");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            int action = JSON.parseObject(json).getInteger("action");

            Album album = albumService.getAlbum(entityId);

            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
                images.getJSONObject(i).remove("thumbUrl50");
            }

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMessage = CommonImageUtil.checkUpdateImages(images);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                res.message = albumService.updateAlbumImages(entityId, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = albumService.deleteAlbumImages(album, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
            }

            for (int i = 0; i < images.size(); i++) {
                //若更新图片中有封面
                JSONObject image = images.getJSONObject(i);
                if(image.getIntValue("type") == ImageType.COVER.getIndex()) {
                    //和原专辑封面URL比较,若不同,则赋值新的url
                    if(!StringUtils.equals(image.getString("url"), CommonImageUtil.getCoverUrl(images))) {
                        //更新对应music的封面图片
                        musicService.updateMusicCoverUrl(album.getId(), CommonImageUtil.getCoverUrl(JSON.parseArray(album.getImages())));
                    }
                }
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

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

            Album album = albumService.getAlbum(id);

            //若discList为空
            if (Objects.equals(discList, "[]")) {
                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                return JSON.toJSONString(res);
            }

            albumService.updateAlbumTrackInfo(id, discList);

            //更新对应music的封面图片
            musicService.updateMusicCoverUrl(album.getId(), CommonImageUtil.getCoverUrl(JSON.parseArray(album.getImages())));

            res.message = ApiInfo.UPDATE_ALBUM_TRACK_INFO_SUCCESS;
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
