package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import com.rakbow.website.util.image.CommonImageUtils;
import com.rakbow.website.util.common.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    private VisitService visitService;
    // @Autowired
    // private ElasticsearchService elasticsearchService;
    @Value("${website.path.upload}")
    private String uploadPath;
    @Value("${website.path.img}")
    private String imgPath;
    @Value("${website.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private RedisUtil redisUtil;

    private final AlbumVOMapper albumVOMapper = AlbumVOMapper.INSTANCES;
    //endregion

    //region ------获取页面------

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ModelAndView getAlbumListPage(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("mediaFormatSet", redisUtil.get("mediaFormatSet"));
        model.addAttribute("albumFormatSet", redisUtil.get("albumFormatSet"));
        model.addAttribute("publishFormatSet", redisUtil.get("publishFormatSet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        view.setViewName("/album/album-list");
        return view;
    }

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int id, Model model) {
        if (albumService.getAlbumById(id) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.ALBUM.getId(), id);

        Album album = albumService.getAlbumById(id);

        model.addAttribute("mediaFormatSet", redisUtil.get("mediaFormatSet"));
        model.addAttribute("albumFormatSet", redisUtil.get("albumFormatSet"));
        model.addAttribute("publishFormatSet", redisUtil.get("publishFormatSet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("album", albumVOMapper.album2VO(album));
        model.addAttribute("user", hostHolder.getUser());
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.ALBUM.getId(), id).getVisitNum());
        //获取相关专辑
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(id));
        return "/album/album-detail";
    }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取专辑
    @RequestMapping(value = "/get-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbumsByFilterList(@RequestBody String json) {

        JSONObject param = JSON.parseObject(json);
        JSONObject queryParams = param.getJSONObject("queryParams");
        String pageLabel = param.getString("pageLabel");

        List<AlbumVOAlpha> albums = new ArrayList<>();

        SearchResult searchResult = albumService.getAlbumsByFilter(queryParams);

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
    public String addAlbum(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(albumService.checkAlbumJson(param))) {
                    res.setErrorMessage(albumService.checkAlbumJson(param));
                    return JSON.toJSONString(res);
                }

                Album album = albumService.json2Album(albumService.handleAlbumJson(param));

                //保存新增专辑
                albumService.addAlbum(album);

                //将新增的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.ALBUM.getId(), album.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.ALBUM.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteAlbum(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONArray albums = JSON.parseArray(json);
        try {
            if (userService.checkAuthority(request).state) {
                for (int i = 0; i < albums.size(); i++) {

                    int id = albums.getJSONObject(i).getInteger("id");

                    //从数据库中删除专辑
                    albumService.deleteAlbumById(id);

                    //删除专辑对应的music
                    musicService.deleteMusicByAlbumId(id);

                    //从Elasticsearch服务器索引中删除专辑
                    // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                    //删除访问量实体
                    visitService.deleteVisit(EntityType.ALBUM.getId(), id);
                }
                res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.ALBUM.getNameZh());
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新专辑基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(albumService.checkAlbumJson(param))) {
                    res.setErrorMessage(albumService.checkAlbumJson(param));
                    return JSON.toJSONString(res);
                }

                Album album = albumService.json2Album(albumService.handleAlbumJson(param));

                //修改编辑时间
                album.setEditedTime(new Timestamp(System.currentTimeMillis()));

                albumService.updateAlbum(album.getId(), album);

                //将更新的专辑保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.ALBUM.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
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
    public String addAlbumImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(albumService.getAlbumById(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                albumService.addAlbumImages(id, images, imagesJson, imageInfosJson);

                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumImages(@RequestBody String json, HttpServletRequest request) {
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
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.getId()) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = albumService.updateAlbumImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.getId()) {
                    res.message = albumService.deleteAlbumImages(id, images);
                }else {
                    res.setErrorMessage(ApiInfo.NOT_ACTION);
                }

                //更新对应music的封面图片
                List<Music> musics = musicService.getMusicsByAlbumId(id);
                for (Music music : musics) {
                    musicService.updateMusicCoverUrl(music.getId(), albumService.getAlbumCoverUrl(id));
                }

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑音乐创作相关Artists
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumArtists(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String artists = JSON.parseObject(json).getJSONArray("artists").toString();
                if (StringUtils.isBlank(artists)) {
                    res.state = 0;
                    res.message = "输入信息为空";
                    return JSON.toJSONString(res);
                }
                albumService.updateAlbumArtists(id, artists);
                res.message = ApiInfo.UPDATE_ALBUM_ARTISTS_SUCCESS;
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

    //更新专辑音轨信息TrackInfo
    @RequestMapping(path = "/update-trackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                int id = JSON.parseObject(json).getInteger("id");

                String discList = JSON.parseObject(json).get("discList").toString();

                System.out.println(discList);

                //若discList为空
                if (Objects.equals(discList, "[]")) {
                    res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                    return JSON.toJSONString(res);
                }

                albumService.updateAlbumTrackInfo(id, discList);

                //更新对应music的封面图片
                List<Music> musics = musicService.getMusicsByAlbumId(id);
                for (Music music : musics) {
                    musicService.updateMusicCoverUrl(music.getId(), albumService.getAlbumCoverUrl(id));
                }

                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = ApiInfo.UPDATE_ALBUM_TRACK_INFO_SUCCESS;
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
    public String updateAlbumDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                albumService.updateAlbumDescription(id, description);
                res.message = ApiInfo.UPDATE_ALBUM_DESCRIPTION_SUCCESS;
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
    public String updateAlbumBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String bonus = JSON.parseObject(json).get("bonus").toString();
                albumService.updateAlbumBonus(id, bonus);
                res.message = ApiInfo.UPDATE_ALBUM_BONUS_SUCCESS;
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

    //region 废弃

    @Deprecated
    public String insertAlbumImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(albumService.getAlbumById(id).getImages());
                //新增图片的信息
                JSONArray imageInfosTmp = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosTmp, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                //创建存储专辑图片的文件夹
                Path albumImgPath = Paths.get(imgPath + "/album/" + id);

                //存储图片链接的json
                JSONArray imgJson = new JSONArray();

                if (Files.notExists(albumImgPath)) {
                    try {
                        Files.createDirectory(albumImgPath);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                for (int i = 0; i < images.length; i++) {
                    //获取json中的jo对象
                    JSONObject imageInfo = imageInfosTmp.getJSONObject(i);

                    String fileName = images[i].getOriginalFilename();
                    String suffix = fileName.substring(fileName.lastIndexOf("."));
                    if (StringUtils.isBlank(suffix)) {
                        res.setErrorMessage(ApiInfo.INCORRECT_FILE_FORMAT);
                        return JSON.toJSONString(res);
                    }
                    fileName = (imageInfo.getString("nameEn") + suffix).replaceAll(" ", "");
                    // 确定文件存放的路径
                    File dest = new File(albumImgPath + "/" + fileName);
                    try {
                        // 存储文件
                        images[i].transferTo(dest);
                    } catch (IOException e) {
                        logger.error("上传文件失败: " + e.getMessage());
                        // throw new RuntimeException("上传文件失败,服务器发生异常!", e);
                        res.setErrorMessage(ApiInfo.UPLOAD_EXCEPTION);
                        return JSON.toJSONString(res);
                    }

                    //将数据存至数据库
                    JSONObject jo = new JSONObject();
                    jo.put("url", "/db/album/" + id + "/" + fileName);
                    jo.put("nameEn", imageInfo.getString("nameEn"));
                    jo.put("nameZh", imageInfo.getString("nameZh"));
                    jo.put("type", imageInfo.getString("type"));
                    jo.put("uploadTime", CommonUtils.getCurrentTime());
                    if (imageInfo.getString("description") == null) {
                        jo.put("description", "");
                    }else {
                        jo.put("description", imageInfo.getString("description"));
                    }
                    imgJson.add(jo);
                }

                imagesJson.addAll(imgJson);
                // albumService.addAlbumImages(id, imagesJson.toJSONString());

                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }
    //endregion
}
