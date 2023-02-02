package com.rakbow.website.util.convertMapper;

import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.vo.franchise.FranchiseVO;
import com.rakbow.website.data.vo.franchise.FranchiseVOAlpha;
import com.rakbow.website.entity.Franchise;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.file.CommonImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-12 10:45
 * @Description: Franchise VO转换接口
 */
@Mapper(componentModel = "spring")
public interface FranchiseVOMapper {

    FranchiseVOMapper INSTANCES = Mappers.getMapper(FranchiseVOMapper.class);

    /**
     * Franchise转VO对象，用于详情页面，转换量最大的
     *
     * @param franchise 元系列
     * @return FranchiseVO
     * @author rakbow
     */
    default FranchiseVO franchise2VO(Franchise franchise) {

        FranchiseVO franchiseVO = new FranchiseVO();

        franchiseVO.setId(franchise.getId());
        franchiseVO.setName(franchise.getName());
        franchiseVO.setNameZh(franchise.getNameZh());
        franchiseVO.setNameEn(franchise.getNameEn());
        franchiseVO.setOriginDate(CommonUtils.dateToString(franchise.getOriginDate()));
        franchiseVO.setRemark(franchise.getRemark());
        franchiseVO.setDescription(franchise.getDescription());

        segmentImagesResult segmentImages = CommonImageUtils.segmentImages(franchise.getImages(), 200, false);
        franchiseVO.setImages(segmentImages.images);
        franchiseVO.setCover(segmentImages.cover);
        franchiseVO.setDisplayImages(segmentImages.displayImages);
        franchiseVO.setOtherImages(segmentImages.otherImages);

        franchiseVO.setAddedTime(CommonUtils.timestampToString(franchise.getAddedTime()));
        franchiseVO.setEditedTime(CommonUtils.timestampToString(franchise.getEditedTime()));
        franchiseVO.set_s(franchise.get_s());

        return franchiseVO;
    }

    /**
     * Franchise转VO，供list界面使用，信息量较少
     *
     * @param franchise 元系列
     * @return FranchiseVOAlpha
     * @author rakbow
     */
    default FranchiseVOAlpha franchise2VOAlpha(Franchise franchise) {

        FranchiseVOAlpha franchiseVOAlpha = new FranchiseVOAlpha();

        franchiseVOAlpha.setId(franchise.getId());
        franchiseVOAlpha.setName(franchise.getName());
        franchiseVOAlpha.setNameZh(franchise.getNameZh());
        franchiseVOAlpha.setNameEn(franchise.getNameEn());
        franchiseVOAlpha.setOriginDate(CommonUtils.dateToString(franchise.getOriginDate()));
        franchiseVOAlpha.setRemark(franchise.getRemark());

        franchiseVOAlpha.setCover(CommonImageUtils.generateCover(franchise.getImages()));

        franchiseVOAlpha.setAddedTime(CommonUtils.timestampToString(franchise.getAddedTime()));
        franchiseVOAlpha.setEditedTime(CommonUtils.timestampToString(franchise.getEditedTime()));
        franchiseVOAlpha.set_s(franchise.get_s());

        return franchiseVOAlpha;
    }

    /**
     * 列表转换, Franchise转VO对象，供list界面使用，信息量较少
     *
     * @param franchises 元系列列表
     * @return List<FranchiseVOAlpha>
     * @author rakbow
     */
    default List<FranchiseVOAlpha> franchise2VOAlpha(List<Franchise> franchises) {
        List<FranchiseVOAlpha> franchiseVOAlphas = new ArrayList<>();

        if (!franchises.isEmpty()) {
            franchises.forEach(franchise -> franchiseVOAlphas.add(franchise2VOAlpha(franchise)));
        }

        return franchiseVOAlphas;
    }


//    /**
//     * Franchise转VO对象，信息量最少
//     *
//     * @param franchise 元系列
//     * @return FranchiseVOBeta
//     * @author rakbow
//     */
//
//    /**
//     * 列表转换, Franchise转VO对象，信息量最少
//     *
//     * @param franchises 元系列列表
//     * @return List<FranchiseVOBeta>
//     * @author rakbow
//     */

}
