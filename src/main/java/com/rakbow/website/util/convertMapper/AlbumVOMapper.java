package com.rakbow.website.util.convertMapper;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.CommonConstant;
import com.rakbow.website.data.segmentImagesResult;
import com.rakbow.website.data.vo.album.AlbumVO;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.album.AlbumVOBeta;
import com.rakbow.website.data.vo.album.AlbumVOGamma;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.entity.AlbumUtils;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.entity.FranchiseUtils;
import com.rakbow.website.util.file.CommonImageUtils;
import com.rakbow.website.util.entity.ProductUtils;
import com.rakbow.website.util.common.SpringUtils;
import com.rakbow.website.util.file.QiniuImageUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-01-11 16:13
 * @Description: album VO转换接口
 */
@Mapper(componentModel = "spring")
public interface AlbumVOMapper {

    AlbumVOMapper INSTANCES = Mappers.getMapper(AlbumVOMapper.class);

    /**
     * Album转VO对象，用于详情页面，转换量最大的
     *
     * @param album 专辑
     * @return AlbumVO
     * @author rakbow
     */
    default AlbumVO album2VO(Album album) {
        if (album == null) {
            return null;
        }
        AlbumVO albumVo = new AlbumVO();

        MusicService musicService = SpringUtils.getBean("musicService");
        List<Music> musics = musicService.getMusicsByAlbumId(album.getId());

        //基础信息
        albumVo.setId(album.getId());
        albumVo.setCatalogNo(album.getCatalogNo());
        albumVo.setName(album.getName());
        albumVo.setNameEn(album.getNameEn());
        albumVo.setNameZh(album.getNameZh());
        albumVo.setBarcode(album.getBarcode());
        albumVo.setPrice(album.getPrice());
        albumVo.setCurrencyUnit(album.getCurrencyUnit());
        albumVo.setRemark(album.getRemark());
        albumVo.setReleaseDate(CommonUtils.dateToString(album.getReleaseDate()));
        albumVo.setHasBonus(album.getHasBonus() == 1);

        //厂商
        albumVo.setLabel(album.getLabel());
        albumVo.setPublisher(album.getPublisher());
        albumVo.setDistributor(album.getDistributor());
        albumVo.setCopyright(album.getCopyright());

        //规格信息
        albumVo.setPublishFormat(AlbumUtils.getPublishFormat(album.getPublishFormat()));
        albumVo.setAlbumFormat(AlbumUtils.getAlbumFormat(album.getAlbumFormat()));
        albumVo.setMediaFormat(AlbumUtils.getMediaFormat(album.getMediaFormat()));

        //大文本字段
        albumVo.setBonus(album.getBonus());
        albumVo.setArtists(JSONArray.parseArray(album.getArtists()));

        //可供编辑的editDiscList
        JSONArray editDiscList = AlbumUtils.getEditDiscList(album.getTrackInfo(), musics);
        //音轨信息
        JSONObject trackInfo = AlbumUtils.getFinalTrackInfo(album.getTrackInfo(), musics);

        albumVo.setEditDiscList(editDiscList);
        albumVo.setTrackInfo(trackInfo);

        return albumVo;
    }

    /**
     * Album转VO，供album-list和album-index界面使用，信息量较少
     *
     * @param album 专辑
     * @return AlbumVOAlpha
     * @author rakbow
     */
    default AlbumVOAlpha album2VOAlpha(Album album) {
        if (album == null) {
            return null;
        }
        AlbumVOAlpha albumVOAlpha = new AlbumVOAlpha();

        //基础信息
        albumVOAlpha.setId(album.getId());
        albumVOAlpha.setCatalogNo(album.getCatalogNo());
        albumVOAlpha.setName(album.getName());
        albumVOAlpha.setNameEn(album.getNameEn());
        albumVOAlpha.setNameZh(album.getNameZh());
        albumVOAlpha.setBarcode(album.getBarcode());
        albumVOAlpha.setPrice(album.getPrice());
        albumVOAlpha.setCurrencyUnit(album.getCurrencyUnit());
        albumVOAlpha.setRemark(album.getRemark());
        albumVOAlpha.setReleaseDate(CommonUtils.dateToString(album.getReleaseDate()));
        albumVOAlpha.setHasBonus(album.getHasBonus() == 1);

        //图片相关
        albumVOAlpha.setCover(CommonImageUtils.generateCover(album.getImages()));

        //关联信息
        albumVOAlpha.setProducts(ProductUtils.getProductList(album.getProducts()));
        albumVOAlpha.setFranchises(FranchiseUtils.getFranchiseList(album.getFranchises()));

        //厂商信息
        albumVOAlpha.setLabel(album.getLabel());
        albumVOAlpha.setPublisher(album.getPublisher());
        albumVOAlpha.setDistributor(album.getDistributor());
        albumVOAlpha.setCopyright(album.getCopyright());

        //规格信息
        albumVOAlpha.setPublishFormat(AlbumUtils.getPublishFormat(album.getPublishFormat()));
        albumVOAlpha.setAlbumFormat(AlbumUtils.getAlbumFormat(album.getAlbumFormat()));
        albumVOAlpha.setMediaFormat(AlbumUtils.getMediaFormat(album.getMediaFormat()));

        //审计字段
        albumVOAlpha.setAddedTime(CommonUtils.timestampToString(album.getAddedTime()));
        albumVOAlpha.setEditedTime(CommonUtils.timestampToString(album.getEditedTime()));
        albumVOAlpha.set_s(album.get_s());

        return albumVOAlpha;
    }

    /**
     * 列表转换, Album转VO对象，供album-list和album-index界面使用，信息量较少
     *
     * @param albums 专辑列表
     * @return List<AlbumVOAlpha>
     * @author rakbow
     */
    default List<AlbumVOAlpha> album2VOAlpha(List<Album> albums) {
        List<AlbumVOAlpha> albumVOAlphas = new ArrayList<>();

        if (!albums.isEmpty()) {
            albums.forEach(album -> {
                albumVOAlphas.add(album2VOAlpha(album));
            });
        }

        return albumVOAlphas;
    }

    /**
     * Album转VO对象，信息量最少
     *
     * @param album 专辑
     * @return AlbumVOBeta
     * @author rakbow
     */
    default AlbumVOBeta album2VOBeta(Album album) {
        if (album == null) {
            return null;
        }
        AlbumVOBeta albumVOBeta = new AlbumVOBeta();

        //基础信息
        albumVOBeta.setId(album.getId());
        albumVOBeta.setCatalogNo(album.getCatalogNo());
        albumVOBeta.setName(album.getName());
        albumVOBeta.setNameEn(album.getNameEn());
        albumVOBeta.setNameZh(album.getNameZh());
        albumVOBeta.setReleaseDate(CommonUtils.dateToString(album.getReleaseDate()));
        albumVOBeta.setCover(CommonImageUtils.generateThumbCover(album.getImages(), 50));
        albumVOBeta.setAlbumFormat(AlbumUtils.getAlbumFormat(album.getAlbumFormat()));
        albumVOBeta.setAddedTime(CommonUtils.timestampToString(album.getAddedTime()));
        albumVOBeta.setEditedTime(CommonUtils.timestampToString(album.getEditedTime()));

        return albumVOBeta;
    }

    /**
     * 列表转换, Album转VO对象，信息量最少
     *
     * @param albums 专辑列表
     * @return List<AlbumVOBeta>
     * @author rakbow
     */
    default List<AlbumVOBeta> album2VOBeta(List<Album> albums) {
        List<AlbumVOBeta> albumVOBetas = new ArrayList<>();

        if (!albums.isEmpty()) {
            albums.forEach(album -> {
                albumVOBetas.add(album2VOBeta(album));
            });
        }

        return albumVOBetas;
    }

    /**
     * Album转VO对象，用于存储到搜索引擎
     *
     * @param album 专辑
     * @return AlbumVOGamma
     * @author rakbow
     */
    default AlbumVOGamma album2VOGamma(Album album) {
        if (album == null) {
            return null;
        }
        AlbumVOGamma albumVOGamma = new AlbumVOGamma();
        albumVOGamma.setId(album.getId());
        albumVOGamma.setCatalogNo(album.getCatalogNo());
        albumVOGamma.setName(album.getName());
        albumVOGamma.setNameEn(album.getNameEn());
        albumVOGamma.setNameZh(album.getNameZh());
        albumVOGamma.setReleaseDate(CommonUtils.dateToString(album.getReleaseDate()));
        albumVOGamma.setHasBonus(album.getHasBonus() == 1);

        //关联信息
        albumVOGamma.setProducts(ProductUtils.getProductList(album.getProducts()));
        albumVOGamma.setFranchises(FranchiseUtils.getFranchiseList(album.getFranchises()));
        albumVOGamma.setAlbumFormat(AlbumUtils.getAlbumFormat(album.getAlbumFormat()));

        albumVOGamma.setCover(QiniuImageUtils.getThumb70Url(album.getImages()));

        return albumVOGamma;
    }

    /**
     * 列表转换, Album转VO对象，用于存储到搜索引擎
     *
     * @param albums 专辑列表
     * @return List<AlbumVOGamma>
     * @author rakbow
     */
    default List<AlbumVOGamma> album2VOGamma(List<Album> albums) {
        List<AlbumVOGamma> albumVOGammas = new ArrayList<>();

        if (!albums.isEmpty()) {
            albums.forEach(album -> {
                albumVOGammas.add(album2VOGamma(album));
            });
        }

        return albumVOGammas;
    }

}
