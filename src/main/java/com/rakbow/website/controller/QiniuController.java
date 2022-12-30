package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.service.QiniuService;
import com.rakbow.website.util.common.ActionResult;
import com.rakbow.website.util.common.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-30 22:12
 * @Description:
 */
@Controller
@RequestMapping("/qiniu")
@Slf4j
public class QiniuController {

    @Autowired
    private QiniuService qiniuService;


    @RequestMapping(value="/upload-img-test", method= RequestMethod.POST)
    @ResponseBody
    public String uploadImgTest(@RequestParam("file") MultipartFile file, HttpServletRequest request) {

        ApiResult res = new ApiResult();

        if(file.isEmpty()) {
            res.setErrorMessage("文件为空，请重新上传");
            return JSON.toJSONString(res);
        }

        try {
            String url = qiniuService.saveImageTest(file);
            if (!url.contains("http")) {
                res.setErrorMessage(url);
                return JSON.toJSONString(res);
            }
            res.data = url;
            res.message = "上传成功";
            return JSON.toJSONString(res);
        } catch (IOException e) {
            e.printStackTrace();
            res.setErrorMessage("文件上传发生异常！");
            return JSON.toJSONString(res);
        }
    }

    @RequestMapping(value="/upload-img", method= RequestMethod.POST)
    @ResponseBody
    public String uploadImgByPath(int entityType, int entityId, MultipartFile file, HttpServletRequest request) {

        ApiResult res = new ApiResult();
        try {

            if(file.isEmpty()) {
                res.setErrorMessage("文件为空，请重新上传");
                return JSON.toJSONString(res);
            }

            String entityTypeName = "";
            String filePath = "";

            if (entityType == EntityType.ALBUM.getId()) {
                entityTypeName = EntityType.ALBUM.getNameEn().toLowerCase();
            }

            filePath = entityTypeName + "/" + entityId + "/";

            ActionResult ar = qiniuService.saveImage(file, filePath);
            if (!ar.state) {
                res.setErrorMessage(ar.message);
            }else {
                res.data = ar.data;
                res.message = "上传成功";
            }
            return JSON.toJSONString(res);
        } catch (IOException e) {
            e.printStackTrace();
            res.setErrorMessage("文件上传发生异常！");
            return JSON.toJSONString(res);
        }
    }

    @RequestMapping(value="/delete-img", method= RequestMethod.POST)
    @ResponseBody
    public String deleteImg(int entityType, int entityId, String fileName, HttpServletRequest request) {

        ApiResult res = new ApiResult();

        String fullFileName = EntityType.getItemNameEnByIndex(entityType).toLowerCase() + "/" + entityId + "/" + fileName;

        ActionResult ar = qiniuService.deleteFileFromQiniu(fullFileName);
        if (!ar.state) {
            res.setErrorMessage(ar.message);
        }else {
            res.message = "删除成功";
        }
        return JSON.toJSONString(res);
    }
}


