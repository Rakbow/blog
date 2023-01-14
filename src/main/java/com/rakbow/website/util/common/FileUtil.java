package com.rakbow.website.util.common;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-30 22:14
 * @Description:
 */
public class FileUtil {

    // 图片允许的后缀扩展名
    public static String[] IMAGE_FILE_EXTD = new String[] { "png", "bmp", "jpg", "jpeg"};

    public static boolean isFileAllowed(String fileName) {
        for (String ext : IMAGE_FILE_EXTD) {
            if (ext.equals(fileName)) {
                return true;
            }
        }
        return false;
    }
}

