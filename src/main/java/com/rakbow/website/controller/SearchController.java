package com.rakbow.website.controller;

import com.rakbow.website.entity.Album;
import com.rakbow.website.service.AlbumService;
// import com.rakbow.website.service.ElasticsearchService;
import com.rakbow.website.service.ElasticsearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-13 22:18
 * @Description:
 */
@Controller
public class SearchController {

    @Autowired
    private ElasticsearchService elasticsearchService;

    @Autowired
    private AlbumService albumService;

    // s?keyword=xxx
    @RequestMapping(path = "/s", method = RequestMethod.GET)
    public String search(String keyword, Model model) throws IOException, ParseException {

        List<Album> searchResult = elasticsearchService.searchAlbum(keyword);

        model.addAttribute("searchResult", searchResult);
        model.addAttribute("keyword", keyword);
        model.addAttribute("totals", (searchResult == null) ? 0 : searchResult.size());
        return "/site/search";
    }

}
