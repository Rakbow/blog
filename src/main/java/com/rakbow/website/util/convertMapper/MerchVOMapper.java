package com.rakbow.website.util.convertMapper;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.Region;
import com.rakbow.website.data.emun.merch.MerchCategory;
import com.rakbow.website.data.vo.merch.MerchVO;
import com.rakbow.website.data.vo.merch.MerchVOAlpha;
import com.rakbow.website.data.vo.merch.MerchVOBeta;
import com.rakbow.website.data.vo.merch.MerchVOGamma;
import com.rakbow.website.entity.Merch;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.entity.FranchiseUtils;
import com.rakbow.website.util.file.CommonImageUtils;
import com.rakbow.website.util.entity.ProductUtils;
import com.rakbow.website.util.file.QiniuImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-12 10:45
 * @Description: Merch VO转换接口
 */
@Mapper(componentModel = "spring")
public interface MerchVOMapper {

    MerchVOMapper INSTANCES = Mappers.getMapper(MerchVOMapper.class);

    /**
     * Merch转VO对象，用于详情页面，转换量最大的
     *
     * @param merch 周边商品
     * @return MerchVO
     * @author rakbow
     */
    default MerchVO merch2VO(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVO merchVO = new MerchVO();

        //基础信息
        merchVO.setId(merch.getId());
        merchVO.setName(merch.getName());
        merchVO.setNameZh(merch.getNameZh());
        merchVO.setNameEn(merch.getNameEn());
        merchVO.setBarcode(merch.getBarcode());
        merchVO.setReleaseDate(CommonUtils.dateToString(merch.getReleaseDate()));
        merchVO.setPrice(merch.getPrice());
        merchVO.setCurrencyUnit(merch.getCurrencyUnit());
        merchVO.setNotForSale(merch.getNotForSale() == 1);
        merchVO.setRemark(merch.getRemark());

        //复杂字段
        merchVO.setCategory(MerchCategory.getMerchCategoryJson(merch.getCategory()));
        merchVO.setSpec(JSON.parseArray(merch.getSpec()));

        JSONObject region = new JSONObject();
        region.put("code", merch.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(merch.getRegion()));
        merchVO.setRegion(region);

        return merchVO;
    }

    /**
     * Merch转VO对象，用于list和index页面，转换量较少
     *
     * @param merch 周边商品
     * @return MerchVOAlpha
     * @author rakbow
     */
    default MerchVOAlpha merch2VOAlpha(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVOAlpha merchVOAlpha = new MerchVOAlpha();

        //基础信息
        merchVOAlpha.setId(merch.getId());
        merchVOAlpha.setName(merch.getName());
        merchVOAlpha.setNameZh(merch.getNameZh());
        merchVOAlpha.setNameEn(merch.getNameEn());
        merchVOAlpha.setBarcode(merch.getBarcode());
        merchVOAlpha.setReleaseDate(CommonUtils.dateToString(merch.getReleaseDate()));
        merchVOAlpha.setPrice(merch.getPrice());
        merchVOAlpha.setCurrencyUnit(merch.getCurrencyUnit());
        merchVOAlpha.setNotForSale(merch.getNotForSale() == 1);
        merchVOAlpha.setRemark(merch.getRemark());

        //关联信息
        merchVOAlpha.setFranchises(FranchiseUtils.getFranchiseList(merch.getFranchises()));
        merchVOAlpha.setProducts(ProductUtils.getProductList(merch.getProducts()));

        //复杂字段
        merchVOAlpha.setCategory(MerchCategory.getMerchCategoryJson(merch.getCategory()));

        JSONObject region = new JSONObject();
        region.put("code", merch.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(merch.getRegion()));
        merchVOAlpha.setRegion(region);

        //图片
        merchVOAlpha.setCover(CommonImageUtils.generateCover(merch.getImages()));

        //审计字段
        merchVOAlpha.setAddedTime(CommonUtils.timestampToString(merch.getAddedTime()));
        merchVOAlpha.setEditedTime(CommonUtils.timestampToString(merch.getEditedTime()));
        merchVOAlpha.setStatus(merch.getStatus() == 1);

        return merchVOAlpha;
    }

    /**
     * 列表，Merch转VO对象，用于list和index页面，转换量较少
     *
     * @param merchs 周边商品列表
     * @return List<MerchVOAlpha>
     * @author rakbow
     */
    default List<MerchVOAlpha> merch2VOAlpha(List<Merch> merchs) {
        List<MerchVOAlpha> merchVOAlphas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> merchVOAlphas.add(merch2VOAlpha(merch)));
        }

        return merchVOAlphas;
    }

    /**
     * Merch转VO对象，转换量最少
     *
     * @param merch 周边商品
     * @return MerchVOBeta
     * @author rakbow
     */
    default MerchVOBeta merch2VOBeta(Merch merch) {
        if (merch == null) {
            return null;
        }

        MerchVOBeta merchVOBeta = new MerchVOBeta();

        //基础信息
        merchVOBeta.setId(merch.getId());
        merchVOBeta.setName(merch.getName());
        merchVOBeta.setNameZh(merch.getNameZh());
        merchVOBeta.setNameEn(merch.getNameEn());
        merchVOBeta.setBarcode(merch.getBarcode());
        merchVOBeta.setReleaseDate(CommonUtils.dateToString(merch.getReleaseDate()));
        merchVOBeta.setNotForSale(merch.getNotForSale() == 1);

        //关联信息
        merchVOBeta.setFranchises(FranchiseUtils.getFranchiseList(merch.getFranchises()));
        merchVOBeta.setProducts(ProductUtils.getProductList(merch.getProducts()));

        //复杂字段
        merchVOBeta.setCategory(MerchCategory.getMerchCategoryJson(merch.getCategory()));

        //图片
        merchVOBeta.setCover(CommonImageUtils.generateThumbCover(merch.getImages(), 50));

        //审计字段
        merchVOBeta.setAddedTime(CommonUtils.timestampToString(merch.getAddedTime()));
        merchVOBeta.setEditedTime(CommonUtils.timestampToString(merch.getEditedTime()));

        return merchVOBeta;
    }

    /**
     * 列表，Merch转VO对象，转换量最少
     *
     * @param merchs 周边商品列表
     * @return List<MerchVOBeta>
     * @author rakbow
     */
    default List<MerchVOBeta> merch2VOBeta(List<Merch> merchs) {
        List<MerchVOBeta> merchVOBetas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> merchVOBetas.add(merch2VOBeta(merch)));
        }

        return merchVOBetas;
    }

    /**
     * 转VO对象，用于存储到搜索引擎
     *
     * @param merch 商品
     * @return MerchVOGamma
     * @author rakbow
     */
    default MerchVOGamma merch2VOGamma(Merch merch) {
        if (merch == null) {
            return null;
        }
        MerchVOGamma merchVOGamma = new MerchVOGamma();

        //基础信息
        merchVOGamma.setId(merch.getId());
        merchVOGamma.setName(merch.getName());
        merchVOGamma.setNameZh(merch.getNameZh());
        merchVOGamma.setNameEn(merch.getNameEn());
        merchVOGamma.setReleaseDate(CommonUtils.dateToString(merch.getReleaseDate()));
        merchVOGamma.setNotForSale(merch.getNotForSale() == 1);

        //关联信息
        merchVOGamma.setFranchises(FranchiseUtils.getFranchiseList(merch.getFranchises()));
        merchVOGamma.setProducts(ProductUtils.getProductList(merch.getProducts()));

        //复杂字段
        merchVOGamma.setCategory(MerchCategory.getMerchCategoryJson(merch.getCategory()));

        JSONObject region = new JSONObject();
        region.put("code", merch.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(merch.getRegion()));
        merchVOGamma.setRegion(region);

        merchVOGamma.setCover(QiniuImageUtils.getThumb70Url(merch.getImages()));

        return merchVOGamma;
    }

    /**
     * 列表转换, 转VO对象，用于存储到搜索引擎
     *
     * @param merchs 列表
     * @return List<MerchVOGamma>
     * @author rakbow
     */
    default List<MerchVOGamma> merch2VOGamma(List<Merch> merchs) {
        List<MerchVOGamma> merchVOGammas = new ArrayList<>();

        if (!merchs.isEmpty()) {
            merchs.forEach(merch -> {
                merchVOGammas.add(merch2VOGamma(merch));
            });
        }

        return merchVOGammas;
    }

}
