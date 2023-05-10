package com.rakbow.website.util.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-10 12:53
 * @Description:
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 日期格式化
    private static final String STANDARD_FORMAT = "yyyy/MM/dd HH:mm:ss";

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
        //所有的日期格式都统一为以下的样式，即yyyy/MM/dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
    }

    /**
     * 对象转Json格式字符串
     * @param obj 对象
     * @return Json格式字符串
     */
    public static <T> String toJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String toJsonPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 字符串转换为自定义对象
     * @param json 要转换的字符串
     * @param clazz 自定义对象的class对象
     * @return 自定义对象
     */
    public static <T> T to(String json, Class<T> clazz){
        if(StringUtils.isEmpty(json) || clazz == null){
            return null;
        }
        try {
            return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T to(String json, TypeReference<T> valueTypeRef) {
        try {
            return objectMapper.readValue(json, valueTypeRef);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("rawtypes")
    public static Map toMap(String json) {
        try {
            return objectMapper.readValue(json, Map.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> toList(String json, TypeReference<List<T>> typeReference) {
        try {
            return objectMapper.readValue(json, typeReference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonNode toTree(String json) {
        try {
            return objectMapper.readTree(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
