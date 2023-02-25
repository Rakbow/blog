package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.meilisearch.sdk.exceptions.MeilisearchException;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.util.common.HostHolder;
import com.rakbow.website.util.common.MeiliSearchUtils;
import com.rakbow.website.util.common.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
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

    //region ------引入实例------
    @Value("${website.path.img}")
    private String imgPath;
    @Value("${website.path.audio}")
    private String audioPath;
    @Autowired
    private MeiliSearchUtils meiliSearchUtils;

    //endregion

    @RequestMapping(path = "", method = RequestMethod.GET)
    public String getIndexPage() {
        return "/index";
    }

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

    // @RequestMapping(path = "/db/simpleSearch", method = RequestMethod.POST)
    // @ResponseBody
    // public String simpleSearch(@RequestBody String json, HttpServletRequest request) throws MeilisearchException {
    //     ApiResult res = new ApiResult();
    //     try {
    //         String keyword = JSON.parseObject(json).getString("keyword");
    //         int entityType = JSON.parseObject(json).getInteger("entityType");
    //         int offset = JSON.parseObject(json).getInteger("offset");
    //         int limit = JSON.parseObject(json).getInteger("limit");
    //
    //         res.data = meiliSearchUtils.simpleSearch(keyword, entityType, offset, limit);
    //     } catch (Exception e) {
    //         res.setErrorMessage(e);
    //     }
    //     return JSON.toJSONString(res);
    // }

}
