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

    Music getMusic(int id, boolean status);

    List<Music> getMusics(List<Integer> ids);

    List<Music> getAll();

    List<Music> getMusicsByAlbumId(int albumId);

    int getMusicRows();

    int addMusic(Music music);

    void updateMusic(int id, Music music);

    void deleteMusicById(int id);

    void deleteMusicByAlbumId(int albumId);

    int updateStatusById(int id);

    void updateMusicCoverUrl(int albumId, String coverUrl);

    void updateMusicArtists(int id, String artists, Timestamp editedTime);

    void updateMusicLyricsText(int id, String lrcText, Timestamp editedTime);

    void updateMusicFiles(int id, String files, Timestamp editedTime);

}
