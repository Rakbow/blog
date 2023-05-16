// package com.rakbow.website.util.common;
//
// import com.fasterxml.jackson.annotation.JsonInclude;
// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.core.type.TypeReference;
// import com.fasterxml.jackson.databind.*;
// import com.fasterxml.jackson.databind.node.ArrayNode;
// import com.fasterxml.jackson.databind.node.JsonNodeFactory;
// import com.fasterxml.jackson.databind.node.ObjectNode;
// import lombok.extern.slf4j.Slf4j;
// import org.apache.commons.lang3.StringUtils;
//
// import java.util.ArrayList;
// import java.util.Arrays;
// import java.util.List;
// import java.util.Map;
//
// /**
//  * @Project_name: website
//  * @Author: Rakbow
//  * @Create: 2023-05-10 12:53
//  * @Description:
//  */
// @Slf4j
// public class JsonUtil {
//
//     public static final JsonNodeFactory nodeFactory = JsonNodeFactory.instance;
//
//     private static final ObjectMapper objectMapper = new ObjectMapper();
//     // 日期格式化
//     private static final String STANDARD_FORMAT = "yyyy/MM/dd";
//
//     static {
//         //对象的所有字段全部列入
//         objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
//         //取消默认转换timestamps形式
//         objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,false);
//         //忽略空Bean转json的错误
//         objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS,false);
//         //所有的日期格式都统一为以下的样式，即yyyy/MM/dd
// //        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
//         //忽略 在json字符串中存在，但是在java对象中不存在对应属性的情况。防止错误
//         objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,false);
//     }
//
//     /**
//      * 对象转Json格式字符串
//      * @param obj 对象
//      * @return Json格式字符串
//      */
//     public static <T> String toJson(T obj) {
//         if (obj == null) {
//             return null;
//         }
//         try {
//             return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> String toJson(JsonNode jsonNode) {
//         if (jsonNode == null) {
//             return null;
//         }
//         try {
//             return objectMapper.writeValueAsString(jsonNode);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     /**
//      * 对象转Json格式字符串(格式化的Json字符串)
//      * @param obj 对象
//      * @return 美化的Json格式字符串
//      */
//     public static <T> String toJsonPretty(T obj) {
//         if (obj == null) {
//             return null;
//         }
//         try {
//             return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     /**
//      * 字符串转换为自定义对象
//      * @param json 要转换的字符串
//      * @param clazz 自定义对象的class对象
//      * @return 自定义对象
//      */
//     public static <T> T to(String json, Class<T> clazz){
//         if(StringUtils.isEmpty(json) || clazz == null){
//             return null;
//         }
//         try {
//             return clazz.equals(String.class) ? (T) json : objectMapper.readValue(json, clazz);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> T to(JsonNode jsonNode, Class<T> clazz) {
//         try {
//             return objectMapper.treeToValue(jsonNode, clazz);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> T to(String json, TypeReference<T> valueTypeRef) {
//         try {
//             return objectMapper.readValue(json, valueTypeRef);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     @SuppressWarnings("rawtypes")
//     public static Map toMap(String json) {
//         try {
//             return objectMapper.readValue(json, Map.class);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> List<T> toList(String json, TypeReference<List<T>> typeReference) {
//         try {
//             return objectMapper.readValue(json, typeReference);
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> List<T> toList(JsonNode jsonList, Class<T> clazz) {
//         try {
//             if (!jsonList.isArray()) {
//                 throw new IllegalArgumentException("Input JsonNode is not a JSON array");
//             }
//             List<T> res = new ArrayList<>();
//             for(JsonNode node : jsonList) {
//                 T t = objectMapper.treeToValue(node, clazz);
//                 res.add(t);
//             }
//             return res;
//         } catch (Exception e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static JsonNode toNode(String json) {
//         try {
//             return objectMapper.readTree(json);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static JsonNode toNode(Object obj) {
//         return objectMapper.valueToTree(obj);
//     }
//
//     public static <T> JsonNode toNode(List<T> list) {
//         ArrayNode arrayNode = objectMapper.createArrayNode();
//         // 遍历列表，将每个元素转换为 JsonNode 对象并添加到 ArrayNode 中
//         for (T element : list) {
//             JsonNode jsonNode = objectMapper.convertValue(element, JsonNode.class);
//             arrayNode.add(jsonNode);
//         }
//         // 返回生成的 ArrayNode 对象
//         return arrayNode;
//     }
//
//     public static ObjectNode emptyObjectNode() {
//         return nodeFactory.objectNode();
//     }
//
//     public static ArrayNode emptyArrayNode() {
//         return nodeFactory.arrayNode();
//     }
//
//     public static <T> ObjectNode toObjectNode(String json) {
//         try {
//             return objectMapper.readValue(json, ObjectNode.class);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> ArrayNode toArrayNode(String json) {
//         try {
//             return objectMapper.readValue(json, ArrayNode.class);
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> ArrayNode toArrayNode(String[] array) {
//         return objectMapper.valueToTree(array);
//     }
//
//     public static <T> List<T> toList(String json, Class<T> clazz) {
//         return toList(toNode(json), clazz);
//     }
//
//     public static <T> List<T> toList(Object obj, Class<T> clazz) {
//         JavaType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, clazz);
//         return objectMapper.convertValue(obj, listType);
//     }
//
//     public static String[] toStringArray(JsonNode node) {
//         ArrayNode arrayNode = (ArrayNode)node;
//         if(!arrayNode.isArray()) {
//             return new String[0];
//         }
//         if(arrayNode.isEmpty()) {
//             return new String[0];
//         }
//         String[] result = new String[arrayNode.size()];
//         for (int i = 0; i < arrayNode.size(); i++) {
//             JsonNode element = arrayNode.get(i);
//             result[i] = element;
//         }
//         Arrays.sort(result);
//         return result;
//     }
//
//     public static <T> List<T> getList(String json, Class<T> clazz) {
//         try {
//             ObjectMapper objectMapper = new ObjectMapper();
//             return objectMapper.readValue(json, new TypeReference<>() {
//             });
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
//     public static <T> List<T> getList(Object obj, Class<T> clazz) {
//         try {
//             String json = objectMapper.writeValueAsString(obj);
//             return objectMapper.readValue(json, new TypeReference<>() {
//             });
//         } catch (JsonProcessingException e) {
//             throw new RuntimeException(e);
//         }
//     }
//
// }
