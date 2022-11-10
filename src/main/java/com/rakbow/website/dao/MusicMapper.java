package com.rakbow.website.dao;

import com.rakbow.website.entity.Music;
import org.apache.ibatis.annotations.Mapper;

import java.sql.Timestamp;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 0:32
 * @Description:
 */
@Mapper
public interface MusicMapper {

    Music getMusicById(int id);

    List<Music> getAll();

    List<Music> getMusicsByAlbumId(int albumId);

    int getMusicRows();

    int addMusic(Music music);

    int updateMusic(int id, Music music);

    int deleteMusicById(int id);

    int deleteMusicByAlbumId(int albumId);

    int updateStatusById(int id);

    int updateMusicCoverUrl(int id, String coverUrl);

    int updateMusicArtists(int id, String artists, Timestamp editedTime);
    int updateMusicLyricsText(int id, String lrcText, Timestamp editedTime);
    int updateMusicDescription(int id, String description, Timestamp editedTime);

}
