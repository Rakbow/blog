package com.rakbow.website.data;

import com.rakbow.website.util.common.CommonUtils;
import lombok.Data;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-02-08 18:04
 * @Description: 通用图片信息
 */
@Data
public class ImageInfo {

    private String url;//图片url
    private String nameEn;//图片名(英)
    private String nameZh;//图片名(中)
    private String type;//图片类型
    private String description;//图片描述
    private String uploadTime;//图片上传时间
    private String uploadUser;//图片上传用户

    public ImageInfo() {
        this.url = "";
        this.nameEn = "";
        this.nameZh = "";
        this.type = "0";
        this.description = "";
        this.uploadTime = CommonUtils.getCurrentTime();
        this.uploadUser = "";
    }

}
