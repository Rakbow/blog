package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.DataActionType;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.AlbumUtil;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
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
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
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
    private UserService userService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private VisitService visitService;
    // @Autowired
    // private ElasticsearchService elasticsearchService;
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
    public ModelAndView getAlbumList(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("mediaFormatSet", AlbumUtil.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtil.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtil.getPublishFormatSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        view.setViewName("/album-list");
        return view;
    }


    @RequestMapping(path = "/card", method = RequestMethod.GET)
    public String getAlbumCard(Model model) {
        List<JSONObject> albums = new ArrayList<>();
        albumService.getAll().forEach(i -> {
            albums.add(albumService.album2JsonDisplay(i));
        });
        model.addAttribute("albums", albums);
        model.addAttribute("justAddedAlbums", albumService.getJustAddedAlbums(5));
        model.addAttribute("justEditedAlbums", albumService.getJustEditedAlbums(5));
        return "/album-card";
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    @ResponseBody
    public List<JSONObject> test() {
        List<JSONObject> albums = new ArrayList<>();
        albumService.getAll().forEach(i -> {
            albums.add(albumService.album2JsonDisplay(i));
        });
        return albums;
    }

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int albumId, Model model) {
        if (albumService.findAlbumById(albumId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getName()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.ALBUM.getId(), albumId);

        model.addAttribute("mediaFormatSet", AlbumUtil.getMediaFormatSet());
        model.addAttribute("user", hostHolder.getUser());
        model.addAttribute("albumFormatSet", AlbumUtil.getAlbumFormatSet());
        model.addAttribute("album", albumService.album2Json(albumService.findAlbumById(albumId)));
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.ALBUM.getId(), albumId).getVisitNum());
        //获取相关专辑
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(albumId));
        return "/album-detail";
    }

    //endregion

    //region ------增删改查------

    //获得所有专辑
    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ResponseBody
    public String getAllAlbum() {
        JSONArray albums = new JSONArray();
        List<Album> tmp = albumService.getAll();
        if (tmp == null || tmp.size() == 0) {
        }
        for (Album album : tmp) {
            albums.add(albumService.album2Json(album));
        }
        return JSON.toJSONString(albums);
    }

    //获取单个专辑信息
    @RequestMapping(value = "/getAlbum", method = RequestMethod.GET)
    @ResponseBody
    public String getAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSONObject.parseObject(json).getInteger("id");
            if (albumService.findAlbumById(id) == null) {
                res.setErrorMessage(String.format(ApiInfo.GET_DATA_FAILED, EntityType.ALBUM.getName()));
                return JSON.toJSONString(res);
            }
            res.data = JSON.toJSONString(albumService.album2Json(albumService.findAlbumById(id)));
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            ex.printStackTrace();
            res.state = 0;
            res.message = ex.getMessage();
            return JSON.toJSONString(res);
        }
    }

    //新增专辑
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public String insertAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            Album album = albumService.json2Album(param);

            //保存新增专辑
            albumService.insertAlbum(album);

            //将新增的专辑保存到Elasticsearch服务器索引中
            // elasticsearchService.saveAlbum(album);

            //新增访问量实体
            visitService.insertVisit(new Visit(EntityType.ALBUM.getId(), album.getId()));

            res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.ALBUM.getName());
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
            return JSON.toJSONString(res);
        }
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteAlbum(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONArray albums = JSON.parseArray(json);
        try {
            for (int i = 0; i < albums.size(); i++) {

                int id = albums.getJSONObject(i).getInteger("id");

                //从数据库中删除专辑
                albumService.deleteAlbumById(id);

                //从Elasticsearch服务器索引中删除专辑
                // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                //删除访问量实体
                visitService.deleteVisit(EntityType.ALBUM.getId(), id);
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.ALBUM.getName());
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
            return JSON.toJSONString(res);
        }
    }

    //更新专辑
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbum(@RequestBody String json) throws ParseException {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            Album album = albumService.json2Album(param);

            //修改编辑时间
            album.setEditedTime(new Timestamp(System.currentTimeMillis()));
            album.setTrackInfo(albumService.findAlbumById(album.getId()).getTrackInfo());
            album.setImages(albumService.findAlbumById(album.getId()).getImages());
            album.setArtists(albumService.findAlbumById(album.getId()).getArtists());

            albumService.updateAlbum(album.getId(), album);

            //将更新的专辑保存到Elasticsearch服务器索引中
            // elasticsearchService.saveAlbum(album);

            res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.ALBUM.getName());
            return JSON.toJSONString(res);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
            return JSON.toJSONString(res);
        }
    }

    //获取专辑图像
    @RequestMapping(path = "/{id}/{fileName}", method = RequestMethod.GET)
    public void getAlbumImg(@PathVariable("fileName") String fileName, @PathVariable("id") int albumId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/album/" + albumId + "/" + fileName;
        // 文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        // 响应图片
        response.setContentType("image/" + suffix);
        try (
                FileInputStream fis = new FileInputStream(fileName);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] buffer = new byte[1024];
            int b = 0;
            while ((b = fis.read(buffer)) != -1) {
                os.write(buffer, 0, b);
            }
        } catch (IOException e) {
            logger.error("读取图片失败: " + e.getMessage());
        }
    }

    //获取相关专辑
    @RequestMapping(path = "/getRelatedAlbums", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getRelatedAlbums(@RequestBody String json) {
        JSONObject param = JSON.parseObject(json);
        Album album = albumService.findAlbumById(param.getInteger("id"));
        List<String> tmp = Arrays.asList(album.getProductId());
        return tmp;
    }

    //endregion

    //region 新增图片、Artists和音轨信息等

    //新增专辑图片
    @RequestMapping(path = "/insertAlbumImages", method = RequestMethod.POST)
    @ResponseBody
    public String insertAlbumImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {

        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images.length == 0 || images == null) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                JSONArray imageInfosTmp = JSON.parseArray(imageInfos);

                for (Object o : imageInfosTmp) {
                    JSONObject jo = (JSONObject) o;
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
                    if(imageInfo.getString("description") == null){
                        jo.put("description", "");
                    }
                    imgJson.add(jo);
                }
                JSONArray imagesJson = JSON.parseArray(albumService.findAlbumById(id).getImages());
                imagesJson.addAll(imgJson);
                albumService.insertAlbumImages(id, imagesJson.toJSONString());
                res.message = ApiInfo.INSERT_ALBUM_IMAGES_SUCCESS;

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新专辑图片，删除或更改信息
    @RequestMapping(path = "/updateAlbumImages", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取专辑id
                int id = JSON.parseObject(json).getInteger("id");

                //更改信息
                if(JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.id){
                    String image = JSON.parseObject(json).getString("image");
                    res.message = albumService.updateAlbumImages(id, image);
                }//删除
                else if(JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.id){
                    String imageUrl = JSON.parseObject(JSON.parseObject(json).getString("image")).getString("url");
                    res.message = albumService.deleteAlbumImages(id, imageUrl);
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

    //更新专辑音乐创作相关Artists
    @RequestMapping(path = "/updateAlbumArtists", method = RequestMethod.POST)
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
    @RequestMapping(path = "/updateAlbumTrackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            System.out.println(json);
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String discList = JSON.parseObject(json).get("discList").toString();
                if (StringUtils.isBlank(discList)) {
                    res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                    return JSON.toJSONString(res);
                }
                albumService.updateAlbumTrackInfo(id, discList);
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
    @RequestMapping(path = "/updateAlbumDescription", method = RequestMethod.POST)
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
    @RequestMapping(path = "/updateAlbumBonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String bonus = JSON.parseObject(json).get("bonus").toString();
                albumService.updateAlbumBonus(id, bonus);
                res.message = ApiInfo.UPDATE_ALBUM_BONUS_SUCCESS;
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
