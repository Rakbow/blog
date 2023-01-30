package com.rakbow.website.util.convertMapper;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.book.BookType;
import com.rakbow.website.data.emun.common.Language;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.vo.book.BookVO;
import com.rakbow.website.data.vo.book.BookVOAlpha;
import com.rakbow.website.data.vo.book.BookVOBeta;
import com.rakbow.website.entity.Book;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.entity.FranchiseUtils;
import com.rakbow.website.util.image.CommonImageUtils;
import com.rakbow.website.util.entity.ProductUtils;
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
        bookVO.setPublishDate(CommonUtils.dateToString(book.getPublishDate()));
        bookVO.setPrice(book.getPrice());
        bookVO.setCurrencyUnit(Region.regionCode2Currency(book.getRegion()));
        bookVO.setPublisher(book.getPublisher());
        bookVO.setSummary(book.getSummary());
        bookVO.setHasBonus(book.getHasBonus() == 1);
        bookVO.setRemark(book.getRemark());

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));
        bookVO.setBookType(bookType);

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));
        bookVO.setRegion(region);

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));
        bookVO.setPublishLanguage(publishLanguage);

        //关联信息
        bookVO.setProducts(ProductUtils.getProductList(book.getProducts()));
        bookVO.setFranchises(FranchiseUtils.getFranchiseList(book.getFranchises()));

        //大文本信息
        bookVO.setAuthors(JSON.parseArray(book.getAuthors()));
        bookVO.setSpec(JSON.parseArray(book.getSpec()));
        bookVO.setBonus(book.getBonus());
        bookVO.setDescription(book.getDescription());

        //将图片分割处理
        segmentImagesResult segmentImages = CommonImageUtils.segmentImages(book.getImages(), 180, false);
        bookVO.setImages(segmentImages.images);
        bookVO.setCover(segmentImages.cover);
        bookVO.setDisplayImages(segmentImages.displayImages);
        bookVO.setOtherImages(segmentImages.otherImages);

        //审计字段
        bookVO.setAddedTime(CommonUtils.timestampToString(book.getAddedTime()));
        bookVO.setEditedTime(CommonUtils.timestampToString(book.getEditedTime()));
        bookVO.set_s(book.get_s());

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
        bookVOAlpha.setPublishDate(CommonUtils.dateToString(book.getPublishDate()));
        bookVOAlpha.setPrice(book.getPrice());
        bookVOAlpha.setCurrencyUnit(Region.regionCode2Currency(book.getRegion()));
        bookVOAlpha.setPublisher(book.getPublisher());
        bookVOAlpha.setSummary(book.getSummary());
        bookVOAlpha.setHasBonus(book.getHasBonus() == 1);
        bookVOAlpha.setRemark(book.getRemark());

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));
        bookVOAlpha.setBookType(bookType);

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));
        bookVOAlpha.setRegion(region);

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));
        bookVOAlpha.setPublishLanguage(publishLanguage);

        //关联信息
        bookVOAlpha.setProducts(ProductUtils.getProductList(book.getProducts()));
        bookVOAlpha.setFranchises(FranchiseUtils.getFranchiseList(book.getFranchises()));

        //将图片分割处理
        bookVOAlpha.setCover(CommonImageUtils.generateCover(book.getImages()));

        //审计字段
        bookVOAlpha.setAddedTime(CommonUtils.timestampToString(book.getAddedTime()));
        bookVOAlpha.setEditedTime(CommonUtils.timestampToString(book.getEditedTime()));
        bookVOAlpha.set_s(book.get_s());

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
        bookVOBeta.setPublishDate(CommonUtils.dateToString(book.getPublishDate()));

        JSONObject bookType = new JSONObject();
        bookType.put("id", book.getBookType());
        bookType.put("nameZh", BookType.index2NameZh(book.getBookType()));
        bookVOBeta.setBookType(bookType);

        JSONObject region = new JSONObject();
        region.put("code", book.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(book.getRegion()));
        bookVOBeta.setRegion(region);

        JSONObject publishLanguage = new JSONObject();
        publishLanguage.put("code", book.getPublishLanguage());
        publishLanguage.put("nameZh", Language.languageCode2NameZh(book.getPublishLanguage()));
        bookVOBeta.setPublishLanguage(publishLanguage);

        //将图片分割处理
        bookVOBeta.setCover(CommonImageUtils.generateThumbCover(book.getImages(), 50));

        //审计字段
        bookVOBeta.setAddedTime(CommonUtils.timestampToString(book.getAddedTime()));
        bookVOBeta.setEditedTime(CommonUtils.timestampToString(book.getEditedTime()));

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

}
