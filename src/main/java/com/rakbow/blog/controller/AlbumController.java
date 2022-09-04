package com.rakbow.blog.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.blog.annotation.LoginRequired;
import com.rakbow.blog.entity.Album;
import com.rakbow.blog.service.AlbumService;
import com.rakbow.blog.service.ProductService;
import com.rakbow.blog.service.TagService;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-07 21:53
 * @Description:
 */
@Controller
@RequestMapping("/album")
public class AlbumController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private AlbumService albumService;
    @Autowired
    private TagService tagService;
    @Value("${blog.path.upload}")
    private String uploadPath;

    @Value("${blog.path.img}")
    private String imgPath;
    @Value("${blog.path.domain}")
    private String domain;
    @Value("${server.servlet.context-path}")
    private String contextPath;

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
        return albums.toJSONString();
    }

    // //获取单个专辑信息
    // @RequestMapping(value = "/getAlbum/{albumId}", method = RequestMethod.GET)
    // @ResponseBody
    // public JSONObject getAlbum(@PathVariable("albumId") int albumId) {
    //     return albumService.album2Json(albumService.findAlbumById(albumId));
    // }

    //获取单个专辑详细信息页面
    @LoginRequired
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getAlbumDetail(@PathVariable("id") int albumId, Model model) {
        model.addAttribute("album", albumService.album2Json(albumService.findAlbumById(albumId)));
        model.addAttribute("relatedAlbums", albumService.getRelatedAlbums(albumId));
        return "/album-detail";
    }

    //新增专辑
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    @ResponseBody
    public void insertAlbum(@RequestBody JSONObject albumJson) throws ParseException {
        albumService.insertAlbum(albumService.json2Album(albumJson));
    }

    //删除专辑(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public void deleteAlbum(@RequestBody List<JSONObject> albums) throws ParseException {
        for (JSONObject album : albums) {
            albumService.deleteAlbumById(album.getInteger("id"));
        }
    }

    //更新专辑
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public void updateAlbum(@RequestBody JSONObject albumJson) throws ParseException {
        if (albumJson != null) {
            Album album = albumService.json2Album(albumJson);
            album.setEditedTime(new Timestamp(System.currentTimeMillis()));
            if (StringUtils.isBlank(album.getImgUrl())) {
                album.setImgUrl("[]");
            }
            if (StringUtils.isBlank(album.getTrackList())) {
                album.setTrackList("[]");
            }
            System.out.println(album);
            albumService.updateAlbum(album.getId(), album);
        }
    }

    //获取所有专辑数据并加上对应的所有标签
    // @RequestMapping(value = "/getAllAlbum",method = RequestMethod.GET)
    // @ResponseBody
    // public JSONArray getAllAlbums() {
    //     List<AlbumInfo> albumInfos = albumService.toAlbumInfoList(albumService.getAll());
    //     JSONArray jsonArray = new JSONArray();
    //     for(AlbumInfo albumInfo : albumInfos){
    //         List<String> tags = new ArrayList<>();
    //         tagService.getAllTagsByItemTypeIdAndItemId(ItemType.ALBUM.getId(),albumInfo.getId())
    //                 .stream().forEach(tagId -> tags.add(tagService.selectTagById(tagId).getName()));
    //         JSONObject jsonObject = (JSONObject) JSONObject.parse(JSON.toJSONString(albumInfo));
    //         jsonObject.put("tags",tags);
    //         jsonArray.add(jsonObject);
    //     }
    //     return jsonArray;
    // }

    //保存专辑图片
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
            jo.put("url", domain + contextPath + "/album/" + albumId + "/" + fileName);
            imgJson.add(jo);
        }
        albumService.updateAlbumImgUrl(albumId, imgJson.toJSONString());
        return "redirect:/index";
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
    public List<String> getRelatedAlbums(String json) {

        JSONObject param = JSON.parseObject(json);
        Album album = albumService.findAlbumById(param.getInteger("id"));
        List<String> tmp = Arrays.asList(album.getProductId());

        return tmp;
    }
}
