package com.rakbow.website.data.game;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.common.Region;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.entity.Game;
import com.rakbow.website.util.FranchiseUtils;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.ProductUtils;
import com.rakbow.website.util.common.CommonUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @Mapper 定义这是一个MapStruct对象属性转换接口，在这个类里面规定转换规则
 *          在项目构建时，会自动生成改接口的实现类，这个实现类将实现对象属性值复制
 */
@Mapper
public interface GameVoMapper {


    ProductUtils productUtils = new ProductUtils();
    FranchiseUtils franchiseUtils = new FranchiseUtils();
    CommonImageUtils commonImageUtils = new CommonImageUtils();

    /**
     * 获取该类自动生成的实现类的实例
     * 接口中的属性都是 public static final 的 方法都是public abstract的
     */
    GameVoMapper INSTANCES = Mappers.getMapper(GameVoMapper.class);

    default GameVo game2Vo (Game game) {
        if (game == null) {
            return null;
        }

        GameVo gameVo = new GameVo();

        gameVo.setId(game.getId());
        gameVo.setName(game.getName());
        gameVo.setNameZh(game.getNameZh());
        gameVo.setNameEn(game.getNameEn());
        gameVo.setBarcode(game.getBarcode());
        gameVo.setProducts(productUtils.getProductList(game.getProducts()));
        gameVo.setFranchises(franchiseUtils.getFranchiseList(game.getFranchises()));

        JSONObject region = new JSONObject();
        region.put("code", game.getRegion());
        region.put("nameZh", Region.regionCode2NameZh(game.getRegion()));
        gameVo.setRegion(region);

        JSONObject platform = new JSONObject();
        platform.put("id", game.getPlatform());
        platform.put("nameEn", GamePlatform.index2Name(game.getPlatform()));
        gameVo.setPlatform(platform);

        //是否包含特典
        gameVo.setHasBonus(game.getHasBonus() == 1);

        //将图片分割处理
        segmentImagesResult segmentImages = CommonImageUtils.segmentImages(game.getImages(), 200);
        gameVo.setCover(segmentImages.cover);
        gameVo.setImages(segmentImages.images);
        gameVo.setDisplayImages(segmentImages.displayImages);
        gameVo.setOtherImages(segmentImages.otherImages);

        JSONObject releaseType = new JSONObject();
        releaseType.put("id", game.getReleaseType());
        releaseType.put("nameZh", ReleaseType.index2NameZh(game.getReleaseType()));
        gameVo.setReleaseType(releaseType);

        gameVo.setOrganizations(JSON.parseArray(game.getOrganizations()));
        gameVo.setStaffs(JSON.parseArray(game.getStaffs()));

        gameVo.setReleaseDate(CommonUtils.dateToString(game.getReleaseDate()));
        gameVo.setAddedTime(CommonUtils.timestampToString(game.getAddedTime()));
        gameVo.setEditedTime(CommonUtils.timestampToString(game.getEditedTime()));
        gameVo.set_s(game.get_s());

        return gameVo;
    }

}
