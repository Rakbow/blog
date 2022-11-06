package com.rakbow.website.dao;

import com.rakbow.website.entity.Music;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 0:32
 * @Description:
 */
@Mapper
public interface MusicMapper {

    Music selectMusicById(int id);

    List<Music> getAll();

    List<Music> selectMusicsByAlbumId(int albumId);

    int selectMusicRows();

    int insertMusic(Music music);

    int updateMusic(int id, Music music);

    int deleteMusicById(int id);

    int updateStatusById(int id);

}
