package com.rakbow.website.controller.entity;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.annotation.UniqueVisitor;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.*;
import com.rakbow.website.util.common.DateUtil;
import com.rakbow.website.util.common.EntityUtils;
import com.rakbow.website.util.entity.MusicUtil;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.ApiResult;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import com.rakbow.website.util.convertMapper.MusicVOMapper;
import com.rakbow.website.util.file.CommonImageUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-06 19:50
 * @Description:
 */
@Controller
@RequestMapping("/db/music")
public class MusicController {

    //region ------引入实例------
    @Resource
    private MusicService musicService;
    @Resource
    private AlbumService albumService;
    @Resource
    private UserService userService;
    @Resource
    private EntityUtils entityUtils;
    @Resource
    private EntityService entityService;

    private final MusicVOMapper musicVOMapper = MusicVOMapper.INSTANCES;
    //endregion

    //获取单个音频详细信息页面
    @UniqueVisitor
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getMusicDetail(@PathVariable("id") int id, Model model, HttpServletRequest request) {
        Music music = musicService.getMusicWithAuth(id, userService.getUserOperationAuthority(userService.getUserByRequest(request)));
        if (music == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.MUSIC.getNameZh()));
            return "/error/404";
        }
        Album album = albumService.getAlbum(music.getAlbumId());
        String coverUrl = CommonImageUtil.getCoverUrl(album.getImages());

        model.addAttribute("music", musicVOMapper.music2VO(music, coverUrl));

        if(userService.getUserOperationAuthority(userService.getUserByRequest(request)) > 0) {
            model.addAttribute("audioInfo", MusicUtil.getMusicAudioInfo(music, coverUrl));
        }
        //前端选项数据
        model.addAttribute("options", entityUtils.getDetailOptions(EntityType.MUSIC.getId()));
        //获取页面数据
        model.addAttribute("pageInfo", entityService.getPageInfo(EntityType.MUSIC.getId(), id, music, request));
        //实体类通用信息
        model.addAttribute("detailInfo", EntityUtils.getMetaDetailInfo(music, EntityType.MUSIC.getId()));
        //获取同属一张碟片的音频
        model.addAttribute("relatedMusics", musicService.getRelatedMusics(music, coverUrl));
        //获取所属专辑的信息
        model.addAttribute("relatedAlbum", AlbumVOMapper.INSTANCES.album2VOBeta(album));

        return "/database/itemDetail/music-detail";

    }

    //更新Music
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusic(@RequestBody  String json) {
        ApiResult res = new ApiResult();
        try{
            JSONObject param = JSON.parseObject(json);
            Music music = entityService.json2Entity(param, Music.class);

            //检测数据
            String errorMsg= musicService.checkMusicJson(param);
            if(!StringUtils.isBlank(errorMsg)) {
                res.setErrorMessage(errorMsg);
                return JSON.toJSONString(res);
            }

            //修改编辑时间
            music.setEditedTime(DateUtil.NOW_TIMESTAMP);

            res.message = musicService.updateMusic(music.getId(), music);
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新music创作人员信息
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicArtists(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String artists = JSON.parseObject(json).get("artists").toString();

            res.message = musicService.updateMusicArtists(id, artists);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //更新歌词文本
    @RequestMapping(path = "/update-lyrics-text", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicLyricsText(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            String lyricsText = JSON.parseObject(json).get("lyricsText").toString();

            res.message = musicService.updateMusicLyricsText(id, lyricsText);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //新增音频文件
    @RequestMapping(path = "/upload-file", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicFile(int id, MultipartFile[] files, String fileInfos, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (files == null || files.length == 0) {
                res.setErrorMessage(ApiInfo.INPUT_FILE_EMPTY);
                return JSON.toJSONString(res);
            }

            //检测数据是否合法
            if (!musicService.checkMusicUploadFile(id, JSON.parseArray(fileInfos))) {
                res.setErrorMessage(ApiInfo.MUSIC_FILE_NUMBER_EXCEPTION);
                return JSON.toJSONString(res);
            }

            JSONArray fileInfosJson = JSON.parseArray(fileInfos);

            musicService.updateMusicFile(id, files, fileInfosJson, userService.getUserByRequest(request));
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

    //删除音频文件
    @RequestMapping(path = "/delete-file", method = RequestMethod.POST)
    @ResponseBody
    public String deleteMusicFile(@RequestBody String json) {
        ApiResult res = new ApiResult();
        try {
            int id = JSON.parseObject(json).getInteger("id");
            JSONArray files = JSON.parseObject(json).getJSONArray("files");
            res.message = musicService.deleteMusicFiles(id, files);
        } catch (Exception e) {
            res.setErrorMessage(e);
        }
        return JSON.toJSONString(res);
    }

}
