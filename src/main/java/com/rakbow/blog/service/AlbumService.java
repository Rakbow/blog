package com.rakbow.blog.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rakbow.blog.dao.AlbumMapper;
import com.rakbow.blog.data.ProductClass;
import com.rakbow.blog.data.album.AlbumFormat;
import com.rakbow.blog.data.album.MediaFormat;
import com.rakbow.blog.data.album.PublishFormat;
import com.rakbow.blog.entity.Album;
import com.rakbow.blog.entity.Product;
import com.rakbow.blog.util.AlbumUtil;
import com.rakbow.blog.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.*;

/**
 * @Project_name: blog
 * @Author: Rakbow
 * @Create: 2022-07-25 1:42
 * @Description:
 */
@Service
public class AlbumService {

    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;

    //新增专辑
    public int insertAlbum(Album album) {
        return albumMapper.insertAlbum(album);
    }

    //获取表中所有数据
    public List<Album> getAll() {
        return albumMapper.getAll();
    }

    public int findAlbumRows() {
        return albumMapper.selectAlbumRows();
    }

    //根据Id获取专辑
    public Album findAlbumById(int id) {
        return albumMapper.selectAlbumById(id);
    }

    //根据Id删除专辑
    public int deleteAlbumById(int id) {
        return albumMapper.deleteAlbumById(id);
    }

    //更新专辑
    public int updateAlbum(int id, Album album) {
        return albumMapper.updateAlbum(id, album);
    }

    //Album转json对象，以便前端展示
    public JSONObject album2Json(Album album) {
        JSONObject albumJson = (JSONObject) JSON.toJSON(album);
        albumJson.put("releaseDate", CommonUtil.dateToString(album.getReleaseDate(), "yyyy/MM/dd"));

        // List<JSONObject> publishFormat = new ArrayList<>();
        // for(String s : album.getPublishFormat().split(",")){
        //     JSONObject jo = new JSONObject();
        //     jo.put("index", Integer.parseInt(s));
        //     jo.put("name", PublishFormat.getNameByIndex(Integer.parseInt(s)));
        //     publishFormat.add(jo);
        // }
        //
        // List<JSONObject> mediaFormat = new ArrayList<>();
        // for(String s : album.getMediaFormat().split(",")){
        //     JSONObject jo = new JSONObject();
        //     jo.put("index", Integer.parseInt(s));
        //     jo.put("name", MediaFormat.getNameByIndex(Integer.parseInt(s)));
        //     mediaFormat.add(jo);
        // }
        //
        // List<JSONObject> albumFormat = new ArrayList<>();
        // for(String s : album.getAlbumFormat().split(",")){
        //     JSONObject jo = new JSONObject();
        //     jo.put("index", Integer.parseInt(s));
        //     jo.put("name", AlbumFormat.getNameByIndex(Integer.parseInt(s)));
        //     albumFormat.add(jo);
        // }

        List<String> publishFormat = new ArrayList();
        Arrays.stream(album.getPublishFormat().split(","))
                .forEach(i -> publishFormat.add(PublishFormat.getNameByIndex(Integer.parseInt(i))));

        List<String> albumFormat = new ArrayList();
        Arrays.stream(album.getAlbumFormat().split(","))
                .forEach(i -> albumFormat.add(AlbumFormat.getNameByIndex(Integer.parseInt(i))));

        List<String> mediaFormat = new ArrayList();
        Arrays.stream(album.getMediaFormat().split(","))
                .forEach(i -> mediaFormat.add(MediaFormat.getNameByIndex(Integer.parseInt(i))));

        List<String> product = new ArrayList();
        Arrays.stream(album.getProductId().split(","))
                .forEach(i -> product.add(productService.selectProductById(Integer.parseInt(i)).getNameZh() + "(" +
                        ProductClass.getNameByIndex(productService.selectProductById(Integer.parseInt(i)).getClassification()) + ")"));

        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("series", seriesService.selectSeriesById(album.getSeriesId()).getNameZh());
        albumJson.put("product", product);
        albumJson.put("addedTime", CommonUtil.dateToString(album.getAddedTime(), "yyyy/MM/dd hh:mm:ss"));
        albumJson.put("editedTime", CommonUtil.dateToString(album.getEditedTime(), "yyyy/MM/dd hh:mm:ss"));

        return albumJson;
    }

    //json对象转Album，以便保存到数据库
    public Album json2Album(JSONObject albumJson) throws ParseException {
        albumJson.put("releaseDate", CommonUtil.stringToDate(albumJson.getString("releaseDate"), "yyyy/MM/dd"));
        return albumJson.toJavaObject(Album.class);
    }

    //设置所有字段信息
    public List<String> getAlbumFields() {
        List<String> AlbumFields = null;
        Field[] f = Album.class.getClass().getFields();
        for (int i = 0; i < f.length; i++) {
            AlbumFields.add(f[i].getName());
        }
        return AlbumFields;
    }

    //更新专辑图片
    public void updateAlbumImgUrl(int id, String imgUrl){
        albumMapper.updateAlbumImgUrl(id, imgUrl);
    }

}
