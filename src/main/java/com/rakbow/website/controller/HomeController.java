package com.rakbow.website.controller;

import com.rakbow.website.service.*;
import com.rakbow.website.util.AlbumUtils;
import com.rakbow.website.util.BookUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

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
    private DiscService discService;
    @Autowired
    private BookService bookService;
    @Autowired
    private HostHolder hostHolder;
    @Value("${website.path.img}")
    private String imgPath;
    @Value("${website.path.file}")
    private String filePath;
    @Value("${website.path.audio}")
    private String audioPath;

    //region ------获取页面------

    //获取首页
    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getIndexPage() {
        return "/index";
    }

    //获取在线数据库首页
    @RequestMapping(path = "/db", method = RequestMethod.GET)
    public ModelAndView getDatabasePage() {
        ModelAndView view = new ModelAndView();
        view.setViewName("/database");
        return view;
    }

    //获取专辑首页
    @RequestMapping(path = "/db/albums", method = RequestMethod.GET)
    public String getAlbumIndexPage(Model model) {
        model.addAttribute("justAddedAlbums", albumService.getJustAddedAlbums(5));
        model.addAttribute("justEditedAlbums", albumService.getJustEditedAlbums(5));
        model.addAttribute("popularAlbums", albumService.getPopularAlbums(10));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
        model.addAttribute("albumFormatSet", AlbumUtils.getAlbumFormatSet());
        model.addAttribute("publishFormatSet", AlbumUtils.getPublishFormatSet());
        return "/album/album-index";
    }

    //获取图书首页
    @RequestMapping(path = "/db/books", method = RequestMethod.GET)
    public String getBookIndexPage(Model model) {
        model.addAttribute("justAddedBooks", bookService.getJustAddedBooks(5));
        model.addAttribute("justEditedBooks", bookService.getJustEditedBooks(5));
        model.addAttribute("popularBooks", bookService.getPopularBooks(10));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("bookTypeSet", BookUtils.getBookTypeSet());
        return "/book/book-index";
    }

    //获取碟片首页
    @RequestMapping(path = "/db/discs", method = RequestMethod.GET)
    public String getDiscIndexPage(Model model) {
        model.addAttribute("justAddedDiscs", discService.getJustAddedDiscs(5));
        model.addAttribute("justEditedDiscs", discService.getJustEditedDiscs(5));
        model.addAttribute("popularDiscs", discService.getPopularDiscs(10));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
        return "/disc/disc-index";
    }

    //endregion

    //获取图像
    @RequestMapping(path = "/img/{fileName}", method = RequestMethod.GET)
    public void getCommonImage(@PathVariable("fileName") String fileName, HttpServletResponse response) {
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
            logger.error(ApiInfo.GET_IMAGE_FAILED + e.getMessage());
        }
    }

    //获取音频文件
    @RequestMapping(path = "/file/audio/{fileName}", method = RequestMethod.GET)
    public void getAudio(@PathVariable("fileName") String fileName, HttpServletResponse response) {
        // 服务器存放路径
        fileName = audioPath + "/" + fileName;
        // 响应文件
        response.setContentType("audio/mp3");
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
            logger.error(ApiInfo.GET_FILE_FAILED + e.getMessage());
        }
    }

    //获取错误页面
    @RequestMapping(path = "/error", method = RequestMethod.GET)
    public String getErrorPage(){
        return "/error/500";
    }

    //权限不足时
    @RequestMapping(path = "/denied", method = RequestMethod.GET)
    public String getDeniedPage(Model model) {
        model.addAttribute("errorMessage", ApiInfo.NOT_AUTHORITY_DENIED);
        return "/error/404";
    }

    //获取图像
    @RequestMapping(path = "/db/{entity}/{id}/{fileName}", method = RequestMethod.GET)
    public void getImg(@PathVariable("entity") String entity, @PathVariable("fileName") String fileName,
                       @PathVariable("id") int entityId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/" + entity + "/" + entityId + "/" + fileName;
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

    //获取缩略图
    @RequestMapping(path = "/db/{entity}/{id}/compress/{fileName}", method = RequestMethod.GET)
    public void getCompressImage(@PathVariable("entity") String entity, @PathVariable("fileName") String fileName,
                                 @PathVariable("id") int entityId, HttpServletResponse response) {
        // 服务器存放路径
        fileName = imgPath + "/compress/" + entity + "/" + entityId + "/" + fileName;
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

}
