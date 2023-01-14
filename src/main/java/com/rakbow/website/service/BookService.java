package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.BookMapper;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.entity.Book;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.util.convertMapper.BookVOMapper;
import com.rakbow.website.util.image.QiniuImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-28 23:45
 * @Description: book业务层
 */
@Service
public class BookService {

    //region ------注入依赖------

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    @Autowired
    private VisitService visitService;

    private final BookVOMapper bookVOMapper = BookVOMapper.INSTANCES;

    //endregion

    //region ------更删改查------

    /**
     * 新增图书
     *
     * @param book 新增的图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addBook(Book book) {
        bookMapper.addBook(book);
    }

    /**
     * 根据Id获取图书
     *
     * @param id 图书id
     * @return book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Book getBook(int id) {
        return bookMapper.getBook(id);
    }

    /**
     * 根据Id删除图书
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteBook(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllBookImages(id);

        bookMapper.deleteBook(id);
    }

    /**
     * 更新图书基础信息
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBook(int id, Book book) {
        bookMapper.updateBook(id, book);
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Book，以便保存到数据库
     *
     * @param bookJson bookJson
     * @return book
     * @author rakbow
     */
    public Book json2Book(JSONObject bookJson) {
        return JSON.to(Book.class, bookJson);
    }

    /**
     * 检测数据合法性
     *
     * @param bookJson bookJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkBookJson(JSONObject bookJson) {
        if (StringUtils.isBlank(bookJson.getString("title"))) {
            return ApiInfo.BOOK_TITLE_EMPTY;
        }

        if (!StringUtils.isBlank(bookJson.getString("isbn10"))) {
            if (bookJson.getString("isbn10").replaceAll("-", "").length() != 10) {
                return ApiInfo.BOOK_ISBN10_LENGTH_EXCEPTION;
            }
        } else {
            return ApiInfo.BOOK_ISBN10_LENGTH_EXCEPTION;
        }
        if (!StringUtils.isBlank(bookJson.getString("isbn13"))) {
            if (bookJson.getString("isbn13").replaceAll("-", "").length() != 13) {
                return ApiInfo.BOOK_ISBN13_LENGTH_EXCEPTION;
            }
        } else {
            return ApiInfo.BOOK_ISBN13_LENGTH_EXCEPTION;
        }
        if (StringUtils.isBlank(bookJson.getString("publishDate"))) {
            return ApiInfo.BOOK_PUBLISH_DATE_EMPTY;
        }
        if (StringUtils.isBlank(bookJson.getString("bookType"))) {
            return ApiInfo.BOOK_TYPE_EMPTY;
        }
        if (StringUtils.isBlank(bookJson.getString("franchises"))
                || StringUtils.equals(bookJson.getString("franchises"), "[]")) {
            return ApiInfo.BOOK_FRANCHISES_EMPTY;
        }
        if (StringUtils.isBlank(bookJson.getString("products"))
                || StringUtils.equals(bookJson.getString("products"), "[]")) {
            return ApiInfo.BOOK_PRODUCTS_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送图书数据
     *
     * @param bookJson bookJson
     * @return 处理后的book json格式数据
     * @author rakbow
     */
    public JSONObject handleBookJson(JSONObject bookJson) {

        String[] products = CommonUtils.str2SortedArray(bookJson.getString("products"));
        String[] franchises = CommonUtils.str2SortedArray(bookJson.getString("franchises"));

        bookJson.put("isbn10", bookJson.getString("isbn10").replaceAll("-", ""));
        bookJson.put("isbn13", bookJson.getString("isbn13").replaceAll("-", ""));
        bookJson.put("publishDate", bookJson.getDate("publishDate"));
        bookJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        bookJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");

        return bookJson;
    }

    //endregion

    //region ------图片操作------

    /**
     * 新增图书图片
     *
     * @param id                 图书id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addBookImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                              JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.BOOK, images, originalImagesJson, imageInfos);

        bookMapper.updateBookImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图书图片
     *
     * @param id     图书id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateBookImages(int id, String images) {
        bookMapper.updateBookImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.BOOK.getNameZh());
    }

    /**
     * 删除图书图片
     *
     * @param id           图书id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteBookImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getBook(id).getImages());

        JSONArray finalImageJson = qiniuImageUtils.commonDeleteImages(id, images, deleteImages);

        bookMapper.updateBookImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.BOOK.getNameZh());
    }

    /**
     * 删除该图书所有图片
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteAllBookImages(int id) {
        Book book = getBook(id);
        JSONArray images = JSON.parseArray(book.getImages());
        qiniuImageUtils.commonDeleteAllImages(EntityType.BOOK, images);
    }

    //endregion

    //region ------更新book数据------

    /**
     * 更新图书作者信息
     *
     * @param id      图书id
     * @param authors 图书的作者信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBookAuthors(int id, String authors) {
        bookMapper.updateBookAuthors(id, authors, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图书规格信息
     *
     * @param id   图书id
     * @param spec 图书的规格信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBookSpec(int id, String spec) {
        bookMapper.updateBookSpec(id, spec, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新图书描述
     *
     * @param id          图书id
     * @param description 图书的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBookDescription(int id, String description) {
        bookMapper.updateBookDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新特典信息
     *
     * @param id    图书id
     * @param bonus 图书的特典信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateBookBonus(int id, String bonus) {
        bookMapper.updateBookBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getBooksByFilter(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String title = filter.getJSONObject("title").getString("value");
        String isbn10 = filter.getJSONObject("isbn10").getString("value");
        String isbn13 = filter.getJSONObject("isbn13").getString("value");
        String region = filter.getJSONObject("region").getString("value");
        String publishLanguage = filter.getJSONObject("publishLanguage").getString("value");
        String publisher = filter.getJSONObject("publisher").getString("value");

        int bookType = 100;
        if (filter.getJSONObject("bookType").getInteger("value") != null) {
            bookType = filter.getJSONObject("bookType").getIntValue("value");
        }

        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        } else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }

        List<Book> books = bookMapper.getBooksByFilter(title, isbn10, isbn13, publisher, region, publishLanguage,
                bookType, franchises, products, hasBonus, sortField, sortOrder, first, row);

        int total = bookMapper.getBooksRowsByFilter(title, isbn10, isbn13, publisher, region, publishLanguage,
                bookType, franchises, products, hasBonus);

        return new SearchResult(total, books);
    }

    /**
     * 根据作品id获取图书
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getBooksByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Book> books = bookMapper.getBooksByFilter(null, null, null, null,
                null, null, 100, null, products, null, "publishDate",
                -1,  0, 0);

        return bookVOMapper.book2VOBeta(books);
    }

    /**
     * 获取相关联Book
     *
     * @param id 图书id
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getRelatedBooks(int id) {

        List<Book> result = new ArrayList<>();

        Book book = getBook(id);

        //该Book包含的作品id
        List<Integer> productIds = JSONObject.parseObject(book.getProducts()).getList("ids", Integer.class);

        //该系列所有Book
        List<Book> allBooks = bookMapper.getBooksByFilter(null, null, null, null,
                null, null, 100, CommonUtils.ids2List(book.getFranchises()),
                        null, null, "publishDate", 1, 0, 0)
                .stream().filter(tmpBook -> tmpBook.getId() != book.getId()).collect(Collectors.toList());

        List<Book> queryResult = allBooks.stream().filter(tmpBook ->
                StringUtils.equals(tmpBook.getProducts(), book.getProducts())
                &&StringUtils.equals(tmpBook.getPublisher(), book.getPublisher())).collect(Collectors.toList());

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Book> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Book> tmpQueryResult = allBooks.stream().filter(tmpBook ->
                        JSONObject.parseObject(tmpBook.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).collect(Collectors.toList());

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Book> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allBooks.stream().filter(tmpBook ->
                                JSONObject.parseObject(tmpBook.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).collect(Collectors.toList())
                );
            }
            result = CommonUtils.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return bookVOMapper.book2VOBeta(CommonUtils.removeDuplicateList(result));
    }

    /**
     * 获取最新收录的图书
     *
     * @param limit 获取条数
     * @return list封装的图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getJustAddedBooks(int limit) {
        return bookVOMapper.book2VOBeta(bookMapper.getBooksOrderByAddedTime(limit));
    }

    /**
     * 获取最近编辑的Book
     *
     * @param limit 获取条数
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getJustEditedBooks(int limit) {
        return bookVOMapper.book2VOBeta(bookMapper.getBooksOrderByEditedTime(limit));
    }

    /**
     * 获取浏览量最高的Book
     *
     * @param limit 获取条数
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<BookVOBeta> getPopularBooks(int limit) {
        List<BookVOBeta> popularBooks = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.BOOK.getId(), limit);

        visits.forEach(visit -> {
            BookVOBeta book = bookVOMapper.book2VOBeta(getBook(visit.getEntityId()));
            book.setVisitNum(visit.getVisitNum());
            popularBooks.add(book);
        });
        return popularBooks;
    }

    //endregion

}
