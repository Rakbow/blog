package com.rakbow.website.util.convertMapper.entity;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.entity.album.AlbumFormat;
import com.rakbow.website.data.emun.entity.album.PublishFormat;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.common.MediaFormat;
import com.rakbow.website.data.vo.album.AlbumVO;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.album.AlbumVOBeta;
import com.rakbow.website.data.vo.album.AlbumVOGamma;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.common.LikeUtil;
import com.rakbow.website.util.common.SpringUtil;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.entity.AlbumUtil;
import com.rakbow.website.util.entity.FranchiseUtil;
import com.rakbow.website.util.entity.ProductUtil;
import com.rakbow.website.util.entry.EntryUtil;
import com.rakbow.website.util.file.CommonImageUtil;
import com.rakbow.website.util.file.QiniuImageUtil;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

        MusicService musicService = SpringUtil.getBean("musicService");
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
        albumVo.setReleaseDate(DateUtil.dateToString(album.getReleaseDate()));
        albumVo.setHasBonus(album.getHasBonus() == 1);

        //企业信息
        albumVo.setCompanies(EntryUtil.getCompanies(album.getCompanies()));
        //可供编辑的企业信息
        albumVo.setEditCompanies(JSONArray.parseArray(album.getCompanies()));

        //规格信息
        albumVo.setPublishFormat(PublishFormat.getAttribute(album.getPublishFormat()));
        albumVo.setAlbumFormat(AlbumFormat.getAttributes(album.getAlbumFormat()));
        albumVo.setMediaFormat(MediaFormat.getAttributes(album.getMediaFormat()));

        //大文本字段
        albumVo.setBonus(album.getBonus());
        albumVo.setArtists(JSONArray.parseArray(album.getArtists()));

        //可供编辑的editDiscList
        JSONArray editDiscList = AlbumUtil.getEditDiscList(album.getTrackInfo(), musics);
        //音轨信息
        JSONObject trackInfo = AlbumUtil.getFinalTrackInfo(album.getTrackInfo(), musics);

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
    @Mapping(target = "releaseDate", source = "releaseDate", qualifiedByName = "getReleaseDate")
    @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    @Mapping(target = "cover", source = "images", qualifiedByName = "getCover")
    @Mapping(target = "products", source = "products", qualifiedByName = "getProducts")
    @Mapping(target = "franchises", source = "franchises", qualifiedByName = "getFranchises")
    @Mapping(target = "publishFormat", source = "publishFormat", qualifiedByName = "getPublishFormat")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "mediaFormat", source = "mediaFormat", qualifiedByName = "getMediaFormat")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "status", source = "status", qualifiedByName = "getBool")
    @Named("toVOAlpha")
    AlbumVOAlpha toVOAlpha(Album album);

    /**
     * 列表转换, Album转VO对象，供album-list和album-index界面使用，信息量较少
     *
     * @param albums 专辑列表
     * @return List<AlbumVOAlpha>
     * @author rakbow
     */
    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<AlbumVOAlpha> toVOAlpha(List<Album> albums);

    @Named("getReleaseDate")
    default String getReleaseDate(Date date) {
        return DateUtil.dateToString(date);
    }

    @Named("getCover")
    default JSONObject getCover(String images) {
        return CommonImageUtil.generateCover(images, EntityType.ALBUM);
    }

    @Named("getProducts")
    default JSONArray getProducts(String products) {
        return ProductUtil.getProductList(products);
    }

    @Named("getFranchises")
    default JSONArray getFranchises(String franchises) {
        return FranchiseUtil.getFranchiseList(franchises);
    }

    @Named("getPublishFormat")
    default JSONArray getPublishFormat(String formats) {
        return PublishFormat.getAttribute(formats);
    }

    @Named("getAlbumFormat")
    default JSONArray getAlbumFormat(String formats) {
        return AlbumFormat.getAttributes(formats);
    }

    @Named("getMediaFormat")
    default JSONArray getMediaFormat(String formats) {
        return MediaFormat.getAttributes(formats);
    }

    @Named("getVOTime")
    default String getVOTime(Timestamp timestamp) {
        return DateUtil.timestampToString(timestamp);
    }

    @Named("getBool")
    default boolean getBool(int bool) {
        return bool == 1;
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
        albumVOBeta.setReleaseDate(DateUtil.dateToString(album.getReleaseDate()));
        albumVOBeta.setCover(CommonImageUtil.generateThumbCover(album.getImages(), EntityType.ALBUM, 50));
        albumVOBeta.setAlbumFormat(AlbumFormat.getAttributes(album.getAlbumFormat()));
        albumVOBeta.setAddedTime(DateUtil.timestampToString(album.getAddedTime()));
        albumVOBeta.setEditedTime(DateUtil.timestampToString(album.getEditedTime()));

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

        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");

        AlbumVOGamma albumVOGamma = new AlbumVOGamma();
        albumVOGamma.setId(album.getId());
        albumVOGamma.setCatalogNo(album.getCatalogNo());
        albumVOGamma.setName(album.getName());
        albumVOGamma.setNameEn(album.getNameEn());
        albumVOGamma.setNameZh(album.getNameZh());
        albumVOGamma.setReleaseDate(DateUtil.dateToString(album.getReleaseDate()));
        albumVOGamma.setHasBonus(album.getHasBonus() == 1);

        //关联信息
        albumVOGamma.setProducts(ProductUtil.getProductList(album.getProducts()));
        albumVOGamma.setFranchises(FranchiseUtil.getFranchiseList(album.getFranchises()));
        albumVOGamma.setAlbumFormat(AlbumFormat.getAttributes(album.getAlbumFormat()));

        albumVOGamma.setCover(QiniuImageUtil.getThumb70Url(album.getImages()));

        albumVOGamma.setVisitCount(visitUtil.getVisit(EntityType.ALBUM.getId(), album.getId()));
        albumVOGamma.setLikeCount(likeUtil.getLike(EntityType.ALBUM.getId(), album.getId()));

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
