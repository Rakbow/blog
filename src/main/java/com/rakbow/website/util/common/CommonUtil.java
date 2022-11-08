package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.clazz.ImageProperty;
import com.rakbow.website.entity.LoginTicket;
import com.rakbow.website.entity.User;
import com.rakbow.website.service.UserService;
import com.rakbow.website.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.PushBuilder;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-02 0:38
 * @Description:
 */
public class CommonUtil {

    //删除服务器上的文件
    //dir: 文件夹路径，fileName: 文件名（不包含后缀）
    public static void deleteFile(Path dir, String fileName){
        File[] files = new File(dir.toUri()).listFiles();
        for (int i = 0; i < files.length; i++) {
            if(files[i].getName().substring(0, files[i].getName().lastIndexOf(".")).equals(fileName)){
                files[i].delete();
            }
        }
    }

    //计算时间总和（返回字符串形式）
    public static String countTotalTime(List<String> times){
        int totalHour = 0;
        int totalMin = 0;
        int totalSecond = 0;
        String hour = "";
        String min = "";
        String sec = "";

        for(String time : times){
            //如果有两个":"表示时长在一小时以上
            if(time.indexOf(":") != time.lastIndexOf(":")){
                totalHour += Integer.parseInt(time.substring(0,time.indexOf(":")));
                totalMin += Integer.parseInt(time.substring(time.indexOf(":")+1, time.lastIndexOf(":")));
                totalSecond += Integer.parseInt(time.substring(time.lastIndexOf(":")+1));
            }else {
                totalMin += Integer.parseInt(time.substring(0, time.indexOf(":")));
                totalSecond += Integer.parseInt(time.substring(time.indexOf(":")+1));
            }

            //处理进位
            if(totalSecond >= 60){
                totalSecond = totalSecond%60;
                totalMin++;
                if(totalMin >= 60){
                    totalMin = totalMin%60;
                    totalHour++;
                }
            }else {
                if(totalMin >= 60){
                    totalMin = totalMin%60;
                    totalHour++;
                }
            }
        }
        hour = (totalHour < 10)? "0" + totalHour:Integer.toString(totalHour);
        min = (totalMin < 10)? "0" + totalMin:Integer.toString(totalMin);
        sec = (totalSecond < 10)? "0" + totalSecond:Integer.toString(totalSecond);
        if(hour.equals("00")){
            return min + ":" + sec;
        }else {
            return hour + ":" + min + ":" + sec;
        }
    }

    //生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    //MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }


    //日期转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static String dateToString(Date date) {
        if (date != null) {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy/MM/dd");
            return ft.format(date);
        } else {
            return null;
        }
    }

    //时间转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static String timestampToString(Timestamp ts){
        if (ts != null) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
            return localDateTime.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        }else {
            return null;
        }
    }

    //时间转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static Timestamp stringToTimestamp(String ts){
        if (ts != null) {
            return Timestamp.valueOf(ts.replaceAll("/", "-"));
        }else {
            return null;
        }
    }

    public static String getCurrentTime(){
        DateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        return sdf.format(new Timestamp(System.currentTimeMillis()));
    }

    //字符串转为时间(自定义格式)，例如：yyyy/MM/dd
    public static Date stringToDate(String dateString, String dateFormat) throws ParseException {
        SimpleDateFormat ft = new SimpleDateFormat(dateFormat);
        return ft.parse(dateString);
    }

    //获取JSON字符串
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        json.put("msg", msg);
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    //递归获取jsonObject的所有value
    public static String getAllContentFromJson(Object object) {
        StringBuffer mStringBuffer = new StringBuffer();
        if(object instanceof JSONObject) {
            JSONObject jsonObject = (JSONObject) object;
            for (Map.Entry<String, Object> entry: jsonObject.entrySet()) {
                Object o = entry.getValue();
                if(o instanceof Integer){
                    mStringBuffer.append(" "+entry.getValue());
                }else if(o instanceof Double){
                    mStringBuffer.append(" "+entry.getValue());
                }else if(o instanceof Float){
                    mStringBuffer.append(" "+entry.getValue());
                }else if(o instanceof Byte){
                    mStringBuffer.append(" "+entry.getValue());
                }else if(o instanceof Long){
                    mStringBuffer.append(" "+entry.getValue());
                }else if(o instanceof String) {
                    try{
                        object= JSONObject.parseObject((String)o);
                        getAllContentFromJson(object);
                    }catch (Exception e){
                        mStringBuffer.append(" "+entry.getValue());
                    }
                }
                else {
                    getAllContentFromJson(o);
                }
            }
        }
        if(object instanceof JSONArray) {
            JSONArray jsonArray = (JSONArray) object;
            for(int i = 0; i < jsonArray.size(); i ++) {
                getAllContentFromJson(jsonArray.get(i));
            }
        }
        return mStringBuffer.toString();
    }

    /**
     * 使用 Stream 去重list
     * @param list
     */
    public static <T> List<T> removeDuplicateList(List<T> list) {
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 使用 通过图片url获取字节大小，长宽
     * @param imgUrl
     */
    public static ImageProperty getImageProperty(String imgUrl) throws IOException {
        ImageProperty img = new ImageProperty();

        File file = new File(imgUrl);

        // 图片对象
        BufferedImage bufferedImage = ImageIO.read(new FileInputStream(file));

        img.setSize(file.length());
        img.setWidth(bufferedImage.getWidth());
        img.setHeight(bufferedImage.getHeight());

        return img;
    }

}
