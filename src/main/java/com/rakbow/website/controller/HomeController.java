package com.rakbow.website.controller;

import com.rakbow.website.entity.Tag;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.ProductService;
import com.rakbow.website.service.SeriesService;
import com.rakbow.website.service.TagService;
import com.rakbow.website.service.util.AlbumUtil;
import com.rakbow.website.service.util.common.HostHolder;
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
 * @Project_name: website
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
    @Autowired
    private HostHolder hostHolder;
    @Value("${website.path.img}")
    private String imgPath;

    //获取首页
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getIndex(Model model) {
        model.addAttribute("user", hostHolder.getUser());
        return "/index";
    }

    @RequestMapping(path = "/db/album-list", method = RequestMethod.GET)
    public String getAlbumList(Model model) {
        model.addAttribute("mediaFormatSet", AlbumUtil.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtil.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtil.getPublishFormatSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        return "/album-list";
    }


    @RequestMapping(path = "/db/album-card", method = RequestMethod.GET)
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
