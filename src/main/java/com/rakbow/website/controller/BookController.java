package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.DataActionType;
import com.rakbow.website.data.common.EntityType;
import com.rakbow.website.entity.Book;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.util.BookUtils;
import com.rakbow.website.util.Image.CommonImageHandleUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.common.HostHolder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-30 10:18
 * @Description:
 */

@Controller
@RequestMapping("/db/book")
public class BookController {

    //region ------引入实例------

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private BookService bookService;
    @Autowired
    private UserService userService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private HostHolder hostHolder;

    //endregion

    //region ------获取页面------

    @RequestMapping(path = "/list", method = RequestMethod.GET)
    public ModelAndView getBookListPage(Model model) {
        ModelAndView view = new ModelAndView();
        model.addAttribute("books", null);
        model.addAttribute("bookTypeSet", BookUtils.getBookTypeSet());
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
        view.setViewName("/book/book-list");
        return view;
    }

    // @RequestMapping(path = "/index", method = RequestMethod.GET)
    // public String getAlbumIndexPage(Model model) {
    //     model.addAttribute("albums", albumService.album2JsonDisplay(albumService.getAll()));
    //     model.addAttribute("justAddedAlbums", albumService.getJustAddedAlbums(5));
    //     model.addAttribute("justEditedAlbums", albumService.getJustEditedAlbums(5));
    //     model.addAttribute("popularAlbums", albumService.getPopularAlbums(10));
    //     model.addAttribute("seriesSet", seriesService.getAllSeriesSet());
    //     model.addAttribute("mediaFormatSet", AlbumUtils.getMediaFormatSet());
    //     model.addAttribute("albumFormatSet", AlbumUtils.getAlbumFormatSet());
    //     model.addAttribute("publishFormatSet", AlbumUtils.getPublishFormatSet());
    //     return "/album/album-index";
    // }

    //获取单个图书详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getBookDetail(@PathVariable("id") int id, Model model) throws IOException {
        if (bookService.getBookById(id) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.BOOK.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.BOOK.getId(), id);

        Book book = bookService.getBookById(id);

        model.addAttribute("bookTypeSet", BookUtils.getBookTypeSet());
        model.addAttribute("productSet", productService.getAllProductSetBySeriesId(book.getSeries()));
        model.addAttribute("seriesSet", seriesService.getAllSeriesSet());

        model.addAttribute("book", bookService.book2Json(book));
        model.addAttribute("user", hostHolder.getUser());
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.BOOK.getId(), id).getVisitNum());
        //获取相关图书
        model.addAttribute("relatedBooks", bookService.getRelatedBooks(id));
        return "/book/book-detail";
    }

    //endregion

    //region ------增删改查------

    //根据搜索条件获取图书--列表界面
//    @RequestMapping(value = "/get-books-list", method = RequestMethod.POST)
//    @ResponseBody
//    public String getBooksByFilterList(@RequestBody String json) {
//        JSONObject queryParams = JSON.parseObject(json);
//
//        Map<String, Object> map = bookService.getBooksByFilterList(queryParams);
//
//        List<JSONObject> books = bookService.book2JsonList((List<Book>) map.get("data"));
//
//        JSONObject result = new JSONObject();
//        result.put("data", books);
//        result.put("total", map.get("total"));
//
//        return JSON.toJSONString(result);
//    }

    //新增图书
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addBook(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {

                //检测数据
                if(!StringUtils.isBlank(bookService.checkBookJson(param))) {
                    res.setErrorMessage(bookService.checkBookJson(param));
                    return JSON.toJSONString(res);
                }

                Book book = bookService.json2Book(bookService.handleBookJson(param));

                //保存新增图书
                bookService.addBook(book);

                //将新增的图书保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                //新增访问量实体
                visitService.insertVisit(new Visit(EntityType.BOOK.getId(), book.getId()));

                res.message = String.format(ApiInfo.INSERT_DATA_SUCCESS, EntityType.BOOK.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //删除图书(单个/多个)
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    @ResponseBody
    public String deleteBook(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONArray books = JSON.parseArray(json);
        try {
            if (userService.checkAuthority(request).state) {
                for (int i = 0; i < books.size(); i++) {

                    int id = books.getJSONObject(i).getInteger("id");

                    //从数据库中删除图书
                    bookService.deleteBookById(id);

                    //从Elasticsearch服务器索引中删除图书
                    // elasticsearchService.deleteAlbum(albums.getJSONObject(i).getInteger("id"));

                    //删除访问量实体
                    visitService.deleteVisit(EntityType.BOOK.getId(), id);
                }
                res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.BOOK.getNameZh());
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新图书基础信息
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateBook(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            if (userService.checkAuthority(request).state) {
                //检测数据
                if(!StringUtils.isBlank(bookService.checkBookJson(param))) {
                    res.setErrorMessage(bookService.checkBookJson(param));
                    return JSON.toJSONString(res);
                }

                Book book = bookService.json2Book(bookService.handleBookJson(param));

                //修改编辑时间
                book.setEditedTime(new Timestamp(System.currentTimeMillis()));

                bookService.updateBook(book.getId(), book);

                //将更新的图书保存到Elasticsearch服务器索引中
                // elasticsearchService.saveAlbum(album);

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.BOOK.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取图书--列表界面
     @RequestMapping(value = "/get-books-list", method = RequestMethod.POST)
     @ResponseBody
     public String getBooksByFilterList(@RequestBody String json) {
         JSONObject queryParams = JSON.parseObject(json);

         Map<String, Object> map = bookService.getBooksByFilterList(queryParams);

         List<JSONObject> books = bookService.book2JsonList((List<Book>) map.get("data"));

         JSONObject result = new JSONObject();
         result.put("data", books);
         result.put("total", map.get("total"));

         return JSON.toJSONString(result);
     }

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addBookImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                if (images == null || images.length == 0) {
                    res.setErrorMessage(ApiInfo.INPUT_IMAGE_EMPTY);
                    return JSON.toJSONString(res);
                }

                //原始图片信息json数组
                JSONArray imagesJson = JSON.parseArray(bookService.getBookById(id).getImages());
                //新增图片的信息
                JSONArray imageInfosJson = JSON.parseArray(imageInfos);

                //检测数据合法性
                String errorMessage = CommonImageHandleUtils.checkAddImages(imageInfosJson, imagesJson);
                if (!StringUtils.equals("", errorMessage)) {
                    res.setErrorMessage(errorMessage);
                    return JSON.toJSONString(res);
                }

                bookService.addBookImages(id, images, imagesJson, imageInfosJson);

                //更新elasticsearch中的图书
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));

                res.message = String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.BOOK.getNameZh());

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图片，删除或更改信息
    @RequestMapping(path = "/update-images", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookImages(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {

                //获取图书id
                int id = JSON.parseObject(json).getInteger("id");
                JSONArray images = JSON.parseObject(json).getJSONArray("images");
                for (int i = 0; i < images.size(); i++) {
                    images.getJSONObject(i).remove("thumbUrl");
                }

                //更新图片信息
                if (JSON.parseObject(json).getInteger("action") == DataActionType.UPDATE.id) {

                    //检测是否存在多张封面
                    String errorMessage = CommonImageHandleUtils.checkUpdateImages(images);
                    if (!StringUtils.equals("", errorMessage)) {
                        res.setErrorMessage(errorMessage);
                        return JSON.toJSONString(res);
                    }

                    res.message = bookService.updateBookImages(id, images.toJSONString());
                }//删除图片
                else if (JSON.parseObject(json).getInteger("action") == DataActionType.REAL_DELETE.id) {
                    res.message = bookService.deleteBookImages(id, images);
                }else {
                    res.setErrorMessage(ApiInfo.NOT_ACTION);
                }

            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图书作者信息
    @RequestMapping(path = "/update-authors", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookAuthors(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String authors = JSON.parseObject(json).getJSONArray("authors").toString();
                bookService.updateBookAuthors(id, authors);
                res.message = ApiInfo.UPDATE_BOOK_AUTHOR_SUCCESS;
                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //更新图书规格信息
    @RequestMapping(path = "/update-spec", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookSpec(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String spec = JSON.parseObject(json).getJSONArray("spec").toString();
                bookService.updateBookSpec(id, spec);
                res.message = ApiInfo.UPDATE_BOOK_SPEC_SUCCESS;
                //更新elasticsearch中的专辑
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //更新图书描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                bookService.updateBookDescription(id, description);
                res.message = ApiInfo.UPDATE_BOOK_DESCRIPTION_SUCCESS;
                //更新elasticsearch中的图书
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //更新图书特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String bonus = JSON.parseObject(json).get("bonus").toString();
                bookService.updateBookBonus(id, bonus);
                res.message = ApiInfo.UPDATE_BOOK_BONUS_SUCCESS;
                //更新elasticsearch中的图书
                // elasticsearchService.saveAlbum(albumService.getAlbumById(id));
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //endregion


}
