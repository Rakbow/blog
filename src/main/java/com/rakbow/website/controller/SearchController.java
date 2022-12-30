// package com.rakbow.website.controller;
//
// import com.alibaba.fastjson2.JSON;
// import com.alibaba.fastjson2.JSONObject;
// import com.rakbow.website.dao.elasticsearch.AlbumRepository;
// import com.rakbow.website.data.Common.EntityType;
// import com.rakbow.website.service.AlbumService;
// import com.rakbow.website.service.ElasticsearchService;
// import com.rakbow.website.service.UserService;
// import com.rakbow.website.util.common.ApiResult;
// import org.apache.commons.lang3.StringUtils;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.web.bind.annotation.ResponseBody;
//
// import javax.servlet.http.HttpServletRequest;
// import java.io.IOException;
// import java.text.ParseException;
// import java.util.ArrayList;
// import java.util.List;
//
// /**
//  * @Project_name: website
//  * @Author: Rakbow
//  * @Create: 2022-09-13 22:18
//  * @Description:
//  */
// @Controller
// public class SearchController {
//
//     @Autowired
//     private ElasticsearchService elasticsearchService;
//     @Autowired
//     private UserService userService;
//     @Autowired
//     private AlbumService albumService;
//     @Autowired
//     private AlbumRepository albumRepository;
//
//     // s?keyword=xxx
//     @RequestMapping(path = "/s", method = RequestMethod.GET)
//     public String search(String type, String keyword, Model model) throws IOException, ParseException {
//
//         List<JSONObject> searchResult = new ArrayList<>();
//
//         //搜索类型：专辑
//         if(Integer.parseInt(type) == EntityType.ALBUM.getId()){
//             searchResult = elasticsearchService.searchAlbum(keyword);
//         }
//         model.addAttribute("searchResult", searchResult);
//         model.addAttribute("totals", (searchResult == null) ? 0 : searchResult.size());
//         model.addAttribute("keyword", keyword);
//         model.addAttribute("type", EntityType.getItemNameZhByIndex(Integer.parseInt(type)));
//         return "/site/search";
//     }
//
//     //对elasticsearch缓存数据批量操作
//     @RequestMapping(value = "/manage", method = RequestMethod.POST)
//     @ResponseBody
//     public String manageElasticsearchData(@RequestBody String json, HttpServletRequest request) {
//         ApiResult res = new ApiResult();
//         JSONObject param = JSON.parseObject(json);
//         String action = param.getString("action");
//         try {
//             if (userService.checkAuthority(request).state) {
//
//                 if(StringUtils.equals(action, "deleteAllAlbum")) {
//                     albumRepository.deleteAll();
//                 }
//                 if (StringUtils.equals(action, "saveAllAlbum")) {
//                     albumRepository.saveAll(albumService.getAll());
//                 }
//
//             } else {
//                 res.setErrorMessage(userService.checkAuthority(request).message);
//             }
//         } catch (Exception ex) {
//             res.setErrorMessage(ex.getMessage());
//         }
//         return JSON.toJSONString(res);
//     }
//
//
//
// }
