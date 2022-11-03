package com.rakbow.website;

import com.rakbow.website.entity.Album;
import com.rakbow.website.service.AlbumService;
import com.rakbow.website.util.common.CommonUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class WebSiteApplicationTests {

	@Autowired
	private AlbumService albumService;

	@Test
	void contextLoads() {
	}

	@Test
	public void test1(){
		Album album = albumService.findAlbumById(55);
		List<Album> albums = new ArrayList<>();
		albums.add(album);
		albums.add(album);
		albums.add(album);
		albums.add(album);
		System.out.println("原数组：" + albums);
		albums = CommonUtil.removeDuplicateList(albums);
		System.out.println("后数组：" + albums );
	}

}
