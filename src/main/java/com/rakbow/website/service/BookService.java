package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.BookMapper;
import com.rakbow.website.data.book.BookType;
import com.rakbow.website.data.common.*;
import com.rakbow.website.data.product.ProductClass;
import com.rakbow.website.entity.Book;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.ProductUtils;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonConstant;
import com.rakbow.website.util.common.CommonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-28 23:45
 * @Description:
 */
@Service
public class BookService {

    //region ------引入实例------

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private CommonImageUtils commonImageUtils;
    @Autowired
    private ProductService productService;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private ProductUtils productUtils;

    //endregion

    //region ------更删改查------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 新增图书
     *
     * @param book 新增的图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Book getBookById(int id) {
        return bookMapper.getBookById(id);
    }

    /**
     * 根据Id删除图书
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void deleteBookById(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllBookImages(id);

        bookMapper.deleteBookById(id);
    }

    /**
     * 更新图书基础信息
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateBook(int id, Book book) {
        bookMapper.updateBook(id, book);
    }

    //endregion

    //region ------数据处理------

    /**
     * Book转Json对象，以便前端使用，转换量最大的
     *
     * @param book
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject book2Json(Book book) {

        JSONObject bookJson = (JSONObject) JSON.toJSON(book);

        //是否包含特典
        boolean hasBonus = (book.getHasBonus() == 1);

        //将图片分割处理
        segmentImagesResult segmentImages = commonImageUtils.segmentImages(book.getImages());

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));

        JSONArray authors = JSON.parseArray(book.getAuthors());
        JSONArray spec = JSON.parseArray(book.getSpec());

        //所属作品
        List<JSONObject> products = productUtils.getProductList(book.getProducts());

        JSONObject series = new JSONObject();
        series.put("id", book.getSeries());
        series.put("name", seriesService.selectSeriesById(book.getSeries()).getNameZh());

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));

        bookJson.put("bookType", bookType);
        bookJson.put("series", series);
        bookJson.put("products", products);
        bookJson.put("region", region);
        bookJson.put("currencyUnit", Region.regionCode2Currency(book.getRegion()));
        bookJson.put("publishLanguage", publishLanguage);
        bookJson.put("authors", authors);
        bookJson.put("spec", spec);

        bookJson.put("publishDate", CommonUtils.dateToString(book.getPublishDate()));
        bookJson.put("hasBonus", hasBonus);
        bookJson.put("images", segmentImages.images);
        bookJson.put("cover", segmentImages.cover);
        bookJson.put("displayImages", segmentImages.displayImages);
        bookJson.put("otherImages", segmentImages.otherImages);
        bookJson.put("addedTime", CommonUtils.timestampToString(book.getAddedTime()));
        bookJson.put("editedTime", CommonUtils.timestampToString(book.getEditedTime()));
        return bookJson;
    }

    /**
     * 列表转换, Book转Json对象，以便前端使用，转换量最大的
     *
     * @param books
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> book2Json(List<Book> books) {
        List<JSONObject> bookJsons = new ArrayList<>();

        books.forEach(book -> {
            bookJsons.add(book2Json(book));
        });
        return bookJsons;
    }

    /**
     * Book转Json对象，以便前端list界面展示使用
     *
     * @param book
     * @return List<JSONObject>
     * @author rakbow
     */
    public JSONObject book2JsonList(Book book) {

        JSONObject bookJson = (JSONObject) JSON.toJSON(book);

        //是否包含特典
        boolean hasBonus = (book.getHasBonus() == 1);

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));

        //发售时间转为string
        bookJson.put("publishDate", CommonUtils.dateToString(book.getPublishDate()));

        //所属作品
        List<JSONObject> products = productUtils.getProductList(book.getProducts());

        //所属系列
        JSONObject series = new JSONObject();
        series.put("id", book.getSeries());
        series.put("name", seriesService.selectSeriesById(book.getSeries()).getNameZh());

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));

        //封面
        JSONObject cover = commonImageUtils.getCover(book.getImages(), 250);

        bookJson.put("hasBonus", hasBonus);
        bookJson.put("cover", cover);
        bookJson.put("series", series);
        bookJson.put("bookType", bookType);
        bookJson.put("products", products);
        bookJson.put("addedTime", CommonUtils.timestampToString(book.getAddedTime()));
        bookJson.put("editedTime", CommonUtils.timestampToString(book.getEditedTime()));

        bookJson.put("region", region);
        bookJson.put("currencyUnit", Region.regionCode2Currency(book.getRegion()));
        bookJson.put("publishLanguage", publishLanguage);

        bookJson.remove("bonus");
        bookJson.remove("authors");
        bookJson.remove("description");
        bookJson.remove("images");
        bookJson.remove("spec");

        return bookJson;
    }

    /**
     * 列表转换, book转Json对象，以便前端list界面展示使用
     *
     * @param books
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> book2JsonList(List<Book> books) {
        List<JSONObject> bookJsons = new ArrayList<>();

        books.forEach(book -> {
            bookJsons.add(book2JsonList(book));
        });
        return bookJsons;
    }

    /**
     * json对象转Book，以便保存到数据库
     *
     * @param bookJson
     * @return book
     * @author rakbow
     */
    public Book json2Book(JSONObject bookJson) {
        return bookJson.toJavaObject(Book.class);
    }

    /**
     * Book转极简Json
     *
     * @param book
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject book2JsonSimple(Book book) {

        //封面
        JSONObject cover = commonImageUtils.getCover(book.getImages(), 50);

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));

        JSONObject bookJson = new JSONObject();
        bookJson.put("id", book.getId());
        bookJson.put("isbn13", book.getIsbn13());
        bookJson.put("publishDate", CommonUtils.dateToString(book.getPublishDate()));
        bookJson.put("title", book.getTitle());
        bookJson.put("titleZh", book.getTitleZh());
        bookJson.put("region", region);
        bookJson.put("publishLanguage", publishLanguage);
        bookJson.put("bookType", bookType);
        bookJson.put("cover", cover);
        bookJson.put("addedTime", CommonUtils.timestampToString(book.getAddedTime()));
        bookJson.put("editedTime", CommonUtils.timestampToString(book.getEditedTime()));
        return bookJson;
    }

    /**
     * 列表转换, Book转极简Json
     *
     * @param books
     * @return JSONObject
     * @author rakbow
     */
    public List<JSONObject> book2JsonSimple(List<Book> books) {
        List<JSONObject> bookJsons = new ArrayList<>();

        books.forEach(book -> bookJsons.add(book2JsonSimple(book)));

        return bookJsons;
    }

    /**
     * Book转Json对象，供首页展示
     *
     * @param book
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject book2JsonIndex(Book book) {

        JSONObject bookJson = (JSONObject) JSON.toJSON(book);

        //封面
        JSONObject cover = commonImageUtils.getIndexCover(book.getImages());

        bookJson.put("publishDate", CommonUtils.dateToString(book.getPublishDate()));

        //所属作品
        List<JSONObject> products = productUtils.getProductList(book.getProducts());

        JSONObject series = new JSONObject();
        series.put("id", book.getSeries());
        series.put("name", seriesService.selectSeriesById(book.getSeries()).getNameZh());

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));

        bookJson.put("cover", cover);
        bookJson.put("products", products);
        bookJson.put("series", series);
        bookJson.put("bookType", bookType);
        bookJson.put("region", region);
        bookJson.put("publishLanguage", publishLanguage);
        bookJson.put("addedTime", CommonUtils.timestampToString(book.getAddedTime()));
        bookJson.put("editedTime", CommonUtils.timestampToString(book.getEditedTime()));
        bookJson.remove("summary");
        bookJson.remove("authors");
        bookJson.remove("spec");
        bookJson.remove("bonus");
        bookJson.remove("description");
        bookJson.remove("images");
        return bookJson;
    }

    /**
     * 列表转换, Book转Json对象，供首页展示
     *
     * @param books
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> book2JsonIndex(List<Book> books) {
        List<JSONObject> bookJsons = new ArrayList<>();

        books.forEach(book -> {
            bookJsons.add(book2JsonIndex(book));
        });
        return bookJsons;
    }

    /**
     * 检测数据合法性
     *
     * @param bookJson
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
        if (StringUtils.isBlank(bookJson.getString("series"))) {
            return ApiInfo.BOOK_SERIES_EMPTY;
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
     * @param bookJson
     * @return 处理后的book json格式数据
     * @author rakbow
     */
    public JSONObject handleBookJson(JSONObject bookJson) {

        String[] products = CommonUtils.str2SortedArray(bookJson.getString("products"));

        bookJson.put("isbn10", bookJson.getString("isbn10").replaceAll("-", ""));
        bookJson.put("isbn13", bookJson.getString("isbn13").replaceAll("-", ""));
        bookJson.put("publishDate", bookJson.getDate("publishDate"));
        bookJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");

        return bookJson;
    }

    //endregion

    //region ------更新book数据------

    /**
     * 新增图书图片
     *
     * @param id                 图书id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addBookImages(int id, MultipartFile[] images, JSONArray originalImagesJson,
                              JSONArray imageInfos) throws IOException {

        JSONArray finalImageJson = commonImageUtils.commonAddImages
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteBookImages(int id, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getBookById(id).getImages());

        JSONArray finalImageJson = commonImageUtils.commonDeleteImages(id, images, deleteImages);

        bookMapper.updateBookImages(id, finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.BOOK.getNameZh());
    }

    /**
     * 删除该图书所有图片
     *
     * @param id 图书id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllBookImages(int id) {
        Book book = getBookById(id);
        JSONArray images = JSON.parseArray(book.getImages());

        return commonImageUtils.commonDeleteAllImages(EntityType.BOOK, images);
    }

    /**
     * 更新图书作者信息
     *
     * @param id      图书id
     * @param authors 图书的作者信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateBookBonus(int id, String bonus) {
        bookMapper.updateBookBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Map<String, Object> getBooksByFilterList(JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");

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

        int series = 0;
        if (filter.getJSONObject("series").getInteger("value") != null) {
            series = filter.getJSONObject("series").getIntValue("value");
        }

        List<Integer> products = new ArrayList<>();
        List<Integer> tmpProducts = filter.getJSONObject("products").getList("value", Integer.class);
        if (tmpProducts != null) {
            products.addAll(tmpProducts);
        }

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        } else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ? Integer.toString(1) : Integer.toString(0);
        }

        int first = queryParams.getIntValue("first");

        int row = queryParams.getIntValue("rows");

        List<Book> books = bookMapper.getBooksByFilterList(title, isbn10, isbn13, publisher, region, publishLanguage,
                bookType, series, products, hasBonus, sortField, sortOrder, first, row);

        int total = bookMapper.getBooksRowsByFilterList(title, isbn10, isbn13, publisher, region, publishLanguage,
                bookType, series, products, hasBonus);

        Map<String, Object> res = new HashMap<>();
        res.put("data", books);
        res.put("total", total);

        return res;
    }

    /**
     * 根据作品id获取关联图书
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getBooksByProductId(int productId) {
        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Book> books = bookMapper.getBooksByFilterList(null, null, null, null,
                null, null, 100, 0, products, null, "publishDate",
                -1,  0, 0);

        return book2JsonSimple(books);
    }

    /**
     * 获取相关联Book
     *
     * @param id 图书id
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedBooks(int id) {

        List<Book> result = new ArrayList<>();

        Book book = getBookById(id);

        List<JSONObject> relatedBooks = new ArrayList<>();

        //该Book包含的作品id
        List<Integer> productIds = JSONObject.parseObject(book.getProducts()).getList("ids", Integer.class);

        //该系列所有Book
        List<Book> allBooks = bookMapper.getBooksByFilterList(null, null, null, null,
                null, null, 100, book.getSeries(), null, null,
                "publishDate", 1, 0, 0)
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
        result = CommonUtils.removeDuplicateList(result);
        result.forEach(i -> relatedBooks.add(book2JsonSimple(i)));
        return relatedBooks;
    }

    /**
     * 获取最新收录的图书
     *
     * @param limit 获取条数
     * @return list封装的图书
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustAddedBooks(int limit) {
        List<JSONObject> justAddedBooks = new ArrayList<>();

        bookMapper.getBooksOrderByAddedTime(limit)
                .forEach(i -> justAddedBooks.add(book2JsonIndex(i)));

        return justAddedBooks;
    }

    /**
     * 获取最近编辑的Book
     *
     * @param limit 获取条数
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustEditedBooks(int limit) {
        List<JSONObject> editedBooks = new ArrayList<>();

        bookMapper.getBooksOrderByEditedTime(limit)
                .forEach(i -> editedBooks.add(book2JsonIndex(i)));

        return editedBooks;
    }

    /**
     * 获取浏览量最高的Book
     *
     * @param limit 获取条数
     * @return list封装的Book
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getPopularBooks(int limit) {
        List<JSONObject> popularBooks = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.BOOK.getId(), limit);

        visits.forEach(visit -> {
            JSONObject book = book2JsonIndex(getBookById(visit.getEntityId()));
            book.put("visitNum", visit.getVisitNum());
            popularBooks.add(book);
        });
        return popularBooks;
    }

    //endregion

}
