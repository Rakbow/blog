package com.rakbow.website.util.common;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.entity.LoginTicket;
import com.rakbow.website.entity.User;
import com.rakbow.website.service.UserService;
import com.rakbow.website.util.CookieUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.PushBuilder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-08-02 0:38
 * @Description:
 */
public class CommonUtil {

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


    //时间转为字符串(自定义格式)，例如：yyyy/MM/dd
    public static String dateToString(Date date, String dateFormat) {
        if (date != null) {
            SimpleDateFormat ft = new SimpleDateFormat(dateFormat);
            return ft.format(date);
        } else {
            return null;
        }
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

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }

}
