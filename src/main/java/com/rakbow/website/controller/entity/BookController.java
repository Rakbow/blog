package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.controller.UserController;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.dto.QueryParams;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.book.BookVOAlpha;
import com.rakbow.website.entity.Book;
import com.rakbow.website.service.*;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.common.EntityUtil;
import com.rakbow.website.util.convertMapper.entity.BookVOMapper;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    @Resource
    private BookService bookService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtil entityUtil;
    @Resource
    private EntityService entityService;

    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;

    //endregion

    //region ------获取页面------

    //获取单个图书详细信息页面
    @UniqueVisitor
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getBookDetail(@PathVariable("id") Integer id, Model model, HttpServletRequest request) {
        Book book = bookService.getBookWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (book == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.BOOK.getNameZh()));
            return "/error/404";
        }
        model.addAttribute("book", bookVOMapper.book2VO(book));
        //前端选项数据
        model.addAttribute("options", entityUtil.getDetailOptions(EntityType.BOOK.getId()));
        //实体类通用信息
        model.addAttribute("detailInfo", entityUtil.getItemDetailInfo(book, EntityType.BOOK.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.BOOK.getId(), id, book));
        //图片相关
        model.addAttribute("itemImageInfo", CommonImageUtil.segmentImages(book.getImages(), 180, EntityType.BOOK, false));
        //获取相关图书
        // model.addAttribute("relatedBooks", bookService.getRelatedBooks(id));
        return "/database/itemDetail/book-detail";
    }

    //endregion

    //region ------增删改查------

    //新增图书
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addBook(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            JSONObject param = JSON.parseObject(json);
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Book book = entityService.json2Entity(bookService.handleBookJson(param), Book.class);

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
    public String deleteBook(@RequestBody String json) {
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
    public String updateBook(@RequestBody String json) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try {
            //检测数据
            String errorMsg = bookService.checkBookJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            Book book = entityService.json2Entity(bookService.handleBookJson(param), Book.class);

            //修改编辑时间
            book.setEditedTime(DateUtil.NOW_TIMESTAMP);

            res.message = bookService.updateBook(book.getId(), book);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //endregion

    //region ------进阶信息增删改查------

    //根据搜索条件获取图书--列表界面
    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/get-books", method = RequestMethod.POST)
     @ResponseBody
     public String getBooksByFilterList(@RequestBody String json, HttpServletRequest request) {
        JSONObject param = JSON.parseObject(json);
        QueryParams queryParam = JSON.to(QueryParams.class, param.getJSONObject("queryParams"));
        String pageLabel = param.getString("pageLabel");

        List<BookVOAlpha> books = new ArrayList<>();

        SearchResult searchResult = bookService.getBooksByFilter(queryParam,
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

    //更新图书作者信息
    @RequestMapping(path = "/update-authors", method = RequestMethod.POST)
    @ResponseBody
    public String updateBookAuthors(@RequestBody String json) {
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

    @RequestMapping(value = "/get-related-books", method = RequestMethod.POST)
    @ResponseBody
    public String getRelatedBooks(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {

            JSONObject param = JSON.parseObject(json);
            int id = param.getInteger("id");
            res.data = bookService.getRelatedBooks(id);

        }catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //endregion

}
