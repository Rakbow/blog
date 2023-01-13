package com.rakbow.website.controller;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.*;
import com.rakbow.website.util.MusicUtil;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.ApiResult;
import com.rakbow.website.util.convertMapper.AlbumVOMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    @Autowired
    private MusicService musicService;
    @Autowired
    private AlbumService albumService;
    @Autowired
    private VisitService visitService;
    @Autowired
    private UserService userService;
    // @Autowired
    // private ElasticsearchService elasticsearchService;
    //endregion

    //获取单个音频详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getMusicDetail(@PathVariable("id") int musicId, Model model) {
        if (musicService.getMusicById(musicId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.MUSIC.getNameZh()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.MUSIC.getId(), musicId);

        Music music = musicService.getMusicById(musicId);

        model.addAttribute("audioTypeSet", MusicUtil.getAudioTypeSet());
        model.addAttribute("music", musicService.music2Json(music));
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.MUSIC.getId(), music.getId()).getVisitNum());
        //获取同属一张碟片的音频
        model.addAttribute("relatedMusics", musicService.getRelatedMusics(musicId));
        //获取所属专辑的信息
        model.addAttribute("relatedAlbum", AlbumVOMapper.INSTANCES.album2VOBeta(albumService.getAlbumById(music.getAlbumId())));

        return "/music/music-detail";

    }

    //更新Music
    @RequestMapping(path = "/update", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusic(@RequestBody  String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        JSONObject param = JSON.parseObject(json);
        try{
            if (userService.checkAuthority(request).state) {

                Music music = musicService.json2Music(param);

                //检测数据
                if(!StringUtils.isBlank(musicService.checkMusicJson(param))) {
                    res.setErrorMessage(musicService.checkMusicJson(param));
                    return JSON.toJSONString(res);
                }

                //修改编辑时间
                music.setEditedTime(new Timestamp(System.currentTimeMillis()));

                musicService.updateMusic(music.getId(), music);

                //将更新的专辑保存到Elasticsearch服务器索引中

                res.message = String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.MUSIC.getNameZh());

            }else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
        } catch (Exception ex) {
            res.setErrorMessage(ex.getMessage());
        }
        return JSON.toJSONString(res);
    }

    //更新music创作人员信息
    @RequestMapping(path = "/update-artists", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicArtists(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String artists = JSON.parseObject(json).get("artists").toString();
                musicService.updateMusicArtists(id, artists);
                res.message = ApiInfo.UPDATE_MUSIC_ARTISTS_SUCCESS;
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //更新歌词文本
    @RequestMapping(path = "/update-lyrics-text", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicLyricsText(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String lyricsText = JSON.parseObject(json).get("lyricsText").toString();
                musicService.updateMusicLyricsText(id, lyricsText);
                res.message = ApiInfo.UPDATE_MUSIC_LYRICS_SUCCESS;
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

    //更新描述信息
    @RequestMapping(path = "/update-description", method = RequestMethod.POST)
    @ResponseBody
    public String updateMusicDescription(@RequestBody String json, HttpServletRequest request) {
        ApiResult res = new ApiResult();
        try {
            if (userService.checkAuthority(request).state) {
                int id = JSON.parseObject(json).getInteger("id");
                String description = JSON.parseObject(json).get("description").toString();
                musicService.updateMusicDescription(id, description);
                res.message = ApiInfo.UPDATE_MUSIC_DESCRIPTION_SUCCESS;
            } else {
                res.setErrorMessage(userService.checkAuthority(request).message);
            }
            return JSON.toJSONString(res);
        } catch (Exception e) {
            res.setErrorMessage(e);
            return JSON.toJSONString(res);
        }
    }

}
