package com.rakbow.blog.dao;

import com.rakbow.blog.entity.Album;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-07-19 1:01
 * @Description:
 */
@Mapper
public interface AlbumMapper {

    //根据id查询专辑信息
    Album selectAlbumById(int id);

    //插入新专辑
    int insertAlbum(Album album);

    //更新专辑信息
    int updateAlbum(int id, Album album);

    //删除单个专辑
    int deleteAlbumById(int id);

    //查询所有专辑信息 offset：每页起始行行号，limit：每页显示数量
    List<Album> getAll();

    //查询表里有多少条数据
    //如果sql语句中有动态的条件并且在<if>里使用
    // 且该方法有且只有一个参数，一定要取别名
    //@Param("userType") int userType
    int selectAlbumRows();

    //更新专辑图片
    int updateAlbumImgUrl(int id, String imgUrl);
}
