package com.rakbow.website.controller;

import com.rakbow.website.data.EntityType;
import com.rakbow.website.entity.Music;
import com.rakbow.website.service.MusicService;
import com.rakbow.website.service.VisitService;
import com.rakbow.website.util.AlbumUtil;
import com.rakbow.website.util.MusicUtil;
import com.rakbow.website.util.common.ApiInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    private VisitService visitService;
    //endregion

    //获取单个音频详细信息页面
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getMusicDetail(@PathVariable("id") int musicId, Model model) {
        if (musicService.selectMusicById(musicId) == null) {
            model.addAttribute("errorMessage", String.format(ApiInfo.GET_DATA_FAILED_404, EntityType.MUSIC.getName()));
            return "/error/404";
        }
        //访问数+1
        visitService.increaseVisit(EntityType.MUSIC.getId(), musicId);

        Music music = musicService.selectMusicById(musicId);

        model.addAttribute("audioTypeSet", MusicUtil.getAudioTypeSet());
        model.addAttribute("music", musicService.music2Json(music));
        //获取页面访问量
        model.addAttribute("visitNum", visitService.getVisit(EntityType.MUSIC.getId(), music.getId()).getVisitNum());
        //获取同属一张碟片的音频
        model.addAttribute("relatedMusics", musicService.getRelatedMusics(music));

        return "/music-detail";

    }

}
