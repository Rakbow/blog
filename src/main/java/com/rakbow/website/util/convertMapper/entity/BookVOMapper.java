package com.rakbow.website.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.entity.book.BookType;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.common.Language;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.vo.book.BookVO;
import com.rakbow.website.data.vo.book.BookVOAlpha;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.data.vo.book.BookVOGamma;
import com.rakbow.website.entity.Book;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.SpringUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.entity.BookUtil;
import com.rakbow.website.util.entity.FranchiseUtil;
import com.rakbow.website.util.entity.ProductUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 16:13
 * @Description: book VO转换接口
 */
@Mapper(componentModel = "spring", uses = EntityConverter.class)
public interface BookVOMapper {

    BookVOMapper INSTANCES = Mappers.getMapper(BookVOMapper.class);
    EntityType entityType = EntityType.BOOK;
    int entityTypeId = EntityType.BOOK.getId();

    //region single convert interface

    /**
     * Book转VO对象，用于详情页面，转换量最大的
     *
     * @param book 图书
     * @return BookVO
     * @author rakbow
     */
    @Mapping(target = "publishDate", source = "publishDate", qualifiedByName = "getPublishDate")
    @Mapping(target = "currencyUnit", source = "region", qualifiedByName = "getCurrencyUnit")
    @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    @Mapping(target = "bookType", source = "bookType", qualifiedByName = "getBookType")
    @Mapping(target = "region", source = "region", qualifiedByName = "getRegion")
    @Mapping(target = "publishLanguage", source = "publishLanguage", qualifiedByName = "getPublishLanguage")
    @Mapping(target = "authors", source = "authors", qualifiedByName = "getJSONArray")
    @Mapping(target = "spec", source = "spec", qualifiedByName = "getJSONArray")
    @Named("toVO")
    BookVO toVO(Book book);

    // /**
    //  * Book转VO对象，用于list和index页面，转换量较少
    //  *
    //  * @param book 图书
    //  * @return BookVOAlpha
    //  * @author rakbow
    //  */
    // @Mapping(target = "publishDate", source = "publishDate", qualifiedByName = "getPublishDate")
    // @Mapping(target = "currencyUnit", source = "region", qualifiedByName = "getCurrencyUnit")
    // @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    // @Mapping(target = "bookType", source = "bookType", qualifiedByName = "getBookType")
    // @Mapping(target = "region", source = "region", qualifiedByName = "getRegion")
    // @Mapping(target = "publishLanguage", source = "publishLanguage", qualifiedByName = "getPublishLanguage")
    // @Mapping(target = "authors", source = "authors", qualifiedByName = "getJSONArray")
    // @Mapping(target = "spec", source = "spec", qualifiedByName = "getJSONArray")
    // @Named("toVOAlpha")
    // BookVOAlpha toVOAlpha(Book book);

    //endregion


    //region multi convert interface

    //endregion


    //region get property method

    @Named("getPublishDate")
    default String getPublishDate(Date date) {
        return DateUtil.dateToString(date);
    }

    @Named("getCurrencyUnit")
    default String getCurrencyUnit(String region) {
        return Region.getCurrencyByCode(region);
    }

    @Named("getBool")
    default boolean getBool(int bool) {
        return bool == 1;
    }

    @Named("getBookType")
    default JSONObject getBookType(int bookType) {
        return BookType.getAttribute(bookType);
    }

    @Named("getRegion")
    default JSONObject getRegion(String region) {
        return Region.getRegion(region);
    }

    @Named("getPublishLanguage")
    default JSONObject getPublishLanguage(String publishLanguage) {
        return Language.getLanguage(publishLanguage);
    }

    @Named("getJSONArray")
    default JSONArray getJSONArray(String json) {
        return JSON.parseArray(json);
    }

    //endregion

    /**
     * Book转VO对象，用于list和index页面，转换量较少
     *
     * @param book 图书
     * @return BookVOAlpha
     * @author rakbow
     */
    default BookVOAlpha book2VOAlpha(Book book) {
        if (book == null) {
            return null;
        }

        BookVOAlpha bookVOAlpha = new BookVOAlpha();

        bookVOAlpha.setId(book.getId());
        bookVOAlpha.setTitle(book.getTitle());
        bookVOAlpha.setTitleZh(book.getTitleZh());
        bookVOAlpha.setTitleEn(book.getTitleEn());
        bookVOAlpha.setIsbn10(book.getIsbn10());
        bookVOAlpha.setIsbn13(book.getIsbn13());
        bookVOAlpha.setAuthors(BookUtil.getAuthors(book));
        bookVOAlpha.setPublishDate(DateUtil.dateToString(book.getPublishDate()));
        bookVOAlpha.setPrice(book.getPrice());
        bookVOAlpha.setCurrencyUnit(Region.getCurrencyByCode(book.getRegion()));
        bookVOAlpha.setPublisher(book.getPublisher());
        bookVOAlpha.setSummary(book.getSummary());
        bookVOAlpha.setHasBonus(book.getHasBonus() == 1);
        bookVOAlpha.setRemark(book.getRemark());

        bookVOAlpha.setBookType(BookType.getAttribute(book.getBookType()));

        bookVOAlpha.setRegion(Region.getRegion(book.getRegion()));

        bookVOAlpha.setPublishLanguage(Language.getLanguage(book.getPublishLanguage()));

        //关联信息
        bookVOAlpha.setProducts(ProductUtil.getProductList(book.getProducts()));
        bookVOAlpha.setFranchises(FranchiseUtil.getFranchiseList(book.getFranchises()));

        //将图片分割处理
        bookVOAlpha.setCover(CommonImageUtil.generateBookCover(book.getImages(), EntityType.BOOK));

        //审计字段
        bookVOAlpha.setAddedTime(DateUtil.timestampToString(book.getAddedTime()));
        bookVOAlpha.setEditedTime(DateUtil.timestampToString(book.getEditedTime()));
        bookVOAlpha.setStatus(book.getStatus() == 1);

        return bookVOAlpha;
    }

    /**
     * 列表，Book转VO对象，用于list和index页面，转换量较少
     *
     * @param books 图书列表
     * @return List<BookVOAlpha>
     * @author rakbow
     */
    default List<BookVOAlpha> book2VOAlpha(List<Book> books) {
        List<BookVOAlpha> bookVOAlphas = new ArrayList<>();

        if (!books.isEmpty()) {
            books.forEach(book -> {
                bookVOAlphas.add(book2VOAlpha(book));
            });
        }

        return bookVOAlphas;
    }

    /**
     * Book转VO对象，转换量最少
     *
     * @param book 图书
     * @return BookVOBeta
     * @author rakbow
     */
    default BookVOBeta book2VOBeta(Book book) {
        if (book == null) {
            return null;
        }

        BookVOBeta bookVOBeta = new BookVOBeta();

        bookVOBeta.setId(book.getId());
        bookVOBeta.setTitle(book.getTitle());
        bookVOBeta.setTitleZh(book.getTitleZh());
        bookVOBeta.setIsbn13(book.getIsbn13());
        bookVOBeta.setPublishDate(DateUtil.dateToString(book.getPublishDate()));

        bookVOBeta.setBookType(BookType.getAttribute(book.getBookType()));

        bookVOBeta.setRegion(Region.getRegion(book.getRegion()));

        bookVOBeta.setPublishLanguage(Language.getLanguage(book.getPublishLanguage()));

        //将图片分割处理
        bookVOBeta.setCover(CommonImageUtil.generateThumbCover(book.getImages(), EntityType.BOOK, 50));

        //审计字段
        bookVOBeta.setAddedTime(DateUtil.timestampToString(book.getAddedTime()));
        bookVOBeta.setEditedTime(DateUtil.timestampToString(book.getEditedTime()));

        return bookVOBeta;

    }

    /**
     * 列表，Book转VO对象，转换量最少
     *
     * @param books 图书列表
     * @return List<BookVOBeta>
     * @author rakbow
     */
    default List<BookVOBeta> book2VOBeta(List<Book> books) {
        List<BookVOBeta> bookVOBetas = new ArrayList<>();

        if (!books.isEmpty()) {
            books.forEach(book -> {
                bookVOBetas.add(book2VOBeta(book));
            });
        }

        return bookVOBetas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param book 图书
     * @return BookVOGamma
     * @author rakbow
     */
    default BookVOGamma book2VOGamma(Book book) {
        if (book == null) {
            return null;
        }
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        BookVOGamma bookVOGamma = new BookVOGamma();
        bookVOGamma.setId(book.getId());
        bookVOGamma.setTitle(book.getTitle());
        bookVOGamma.setTitleZh(book.getTitleZh());
        bookVOGamma.setTitleEn(book.getTitleEn());
        bookVOGamma.setIsbn10(book.getIsbn10());
        bookVOGamma.setIsbn13(book.getIsbn13());
        bookVOGamma.setAuthors(BookUtil.getAuthors(book));
        bookVOGamma.setPublishDate(DateUtil.dateToString(book.getPublishDate()));
        bookVOGamma.setPublisher(book.getPublisher());
        bookVOGamma.setSummary(book.getSummary());
        bookVOGamma.setHasBonus(book.getHasBonus() == 1);

        bookVOGamma.setBookType(BookType.getAttribute(book.getBookType()));

        bookVOGamma.setRegion(Region.getRegion(book.getRegion()));

        bookVOGamma.setPublishLanguage(Language.getLanguage(book.getPublishLanguage()));

        //关联信息
        bookVOGamma.setProducts(ProductUtil.getProductList(book.getProducts()));
        bookVOGamma.setFranchises(FranchiseUtil.getFranchiseList(book.getFranchises()));

        bookVOGamma.setCover(QiniuImageUtil.getThumb70Url(book.getImages()));

        bookVOGamma.setVisitCount(visitUtil.getVisit(EntityType.BOOK.getId(), book.getId()));
        bookVOGamma.setLikeCount(likeUtil.getLike(EntityType.BOOK.getId(), book.getId()));

        return bookVOGamma;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param books 列表
     * @return List<BookVOGamma>
     * @author rakbow
     */
    default List<BookVOGamma> book2VOGamma(List<Book> books) {
        List<BookVOGamma> bookVOGammas = new ArrayList<>();

        if (!books.isEmpty()) {
            books.forEach(book -> {
                bookVOGammas.add(book2VOGamma(book));
            });
        }

        return bookVOGammas;
    }

}
