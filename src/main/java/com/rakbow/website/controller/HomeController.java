package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.MediaFormat;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.data.book.BookType;
import com.rakbow.website.data.common.Language;
import com.rakbow.website.data.common.Region;
import com.rakbow.website.data.merch.MerchCategory;
import com.rakbow.website.service.*;
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
    private static final List<JSONObject> albumFormatSet = AlbumFormat.getAlbumFormatSet();
    private static final List<JSONObject> mediaFormatSet = MediaFormat.getMediaFormatSet();
    private static final List<JSONObject> publishFormatSet = PublishFormat.getPublishFormatSet();
    private static final List<JSONObject> bookTypeSet = BookType.getBookTypeSet();
    private static final List<JSONObject> merchCategorySet = MerchCategory.getMerchCategorySet();
    private static final List<JSONObject> regionSet = Region.getRegionSet();
    private static final List<JSONObject> languageSet = Language.getLanguageSet();

    //region ------引入实例------

    @Autowired
    private ProductService productService;
    @Autowired
    private FranchiseService franchiseService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private DiscService discService;
    @Autowired
    private BookService bookService;
    @Autowired
    private MerchService merchService;
    @Autowired
    private HostHolder hostHolder;
    @Value("${website.path.img}")
    private String imgPath;
    @Value("${website.path.audio}")
    private String audioPath;

    //endregion

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
        model.addAttribute("franchiseSet", franchiseService.getAllFranchiseSet());
        model.addAttribute("mediaFormatSet", mediaFormatSet);
        model.addAttribute("albumFormatSet", albumFormatSet);
        model.addAttribute("publishFormatSet", publishFormatSet);
        return "/album/album-index";
    }

    //获取图书首页
    @RequestMapping(path = "/db/books", method = RequestMethod.GET)
    public String getBookIndexPage(Model model) {
        model.addAttribute("justAddedBooks", bookService.getJustAddedBooks(5));
        model.addAttribute("justEditedBooks", bookService.getJustEditedBooks(5));
        model.addAttribute("popularBooks", bookService.getPopularBooks(10));
        model.addAttribute("franchiseSet", franchiseService.getAllFranchiseSet());
        model.addAttribute("bookTypeSet", bookTypeSet);
        model.addAttribute("regionSet", regionSet);
        model.addAttribute("languageSet", languageSet);
        return "/book/book-index";
    }

    //获取碟片首页
    @RequestMapping(path = "/db/discs", method = RequestMethod.GET)
    public String getDiscIndexPage(Model model) {
        model.addAttribute("justAddedDiscs", discService.getJustAddedDiscs(5));
        model.addAttribute("justEditedDiscs", discService.getJustEditedDiscs(5));
        model.addAttribute("popularDiscs", discService.getPopularDiscs(10));
        model.addAttribute("franchiseSet", franchiseService.getAllFranchiseSet());
        model.addAttribute("mediaFormatSet", mediaFormatSet);
        return "/disc/disc-index";
    }

    //获取周边首页
    @RequestMapping(path = "/db/merchs", method = RequestMethod.GET)
    public String getMerchIndexPage(Model model) {
        model.addAttribute("justAddedMerchs", merchService.getJustAddedMerchs(5));
        model.addAttribute("justEditedMerchs", merchService.getJustEditedMerchs(5));
        model.addAttribute("popularMerchs", merchService.getPopularMerchs(10));
        model.addAttribute("franchiseSet", franchiseService.getAllFranchiseSet());
        model.addAttribute("merchCategorySet", merchCategorySet);
        return "/merch/merch-index";
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
