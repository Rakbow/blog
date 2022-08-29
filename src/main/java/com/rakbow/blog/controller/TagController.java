package com.rakbow.blog.controller;

import com.rakbow.blog.entity.Tag;
import com.rakbow.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-08-14 2:58
 * @Description:
 */
@Controller
public class TagController {

    @Autowired
    private TagService tagService;

    //获取所有标签
    @RequestMapping(value = "/getAllTag",method = RequestMethod.GET)
    @ResponseBody
    public List<Tag> getAllTag() {
        List<Tag> tags = tagService.getAll();
        return tags;
    }

    //新增标签
    @RequestMapping(value = "/insertTag", method = RequestMethod.POST)
    public void insertTag(String tagName){
        Tag tag = new Tag();
        tag.setName(tagName);
        tagService.insertTag(tag);
    }

    //删除标签
    @RequestMapping(value = "/deleteTag", method = RequestMethod.DELETE)
    public void insertTag(int tagId){
        tagService.deleteTag(tagId);
    }
}
