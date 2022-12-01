package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.DataActionType;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.AlbumUtils;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.HostHolder;
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
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private ElasticsearchService elasticsearchService;
    @Autowired
    private TagService tagService;
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
    //endregion

    //region ------获取页面------

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ModelAndView getAlbumListPage(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtils.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtils.getPublishFormatSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        view.setViewName("/album/album-list");
        return view;
    }

    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getAlbumIndexPage(Model model) {
        // model.addAttribute("totalRecords", albumService.getAlbumRows());
        model.addAttribute("albums", albumService.album2JsonDisplay(albumService.getAll()));
        model.addAttribute("justAddedAlbums", albumService.getJustAddedAlbums(5));
        model.addAttribute("justEditedAlbums", albumService.getJustEditedAlbums(5));
        model.addAttribute("popularAlbums", albumService.getPopularAlbums(10));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtils.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtils.getPublishFormatSet());
        return "/album/album-index";
    }

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int albumId, Model model) throws IOException {
        if (albumService.getAlbumById(albumId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.ALBUM.getId(), albumId);

        Album album = albumService.getAlbumById(albumId);

        model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtils.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtils.getPublishFormatSet());
        model.addAttribute("productSet", productService.getAllProductSetBySeriesId(album.getSeries()));

        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());

        model.addAttribute("album", albumService.album2Json(album));
        model.addAttribute("user", hostHolder.getUser());
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.ALBUM.getId(), albumId).getVisitNum());
        //获取相关专辑
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(albumId));
        return "/album/album-detail";
    }

    //endregion

    //region ------增删改查------

    //获取指定起始行，指定数量的专辑get-album-limit
    @RequestMapping(value = "/get-album-limit", method = RequestMethod.POST)
    @ResponseBody
    public List<JSONObject> getAlbumLimit(@RequestBody String json) {
        int offset = JSONObject.parseObject(json).getInteger("offset");
        int limit = JSONObject.parseObject(json).getInteger("limit");
        String where = JSONObject.parseObject(json).getString("where");
        return albumService.album2JsonDisplay(albumService.getAlbumLimit(offset, limit, where));
    }

    //获得所有专辑
    @RequestMapping(value = "/get-all", method = RequestMethod.GET)
    @ResponseBody
    public String getAllAlbum() {
        return JSON.toJSONString(albumService.album2JsonDisplayList(albumService.getAll()));
    }

    //获取单个专辑信息
    @RequestMapping(value = "/get-album", method = RequestMethod.GET)
    @ResponseBody
    public String getAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSONObject.parseObject(json).getInteger("id");
            Album album = albumService.getAlbumById(id);
            if (album == null) {
                res.setErrorMessage(String.format(ApiInfo.GET_DATA_FAILED, EntityType.ALBUM.getNameZh()));
                return JSON.toJSONString(res);
            }
            res.data = JSON.toJSONString(albumService.album2Json(album));
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            res.setErrorMessage(ex);
            return JSON.toJSONString(res);
        }
    }

    //根据搜索条件获取专辑
    @RequestMapping(value = "/get-albums", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbumsByFilter(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject queryParam = new JSONObject();
            String series = JSONObject.parseObject(json).getString("seriesId");
            queryParam.put("seriesId", series);

            JSONArray productId = JSONObject.parseObject(json).getJSONArray("productId");
            if (productId.size() != 0) {
                String productIdString = productId.toString();
                queryParam.put("productId", productIdString.substring(productIdString.indexOf("[") + 1, productIdString.indexOf("]")));
            } else {
                queryParam.put("productId", null);
            }

            JSONArray publishFormat = JSONObject.parseObject(json).getJSONArray("publishFormat");
            if (publishFormat.size() != 0) {
                String publishFormatString = publishFormat.toString();
                queryParam.put("publishFormat", publishFormatString.substring(publishFormatString.indexOf("[") + 1, publishFormatString.indexOf("]")));
            } else {
                queryParam.put("publishFormat", null);
            }

            JSONArray albumFormat = JSONObject.parseObject(json).getJSONArray("albumFormat");
            if (albumFormat.size() != 0) {
                String albumFormatString = albumFormat.toString();
                queryParam.put("albumFormat", albumFormatString.substring(albumFormatString.indexOf("[") + 1, albumFormatString.indexOf("]")));
            } else {
                queryParam.put("albumFormat", null);
            }

            JSONArray mediaFormat = JSONObject.parseObject(json).getJSONArray("mediaFormat");
            if (mediaFormat.size() != 0) {
                String mediaFormatString = mediaFormat.toString();
                queryParam.put("mediaFormat", mediaFormatString.substring(mediaFormatString.indexOf("[") + 1, mediaFormatString.indexOf("]")));
            } else {
                queryParam.put("mediaFormat", null);
            }

            String hasBonus = JSONObject.parseObject(json).getString("hasBonus");
            if (hasBonus == null || hasBonus.equals("")) {
                queryParam.put("hasBonus", null);
            } else {
                if (hasBonus.equals("all")) {
                    queryParam.put("hasBonus", null);
                } else if (hasBonus.equals("has")) {
                    queryParam.put("hasBonus", "1");
                } else {
                    queryParam.put("hasBonus", "0");
                }
            }
            List<JSONObject> albums = new ArrayList<>();
            List<Album> searchResult = albumService.getAlbumsByFilter(queryParam.toJSONString());
            if (searchResult.size() != 0) {
                searchResult.forEach(i -> albums.add(albumService.album2JsonDisplay(i)));
            }
            res.data = albums;
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            ex.printStackTrace();
            res.state = 0;
            res.message = ex.getMessage();
            return JSON.toJSONString(res);
        }
    }

    //根据搜索条件获取专辑--列表界面
    @RequestMapping(value = "/get-albums-list", method = RequestMethod.POST)
    @ResponseBody
    public String getAlbumsByFilterList(@RequestBody String json) {
        JSONObject queryParams = JSON.parseObject(json);

        Map<String, Object> map = albumService.getAlbumsByFilterList(queryParams);

        List<JSONObject> albums = albumService.album2JsonDisplayList((List<Album>) map.get("data"));

        JSONObject result = new JSONObject();
        result.put("data", albums);
        result.put("total", map.get("total"));

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
                elasticsearchService.saveAlbum(album);

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
                    elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

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
                elasticsearchService.saveAlbum(album);

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
                JSONArray imageInfosTmp = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageUtils.checkAddImages(imageInfosTmp, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                albumService.addAlbumImages(id, images, imagesJson, imageInfosTmp);

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
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.id) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = albumService.updateAlbumImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.id) {
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
                elasticsearchService.saveAlbum(albumService.getAlbumById(id));
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
                elasticsearchService.saveAlbum(albumService.getAlbumById(id));

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
                System.out.println(description);
                albumService.updateAlbumDescription(id, description);
                res.message = ApiInfo.UPDATE_ALBUM_DESCRIPTION_SUCCESS;
                //更新elasticsearch中的专辑
                elasticsearchService.saveAlbum(albumService.getAlbumById(id));
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
                elasticsearchService.saveAlbum(albumService.getAlbumById(id));
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
                elasticsearchService.saveAlbum(albumService.getAlbumById(id));

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
