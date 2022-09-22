package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.LoginRequired;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Album;
// import com.rakbow.website.service.ElasticsearchService;
import com.rakbow.website.service.util.AlbumUtil;
import com.rakbow.website.service.util.common.ApiResult;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.TagService;
import com.rakbow.website.service.util.common.ApiResultInfo;
import com.rakbow.website.service.util.common.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
import java.util.Arrays;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-07 21:53
 * @Description:
 */
@Controller
@RequestMapping("/db/album")
public class AlbumController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AlbumService albumService;
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
                res.setErrorMessage(String.format(ApiResultInfo.GET_DATA_FAILED, EntityType.ALBUM.getName()));
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

    //获取单个专辑详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int albumId, Model model) {
        if (albumService.findAlbumById(albumId) == null) {
            model.addAttribute("errorMessage", String.format(ApiResultInfo.GET_DATA_FAILED_404, EntityType.ALBUM.getName()));
            return "/error/404";
        }
        model.addAttribute("mediaFormatSet", AlbumUtil.getMediaFormatSet());
        model.addAttribute("user", hostHolder.getUser());
        model.addAttribute("albumFormatSet", AlbumUtil.getAlbumFormatSet());
        model.addAttribute("album", albumService.album2Json(albumService.findAlbumById(albumId)));
        //获取相关专辑
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(albumId));
        return "/album-detail";
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

            res.message = String.format(ApiResultInfo.INSERT_DATA_SUCCESS, EntityType.ALBUM.getName());
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
                //从数据库中删除专辑
                albumService.deleteAlbumById(albums.getJSONObject(i).getInteger("id"));
                //从Elasticsearch服务器索引中删除专辑
                // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));
            }
            res.message = String.format(ApiResultInfo.DELETE_DATA_SUCCESS, EntityType.ALBUM.getName());
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
            album.setEditedTime(new Timestamp(System.currentTimeMillis()));

            if (StringUtils.isBlank(album.getImgUrl())) {
                album.setImgUrl("[]");
            }
            if (StringUtils.isBlank(album.getArtists())) {
                album.setArtists("[]");
            }
            if (StringUtils.isBlank(album.getTrackInfo())) {
                album.setTrackInfo("{}");
            }
            album.setTrackInfo(albumService.findAlbumById(album.getId()).getTrackInfo());
            album.setImgUrl(albumService.findAlbumById(album.getId()).getImgUrl());
            album.setArtists(albumService.findAlbumById(album.getId()).getArtists());

            albumService.updateAlbum(album.getId(), album);

            //将新增的专辑保存到Elasticsearch服务器索引中
            // elasticsearchService.saveAlbum(album);

            res.message = String.format(ApiResultInfo.UPDATE_DATA_SUCCESS, EntityType.ALBUM.getName());
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

    //region 新增图片、Artists和音轨信息等
    //新增专辑图片
    @RequestMapping(path = "/upload", method = RequestMethod.POST)
    public String uploadAlbumImageSet(MultipartFile[] images, int albumId, String[] imageName, Model model) {
        if (images.length == 0 || images == null) {
            model.addAttribute("error", "您还没有选择图片!");
            return null;
        }
        //创建存储图片的文件夹
        Path albumImgPath = Paths.get(imgPath + "/album/" + albumId);

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
            String fileName = images[i].getOriginalFilename();
            String suffix = fileName.substring(fileName.lastIndexOf("."));
            if (StringUtils.isBlank(suffix)) {
                model.addAttribute("error", "文件的格式不正确!");
                return null;
            }
            fileName = imageName[i] + suffix;
            // 确定文件存放的路径
            File dest = new File(albumImgPath + "/" + fileName);
            try {
                // 存储文件
                images[i].transferTo(dest);
            } catch (IOException e) {
                logger.error("上传文件失败: " + e.getMessage());
                throw new RuntimeException("上传文件失败,服务器发生异常!", e);
            }

            JSONObject jo = new JSONObject();
            jo.put("name", fileName.substring(0, fileName.lastIndexOf(".")));
            jo.put("url", contextPath + "/album/" + albumId + "/" + fileName);
            imgJson.add(jo);
        }
        albumService.updateAlbumImgUrl(albumId, imgJson.toJSONString());
        return "redirect:/index";
    }

    //新增专辑Artists
    @RequestMapping(path = "/updateAlbumArtists", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumArtists(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String artists = JSON.parseObject(json).get("artists").toString();
            if (StringUtils.isBlank(artists)) {
                res.state = 0;
                res.message = "输入信息为空";
                return JSON.toJSONString(res);
            }
            albumService.updateAlbumArtists(id, artists);
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.state = 0;
            res.message = e.getMessage();
            return JSON.toJSONString(res);
        }
    }

    @RequestMapping(path = "/updateAlbumTrackInfo", method = RequestMethod.POST)
    @ResponseBody
    public String updateAlbumTrackInfo(@RequestBody String json){
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String discList = JSON.parseObject(json).get("discList").toString();
            if (StringUtils.isBlank(discList)) {
                res.state = 0;
                res.message = "输入信息为空";
                return JSON.toJSONString(res);
            }
            albumService.updateAlbumTrackInfo(id, discList);
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e.getMessage());
            System.out.println(e.getMessage());
            return JSON.toJSONString(res);
        }
    }
    //endregion
}
