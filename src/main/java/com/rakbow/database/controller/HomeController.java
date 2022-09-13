package com.rakbow.database.controller;

import com.rakbow.database.entity.Tag;
import com.rakbow.database.service.AlbumService;
import com.rakbow.database.service.ProductService;
import com.rakbow.database.service.SeriesService;
import com.rakbow.database.service.TagService;
import com.rakbow.database.service.util.AlbumUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-07-25 2:10
 * @Description:
 */
@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private ProductService productService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private TagService albumTagService;
    @Autowired
    private AlbumService albumService;
    @Value("${database.path.img}")
    private String imgPath;

    //获取首页
    @RequestMapping(path = "/index", method = RequestMethod.GET)
    public String getIndex(Model model) {
        return "/index_temp";
    }

    @RequestMapping(path = "/album-list", method = RequestMethod.GET)
    public String getAlbumList(Model model) {
        model.addAttribute("mediaFormatSet", AlbumUtil.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtil.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtil.getPublishFormatSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        return "/album-list";
    }


    @RequestMapping(path = "/album-card", method = RequestMethod.GET)
    public String getAlbumCard(Model model) {
        List<Tag> albumTags = albumTagService.getAll();
        model.addAttribute("albumTags", albumTags);
        return "/album-card";
    }

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public String getTestPage(Model model) {
        return "site/test";
    }

    //获取图像
    @RequestMapping(path = "/img/{fileName}", method = RequestMethod.GET)
    public void getAlbumImg(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/" + fileName;
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

    //获取错误页面
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }
}
