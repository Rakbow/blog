package com.rakbow.website.util.convertMapper;

import com.alibaba.fastjson2.JSON;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.vo.disc.DiscVO;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.disc.DiscVOBeta;
import com.rakbow.website.entity.Disc;
import com.rakbow.website.util.entity.AlbumUtils;
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
 * @Create: 2023-01-12 10:45
 * @Description: Disc VO转换接口
 */
@Mapper(componentModel = "spring")
public interface DiscVOMapper {

    DiscVOMapper INSTANCES = Mappers.getMapper(DiscVOMapper.class);

    /**
     * Disc转VO对象，用于详情页面，转换量最大的
     *
     * @param disc Disc
     * @return DiscVO
     * @author rakbow
     */
    default DiscVO disc2VO(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVO discVO = new DiscVO();

        discVO.setId(disc.getId());
        discVO.setCatalogNo(disc.getCatalogNo());
        discVO.setName(disc.getName());
        discVO.setNameZh(disc.getNameZh());
        discVO.setNameEn(disc.getNameEn());
        discVO.setBarcode(disc.getBarcode());
        discVO.setReleaseDate(CommonUtils.dateToString(disc.getReleaseDate()));
        discVO.setPrice(disc.getPrice());
        discVO.setLimited(disc.getLimited() == 1);
        discVO.setHasBonus(disc.getHasBonus() == 1);
        discVO.setRemark(disc.getRemark());

        discVO.setFranchises(FranchiseUtils.getFranchiseList(disc.getFranchises()));
        discVO.setProducts(ProductUtils.getProductList(disc.getProducts()));

        discVO.setMediaFormat(AlbumUtils.getMediaFormat(disc.getMediaFormat()));
        discVO.setSpec(JSON.parseArray(disc.getSpec()));
        discVO.setBonus(disc.getBonus());
        discVO.setDescription(disc.getDescription());

        //将图片分割处理
        segmentImagesResult segmentImages = CommonImageUtils.segmentImages(disc.getImages(), 200);
        discVO.setImages(segmentImages.images);
        discVO.setCover(segmentImages.cover);
        discVO.setDisplayImages(segmentImages.displayImages);
        discVO.setOtherImages(segmentImages.otherImages);

        discVO.setAddedTime(CommonUtils.timestampToString(disc.getAddedTime()));
        discVO.setEditedTime(CommonUtils.timestampToString(disc.getEditedTime()));
        discVO.set_s(disc.get_s());

        return discVO;
    }

    /**
     * Disc转VO对象，用于list和index页面，转换量较少
     *
     * @param disc Disc
     * @return DiscVOAlpha
     * @author rakbow
     */
    default DiscVOAlpha disc2VOAlpha(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVOAlpha discVOAlpha = new DiscVOAlpha();

        discVOAlpha.setId(disc.getId());
        discVOAlpha.setCatalogNo(disc.getCatalogNo());
        discVOAlpha.setName(disc.getName());
        discVOAlpha.setNameZh(disc.getNameZh());
        discVOAlpha.setNameEn(disc.getNameEn());
        discVOAlpha.setBarcode(disc.getBarcode());
        discVOAlpha.setReleaseDate(CommonUtils.dateToString(disc.getReleaseDate()));
        discVOAlpha.setPrice(disc.getPrice());
        discVOAlpha.setLimited(disc.getLimited() == 1);
        discVOAlpha.setHasBonus(disc.getHasBonus() == 1);
        discVOAlpha.setRemark(disc.getRemark());

        discVOAlpha.setFranchises(FranchiseUtils.getFranchiseList(disc.getFranchises()));
        discVOAlpha.setProducts(ProductUtils.getProductList(disc.getProducts()));

        discVOAlpha.setMediaFormat(AlbumUtils.getMediaFormat(disc.getMediaFormat()));

        //将图片分割处理
        discVOAlpha.setCover(CommonImageUtils.generateCover(disc.getImages()));

        discVOAlpha.setAddedTime(CommonUtils.timestampToString(disc.getAddedTime()));
        discVOAlpha.setEditedTime(CommonUtils.timestampToString(disc.getEditedTime()));
        discVOAlpha.set_s(disc.get_s());

        return discVOAlpha;
    }


    /**
     * 列表，Disc转VO对象，用于list和index页面，转换量较少
     *
     * @param discs disc列表
     * @return List<DiscVOAlpha>
     * @author rakbow
     */
    default List<DiscVOAlpha> disc2VOAlpha(List<Disc> discs) {
        List<DiscVOAlpha> discVOAlphas = new ArrayList<>();

        if (!discs.isEmpty()) {
            discs.forEach(disc -> discVOAlphas.add(disc2VOAlpha(disc)));
        }

        return discVOAlphas;
    }

    /**
     * Disc转VO对象，转换量最少
     *
     * @param disc disc
     * @return DiscVOBeta
     * @author rakbow
     */
    default DiscVOBeta disc2VOBeta(Disc disc) {
        if (disc == null) {
            return null;
        }

        DiscVOBeta discVOBeta = new DiscVOBeta();

        discVOBeta.setId(disc.getId());
        discVOBeta.setCatalogNo(disc.getCatalogNo());
        discVOBeta.setName(disc.getName());
        discVOBeta.setNameZh(disc.getNameZh());
        discVOBeta.setNameEn(disc.getNameEn());
        discVOBeta.setReleaseDate(CommonUtils.dateToString(disc.getReleaseDate()));

        discVOBeta.setMediaFormat(AlbumUtils.getMediaFormat(disc.getMediaFormat()));

        discVOBeta.setCover(CommonImageUtils.generateThumbCover(disc.getImages(), 50));

        discVOBeta.setAddedTime(CommonUtils.timestampToString(disc.getAddedTime()));
        discVOBeta.setEditedTime(CommonUtils.timestampToString(disc.getEditedTime()));

        return discVOBeta;
    }

    /**
     * 列表，Disc转VO对象，转换量最少
     *
     * @param discs disc列表
     * @return List<DiscVOBeta>
     * @author rakbow
     */
    default List<DiscVOBeta> disc2VOBeta(List<Disc> discs) {
        List<DiscVOBeta> discVOBetas = new ArrayList<>();

        if (!discs.isEmpty()) {
            discs.forEach(disc -> discVOBetas.add(disc2VOBeta(disc)));
        }

        return discVOBetas;
    }

}
