package com.rakbow.website;

import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Tag;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.service.TagService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-25 1:19
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = WebSiteApplication.class)
public class MapperTests {

    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private AlbumService albumService;

    @Autowired
    private TagService tagService;

    @Test
    public void insertAlbumTest(){
        Album album = new Album();
        albumService.insertAlbum(album);
    }

    // @Test
    // public void updateAlbumTest() throws ParseException {
    //     AlbumInfo albumInfo = new AlbumInfo();
    //     albumInfo.setId(48);
    //     albumInfo.setReleaseDate("");
    //     albumInfo.setType("1");
    //     albumInfo.setRemark("1");
    //     albumInfo.setSeries("1");
    //     albumInfo.setPublisher("1");
    //     albumInfo.setNameZh("1");
    //     albumInfo.setName("1");
    //     albumInfo.setPrice(0);
    //     albumInfo.setTrackNum(0);
    //     albumInfo.setDiscNum(0);
    //     albumInfo.setDescription("1");
    //     albumInfo.setArtist("1");
    //     albumInfo.setCatalogNo("1");
    //     System.out.println(albumService.toAlbum(albumInfo).toString());
    //     albumMapper.updateAlbum(albumInfo.getId(), albumService.toAlbum(albumInfo));
    // }

    @Test
    public void testTag(){
        Tag tag = new Tag();
        tag.setName("");
        tagService.insertTag(tag);
    }

    // @Test
    // public void testUpdateAlbum(){
    //     List<Album> albums = albumService.getAll();
    //     for(Album album : albums){
    //         if(!StringUtils.isBlank(album.getCatalogNo())){
    //             album.setCoverUrl("http://localhost:8080/blog/img/album/cover/" + album.getCatalogNo() + "_album_cover.jpg");
    //             albumService.updateAlbum(album.getId(),album);
    //         }
    //     }
    // }

    @Test
    public void testInsertTagInfo(){
        tagService.insertTagByItemTypeIdAndItemId(1,1,1);
        tagService.insertTagByItemTypeIdAndItemId(1,1,2);
        tagService.insertTagByItemTypeIdAndItemId(1,1,3);
        tagService.insertTagByItemTypeIdAndItemId(1,1,4);
        tagService.insertTagByItemTypeIdAndItemId(1,1,5);
        tagService.insertTagByItemTypeIdAndItemId(1,2,1);
        tagService.insertTagByItemTypeIdAndItemId(1,2,2);
        tagService.insertTagByItemTypeIdAndItemId(1,2,3);
        tagService.insertTagByItemTypeIdAndItemId(1,3,5);
        tagService.insertTagByItemTypeIdAndItemId(1,3,6);
        tagService.insertTagByItemTypeIdAndItemId(1,3,8);
    }

    @Test
    public void testDeleteTag(){
        tagService.deleteAllTagsByItemTypeIdAndItemId(1,3);
    }

    @Test
    public void testGetAllTags(){
        List<String> tags = new ArrayList<>();
        List<Integer> tags_temp = tagService.getAllTagsByItemTypeIdAndItemId(1,1);
        for(int i : tags_temp){
            tags.add(tagService.selectTagById(i).getName());
        }
        tags.stream().forEach(i -> System.out.println(i));
    }
}
