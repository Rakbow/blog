package com.rakbow.website.dao;

import com.rakbow.website.entity.Album;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-19 1:01
 * @Description:
 */
@Mapper
public interface AlbumMapper {

    //根据id查询专辑信息
    Album getAlbumById(int id);

    //超详细查询
    List<Album> getAlbumsByFilter(String catalogNo, String name, List<Integer> franchises, List<Integer> products,
                                  List<Integer> publishFormat, List<Integer> albumFormat,
                                  List<Integer> mediaFormat, String hasBonus, String sortField, int sortOrder,
                                  int first, int row);

    //超详细查询条数
    int getAlbumRowsByFilter(String catalogNo, String name, List<Integer> franchises, List<Integer> products,
                             List<Integer> publishFormat, List<Integer> albumFormat,
                             List<Integer> mediaFormat, String hasBonus);

    //新增专辑
    void addAlbum(Album album);

    //更新专辑信息
    void updateAlbum(int id, Album album);

    //删除单个专辑
    void deleteAlbumById(int id);

    //查询所有专辑信息 offset：每页起始行行号，limit：每页显示数量
    List<Album> getAll();

    //查询表里有多少条数据
    //如果sql语句中有动态的条件并且在<if>里使用
    // 且该方法有且只有一个参数，一定要取别名
    //@Param("userType") int userType
    int getAlbumRows();

    //更新专辑图片
    void updateAlbumImages(int id, String images, Timestamp editedTime);

    //更新专辑Artists
    void updateAlbumArtists(int id, String artists, Timestamp editedTime);

    //更新音轨信息
    void updateAlbumTrackInfo(int id, String trackInfo, Timestamp editedTime);

    //更新描述信息
    void updateAlbumDescription(int id, String description, Timestamp editedTime);

    //更新特典信息
    void updateAlbumBonus(int id, String bonus, Timestamp editedTime);

    //获取最新添加专辑, limit
    List<Album> getAlbumOrderByAddedTime(int limit);

    //获取最新编辑专辑, limit
    List<Album> getAlbumOrderByEditedTime(int limit);
}
