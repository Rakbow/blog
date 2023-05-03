package com.rakbow.website.util.convertMapper.entity;

import com.alibaba.fastjson2.JSON;
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
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 16:13
 * @Description: book VO转换接口
 */
@Mapper(componentModel = "spring")
public interface BookVOMapper {

    BookVOMapper INSTANCES = Mappers.getMapper(BookVOMapper.class);

    /**
     * Book转VO对象，用于详情页面，转换量最大的
     *
     * @param book 图书
     * @return BookVO
     * @author rakbow
     */
    default BookVO book2VO(Book book) {
        if (book == null) {
            return null;
        }

        BookVO bookVO = new BookVO();

        bookVO.setId(book.getId());
        bookVO.setTitle(book.getTitle());
        bookVO.setTitleZh(book.getTitleZh());
        bookVO.setTitleEn(book.getTitleEn());
        bookVO.setIsbn10(book.getIsbn10());
        bookVO.setIsbn13(book.getIsbn13());
        bookVO.setPublishDate(DateUtil.dateToString(book.getPublishDate()));
        bookVO.setPrice(book.getPrice());
        bookVO.setCurrencyUnit(Region.getCurrencyByCode(book.getRegion()));
        bookVO.setPublisher(book.getPublisher());
        bookVO.setSummary(book.getSummary());
        bookVO.setHasBonus(book.getHasBonus() == 1);
        bookVO.setRemark(book.getRemark());

        bookVO.setBookType(BookType.getAttribute(book.getBookType()));

        bookVO.setRegion(Region.getRegion(book.getRegion()));

        bookVO.setPublishLanguage(Language.getLanguage(book.getPublishLanguage()));

        //大文本信息
        bookVO.setAuthors(JSON.parseArray(book.getAuthors()));
        bookVO.setSpec(JSON.parseArray(book.getSpec()));
        bookVO.setBonus(book.getBonus());

        return bookVO;
    }

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
