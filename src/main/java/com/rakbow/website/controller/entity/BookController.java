package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.DataActionType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.book.BookVOAlpha;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Book;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.convertMapper.BookVOMapper;
import com.rakbow.website.util.file.CommonImageUtils;
import com.rakbow.website.util.common.RedisUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private VisitService visitService;
    @Autowired
    private RedisUtil redisUtil;

    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个图书详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getBookDetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        Book book = bookService.getBookWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (book == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.BOOK.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.BOOK.getId(), id);

        model.addAttribute("bookTypeSet", redisUtil.get("bookTypeSet"));
        model.addAttribute("regionSet", redisUtil.get("regionSet"));
        model.addAttribute("languageSet", redisUtil.get("languageSet"));
        model.addAttribute("franchiseSet", redisUtil.get("franchiseSet"));
        model.addAttribute("book", bookVOMapper.book2VO(book));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getItemDetailInfo(book, EntityType.BOOK.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", visitService.getPageInfo(EntityType.BOOK.getId(), id, book.getAddedTime(), book.getEditedTime()));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtils.segmentImages(book.getImages(), 180, EntityType.BOOK, false));
        //获取相关图书
        model.addAttribute("relatedBooks", bookService.getRelatedBooks(id));
        return "/database/itemDetail/book-detail";
    }

    //endregion

    //region ------增删改查------

    //新增图书
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addBook(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Book book = bookService.json2Book(bookService.handleBookJson(param));

            //保存新增图书
            res.message = bookService.addBook(book);
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
        try {
            List<Book> books = JSON.parseArray(json).toJavaList(Book.class);
            for (Book book : books) {
                //从数据库中删除专辑
                bookService.deleteBook(book);
            }
            res.message = String.format(ApiInfo.DELETE_DATA_SUCCESS, EntityType.BOOK.getNameZh());
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
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Book book = bookService.json2Book(bookService.handleBookJson(param));

            //修改编辑时间
            book.setEditedTime(new Timestamp(System.currentTimeMillis()));

            res.message = bookService.updateBook(book.getId(), book);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取图书--列表界面
     @RequestMapping(value = "/get-books", method = RequestMethod.POST)
     @ResponseBody
     public String getBooksByFilterList(@RequestBody String json, HttpServletRequest request) {
         JSONObject param = JSON.parseObject(json);
         JSONObject queryParams = param.getJSONObject("queryParams");
         String pageLabel = param.getString("pageLabel");

         List<BookVOAlpha> books = new ArrayList<>();

         SearchResult searchResult = bookService.getBooksByFilter(queryParams, 
                 userService.getUserOperationAuthority(userService.getUserByRequest(request)));

         if (StringUtils.equals(pageLabel, "list")) {
             books = bookVOMapper.book2VOAlpha((List<Book>) searchResult.data);
         }
         if (StringUtils.equals(pageLabel, "index")) {
             books = bookVOMapper.book2VOAlpha((List<Book>) searchResult.data);
         }

         JSONObject result = new JSONObject();
         result.put("data", books);
         result.put("total", searchResult.total);

         return JSON.toJSONString(result);
     }

    //新增图片
    @RequestMapping(path = "/add-images", method = RequestMethod.POST)
    @ResponseBody
    public String addBookImages(int id, MultipartFile[] images, String imageInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (images == null || images.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            Book book = bookService.getBook(id);

            //原始图片信息json数组
            JSONArray imagesJson = JSON.parseArray(book.getImages());
            //新增图片的信息
            JSONArray imageInfosJson = JSON.parseArray(imageInfos);

            //检测数据合法性
            String errorMsg = CommonImageUtils.checkAddImages(imageInfosJson, imagesJson);
            if (!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            res.message = bookService.addBookImages(id, images, imagesJson, imageInfosJson, userService.getUserByRequest(request));
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
            //获取图书id
            int id = JSON.parseObject(json).getInteger("id");
            int action = JSON.parseObject(json).getInteger("action");
            JSONArray images = JSON.parseObject(json).getJSONArray("images");
            for (int i = 0; i < images.size(); i++) {
                images.getJSONObject(i).remove("thumbUrl");
            }

            Book book = bookService.getBook(id);

            //更新图片信息
            if (action == DataActionType.UPDATE.getId()) {

                //检测是否存在多张封面
                String errorMsg = CommonImageUtils.checkUpdateImages(images);
                if (!StringUtils.isBlank(errorMsg)) {
                    res.setErrorMessage(errorMsg);
                    return JSON.toJSONString(res);
                }

                res.message = bookService.updateBookImages(id, images.toJSONString());
            }//删除图片
            else if (action == DataActionType.REAL_DELETE.getId()) {
                res.message = bookService.deleteBookImages(book, images);
            }else {
                res.setErrorMessage(ApiInfo.NOT_ACTION);
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
            int id = JSON.parseObject(json).getInteger("id");
            String authors = JSON.parseObject(json).getJSONArray("authors").toString();
            if (StringUtils.isBlank(authors)) {
                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                return JSON.toJSONString(res);
            }

            res.message = bookService.updateBookAuthors(id, authors);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图书规格信息
    @RequestMapping(path = "/update-spec", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookSpec(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String spec = JSON.parseObject(json).getJSONArray("spec").toString();
            if (StringUtils.isBlank(spec)) {
                res.setErrorMessage(ApiInfo.INPUT_TEXT_EMPTY);
                return JSON.toJSONString(res);
            }

            res.message = bookService.updateBookSpec(id, spec);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图书描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String description = JSON.parseObject(json).get("description").toString();

            res.message = bookService.updateBookDescription(id, description);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新图书特典信息
    @RequestMapping(path = "/update-bonus", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookBonus(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String bonus = JSON.parseObject(json).get("bonus").toString();

            res.message = bookService.updateBookBonus(id, bonus);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region other

    //isbn互相转换
    @RequestMapping(value = "/get-isbn", method = RequestMethod.POST)
    @ResponseBody
    public String getISBN(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            String label = param.getString("label");
            String isbn = param.getString("isbn");

            res.data = bookService.getISBN(label, isbn);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
