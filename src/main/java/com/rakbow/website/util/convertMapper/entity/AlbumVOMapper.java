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
    @Mapping(target = "releaseDate", source = "releaseDate", qualifiedByName = "getReleaseDate")
    @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    @Mapping(target = "companies", source = "companies", qualifiedByName = "getCompanies")
    @Mapping(target = "editCompanies", source = "companies", qualifiedByName = "getEditCompanies")
    @Mapping(target = "publishFormat", source = "publishFormat", qualifiedByName = "getPublishFormat")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "mediaFormat", source = "mediaFormat", qualifiedByName = "getMediaFormat")
    @Mapping(target = "artists", source = "artists", qualifiedByName = "getArtists")
    @Mapping(target = "trackInfo", ignore = true)
    @Mapping(target = "editDiscList", ignore = true)
    AlbumVO toVO(Album album);


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
    @Mapping(target = "visitNum", ignore = true)
    @Named("toVOAlpha")
    AlbumVOAlpha toVOAlpha(Album album);

    /**
     * Album转VO对象，信息量最少
     *
     * @param album 专辑
     * @return AlbumVOBeta
     * @author rakbow
     */
    @Mapping(target = "releaseDate", source = "releaseDate", qualifiedByName = "getReleaseDate")
    @Mapping(target = "cover", source = "images", qualifiedByName = "getThumbCover")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "addedTime", source = "addedTime", qualifiedByName = "getVOTime")
    @Mapping(target = "editedTime", source = "editedTime", qualifiedByName = "getVOTime")
    @Named("toVOBeta")
    AlbumVOBeta toVOBeta(Album album);

    /**
     * Album转VO对象，用于存储到搜索引擎
     *
     * @param album 专辑
     * @return AlbumVOGamma
     * @author rakbow
     */
    @Mapping(target = "hasBonus", source = "hasBonus", qualifiedByName = "getBool")
    @Mapping(target = "products", source = "products", qualifiedByName = "getProducts")
    @Mapping(target = "franchises", source = "franchises", qualifiedByName = "getFranchises")
    @Mapping(target = "albumFormat", source = "albumFormat", qualifiedByName = "getAlbumFormat")
    @Mapping(target = "cover", source = "images", qualifiedByName = "getThumb70CoverUrl")
    @Mapping(target = "visitCount", source = "id", qualifiedByName = "getVisitCount")
    @Mapping(target = "likeCount", source = "id", qualifiedByName = "getLikeCount")
    @Named("toVOGamma")
    AlbumVOGamma toVOGamma(Album album);

    /**
     * 列表转换, Album转VO对象，供album-list和album-index界面使用，信息量较少
     *
     * @param albums 专辑列表
     * @return List<AlbumVOAlpha>
     * @author rakbow
     */
    @IterableMapping(qualifiedByName = "toVOAlpha")
    List<AlbumVOAlpha> toVOAlpha(List<Album> albums);

    /**
     * 列表转换, Album转VO对象，信息量最少
     *
     * @param albums 专辑列表
     * @return List<AlbumVOBeta>
     * @author rakbow
     */
    @IterableMapping(qualifiedByName = "toVOBeta")
    List<AlbumVOBeta> toVOBeta(List<Album> albums);

    /**
     * 列表转换, Album转VO对象，用于存储到搜索引擎
     *
     * @param albums 专辑列表
     * @return List<AlbumVOGamma>
     * @author rakbow
     */
    @IterableMapping(qualifiedByName = "toVOGamma")
    List<AlbumVOGamma> toVOGamma(List<Album> albums);

    //region get property method

    @Named("getReleaseDate")
    default String getReleaseDate(Date date) {
        return DateUtil.dateToString(date);
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

    @Named("getCover")
    default JSONObject getCover(String images) {
        return CommonImageUtil.generateCover(images, EntityType.ALBUM);
    }

    @Named("getThumbCover")
    default JSONObject getThumbCover(String images) {
        return CommonImageUtil.generateThumbCover(images, EntityType.ALBUM, 50);
    }

    @Named("getThumb70CoverUrl")
    default String getThumb70Cover(String images) {
        return QiniuImageUtil.getThumb70Url(images);
    }

    @Named("getVisitCount")
    default long getVisitCount(int id) {
        VisitUtil visitUtil = SpringUtil.getBean("visitUtil");
        return visitUtil.getVisit(EntityType.ALBUM.getId(), id);
    }

    @Named("getLikeCount")
    default long getLikeCount(int id) {
        LikeUtil likeUtil = SpringUtil.getBean("likeUtil");
        return likeUtil.getLike(EntityType.ALBUM.getId(), id);
    }

    @Named("getCompanies")
    default JSONArray getCompanies(String companies) {
        return EntryUtil.getCompanies(companies);
    }

    @Named("getEditCompanies")
    default JSONArray getEditCompanies(String companies) {
        return JSONArray.parseArray(companies);
    }

    @Named("getArtists")
    default JSONArray getArtists(String artists) {
        return JSONArray.parseArray(artists);
    }

    //endregion

}
