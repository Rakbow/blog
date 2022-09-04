package com.rakbow.blog.util;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-02 0:38
 * @Description:
 */
public class CommonUtil {

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
