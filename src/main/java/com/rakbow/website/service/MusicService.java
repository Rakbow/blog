package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.MusicMapper;
import com.rakbow.website.data.ActionResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.emun.system.FileType;
import com.rakbow.website.data.vo.music.MusicVOAlpha;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.User;
import com.rakbow.website.util.common.VisitUtil;
import com.rakbow.website.util.convertMapper.MusicVOMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.util.common.CommonUtil;
import com.rakbow.website.util.common.DataFinder;
import com.rakbow.website.util.file.QiniuBaseUtil;
import com.rakbow.website.util.file.QiniuFileUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 2:04
 * @Description: music业务层
 */
@Service
public class MusicService {

    //region ------引入实例------
    @Autowired
    private MusicMapper musicMapper;
    @Autowired
    private QiniuBaseUtil qiniuBaseUtil;
    @Autowired
    private QiniuFileUtil qiniuFileUtil;
    @Autowired
    private VisitUtil visitUtil;

    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;
    //endregion

    //region -----曾删改查------

    /**
     * 根据音乐id获取音乐
     * @author rakbow
     * @param id 音乐id
     * @return music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Music getMusic(int id) {
        return musicMapper.getMusic(id, true);
    }

    /**
     * 根据Id获取Music,需要判断权限
     *
     * @param id id
     * @return Music
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Music getMusicWithAuth(int id, int userAuthority) {
        if(userAuthority > 2) {
            return musicMapper.getMusic(id, true);
        }
        return musicMapper.getMusic(id, false);
    }

    /**
     * 获取所有音乐数据
     * @author rakbow
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Music> getAll() {
        return musicMapper.getAll();
    }

    /**
     * 根据专辑id获取该专辑所有音乐
     * @author rakbow
     * @param albumId 专辑id
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<Music> getMusicsByAlbumId(int albumId) {
        return musicMapper.getMusicsByAlbumId(albumId);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public int getMusicRows() {
        return musicMapper.getMusicRows();
    }

    /**
     * 新增music
     * @param music music
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMusic(Music music) throws Exception {
        try {
            int id = musicMapper.addMusic(music);

        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 从专辑数据中新增music
     * @author rakbow
     * @param albumJson albumJson
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addMusicsFromAlbum(JSONObject albumJson) throws Exception {

        int albumId = albumJson.getInteger("id");

        JSONObject trackInfo = albumJson.getJSONObject("trackInfo");

        if (trackInfo == null || Objects.equals(trackInfo.toJSONString(), "{}")) {
            return;
        }

        JSONObject cover = albumJson.getJSONObject("cover");

        JSONArray discList = trackInfo.getJSONArray("disc_list");

        for (int i = 0; i < discList.size(); i++) {
            JSONObject disc = discList.getJSONObject(i);
            JSONArray trackList = disc.getJSONArray("track_list");
            for (int j = 0; j < trackList.size(); j++) {
                JSONObject track = trackList.getJSONObject(j);
                Music music = new Music();
                music.setName(track.getString("name"));
                music.setAlbumId(albumId);
                music.setDiscSerial(disc.getInteger("serial"));
                music.setTrackSerial(track.getString("serial"));
                music.setAudioLength(track.getString("audioLength"));
                music.setAddedTime(CommonUtil.stringToTimestamp(albumJson.getString("addedTime")));
                music.setEditedTime(CommonUtil.stringToTimestamp(albumJson.getString("editedTime")));
                addMusic(music);
            }
        }

    }

    /**
     * 更新music基础信息
     * @author rakbow
     * @param id,music music的id和music */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusic(int id, Music music) throws Exception {
        try {
            musicMapper.updateMusic(id, music);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.MUSIC.getNameZh());
    }

    /**
     * 根据id删除对应的music
     * @author rakbow
     * @param music music
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusic(Music music) throws Exception {
        try {
            //删除对应music的音频文件
            deleteMusicAllFiles(music);
            musicMapper.deleteMusicById(music.getId());
            visitUtil.deleteVisit(EntityType.MUSIC.getId(), music.getId());
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    /**
     * 根据专辑id删除对应的music
     * @author rakbow
     * @param albumId 专辑id
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteMusicByAlbumId(int albumId) throws Exception {
        try {
            List<Music> musics = getMusicsByAlbumId(albumId);
            //删除对应music的音频文件
            musics.forEach(this::deleteMusicAllFiles);
            musics.forEach(music -> visitUtil.deleteVisit(EntityType.MUSIC.getId(), music.getId()));
            musicMapper.deleteMusicByAlbumId(albumId);
        }catch (Exception ex) {
            throw new Exception(ex);
        }
    }

    //endregion

    //region ------处理数据------

    /**
     * 检测music数据
     * @author rakbow
     * @param musicJson musicJson
     * @return 错误信息
     * */
    public String checkMusicJson(JSONObject musicJson) {
        if (StringUtils.isBlank(musicJson.getString("name"))) {
            return ApiInfo.MUSIC_NAME_EMPTY;
        }
        if (StringUtils.isBlank(musicJson.getString("audioType"))) {
            return ApiInfo.MUSIC_AUDIO_TYPE_EMPTY;
        }
        if (StringUtils.isBlank(musicJson.getString("audioLength"))) {
            return ApiInfo.MUSIC_AUDIO_LENGTH_EMPTY;
        }
        return "";
    }

    /**
     * json转music
     * @author rakbow
     * @param musicJson musicJson
     * @return music
     * */
    public Music json2Music(@RequestBody JSONObject musicJson) {
        return JSON.to(Music.class, musicJson);
    }

    //endregion

    //region ------特殊查询------

    /**
     * 获取同属一张碟片的音频
     * @author rakbow
     * @param music 音乐
     * @return list
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<MusicVOAlpha> getRelatedMusics(Music music, String coverUrl) {

        //获取同属一张专辑的音频
        List<Music> sameAlbumMusics = getMusicsByAlbumId(music.getAlbumId());

        if (sameAlbumMusics.size() >= 5) {
            sameAlbumMusics = sameAlbumMusics.subList(0, 5);
        }

        List<Music> tmpList = DataFinder.findMusicByDiscSerial(music.getDiscSerial(), sameAlbumMusics);
        for (int i = 0; i < tmpList.size(); i++) {
            if (tmpList.get(i).getId() == music.getId()) {
                tmpList.remove(tmpList.get(i));
            }
        }

        //筛选出同一张碟片的音频，并按照序号排序
        return musicVOMapper.music2VOAlpha(tmpList, coverUrl);
    }

    //endregion


    //region -----更新数据------

    /**
     * 更新创作人员信息
     * @author rakbow
     * @param id 音乐id
     * @param artists 创作人员信息
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusicArtists(int id, String artists) {
        musicMapper.updateMusicArtists(id, artists, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_MUSIC_ARTISTS_SUCCESS;
    }

    /**
     * 更新歌词文本
     * @author rakbow
     * @param id 音乐id
     * @param lrcText 歌词文本
     * */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateMusicLyricsText(int id, String lrcText) {
        musicMapper.updateMusicLyricsText(id, lrcText, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_MUSIC_LYRICS_SUCCESS;
    }

    /**
     * 新增music文件
     *
     * @param id     id
     * @param files 新增文件数组
     * @param fileInfos 新增文件json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateMusicFile(int id, MultipartFile[] files, JSONArray fileInfos, User user) throws IOException {

        //原数据库里的文件信息
        JSONArray originalFiles = JSON.parseArray(getMusic(id).getFiles());

        //最终保存到数据库的json信息
        JSONArray addFiles = new JSONArray();

        //创建存储链接前缀
        String filePath = "file/" + EntityType.MUSIC.getNameEn().toLowerCase() + "/" + id + "/";

        for (int i = 0; i < files.length; i++) {

            ActionResult ar = new ActionResult();
            //判断文件是否为lrc歌词
            if (StringUtils.equals(files[i].getOriginalFilename().substring(files[i].getOriginalFilename().lastIndexOf(".")+1), "lrc")) {
                //上传lrc歌词文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.TEXT);
            }else {
                //上传音频文件
                ar = qiniuBaseUtil.uploadFileToQiniu(files[i], filePath, FileType.AUDIO);
            }
            if (ar.state) {
                JSONObject jo = new JSONObject();
                jo.put("url", ar.data.toString());
                jo.put("name", fileInfos.getJSONObject(i).getString("name"));
                jo.put("size", fileInfos.getJSONObject(i).getIntValue("size"));
                jo.put("type", fileInfos.getJSONObject(i).getString("type"));
                jo.put("uploadTime", CommonUtil.getCurrentTime());
                jo.put("uploadUser", user.getUsername());
                addFiles.add(jo);
            }
        }

        originalFiles.addAll(addFiles);

        musicMapper.updateMusicFiles(id, originalFiles.toJSONString(), new Timestamp(System.currentTimeMillis()));

    }

    /**
     * 删除音频文件
     *
     * @param id           id
     * @param deleteFiles 需要删除的文件信息
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteMusicFiles(int id, JSONArray deleteFiles) throws Exception {
        //获取原始文件json数组
        JSONArray files = JSONArray.parseArray(getMusic(id).getFiles());

        JSONArray finalFileJson = qiniuFileUtil.commonDeleteFiles(files, deleteFiles);

        musicMapper.updateMusicFiles(id, finalFileJson.toString(), new Timestamp(System.currentTimeMillis()));
        return ApiInfo.DELETE_FILES_SUCCESS;
    }

    /**
     * 删除所有音频文件
     *
     * @param music music
     * @author rakbow
     */
    public void deleteMusicAllFiles(Music music) {
        JSONArray files = JSON.parseArray(music.getFiles());
        //删除七牛云上的图片
        qiniuFileUtil.commonDeleteAllFiles(files);
    }

    //endregion

    /**
     * 检测上传文件是否合法
     *
     * @param id     id
     * @param fileInfos 新增文件json数据
     * @author rakbow
     */
    public boolean checkMusicUploadFile(int id, JSONArray fileInfos) {
        if (fileInfos.size() > 2) {
            return false;
        }

        //数据库中的文件信息
        JSONArray files = JSON.parseArray(getMusic(id).getFiles());
        if(files.size() + fileInfos.size() > 2) {
            return false;
        }

        fileInfos.addAll(files);
        int lrcFileNum = 0;
        int audioFileNum = 0;
        for (int i = 0; i < fileInfos.size(); i++) {
            //歌词文件数
            if (fileInfos.getJSONObject(i).getString("type").contains("text")) {
                lrcFileNum++;
            }
            //音频文件数
            if (fileInfos.getJSONObject(i).getString("type").contains("audio")) {
                audioFileNum++;
            }
        }

        return lrcFileNum <= 1 && audioFileNum <= 1;
    }

}
