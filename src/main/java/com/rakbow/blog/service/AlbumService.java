package com.rakbow.blog.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.rakbow.blog.dao.AlbumMapper;
import com.rakbow.blog.data.ProductClass;
import com.rakbow.blog.data.album.AlbumFormat;
import com.rakbow.blog.data.album.MediaFormat;
import com.rakbow.blog.data.album.PublishFormat;
import com.rakbow.blog.entity.Album;
import com.rakbow.blog.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    //根据具体搜索条件查询
    public List<Album> selectAlbumByFilter(String queryParam) {
        JSONObject param = JSON.parseObject(queryParam);
        String publishFormat = (param.get("publishFormat") == null) ? null : param.get("publishFormat").toString();
        String albumFormat = (param.get("albumFormat") == null) ? null : param.get("albumFormat").toString();
        String mediaFormat = (param.get("mediaFormat") == null) ? null : param.get("mediaFormat").toString();
        String productId = (param.get("productId") == null) ? null : param.get("productId").toString();
        return albumMapper.selectAlbumByFilter(publishFormat, albumFormat, mediaFormat, productId);
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

    //Album转Json对象，以便前端展示
    public JSONObject album2Json(Album album) {

        JSONObject albumJson = (JSONObject) JSON.toJSON(album);

        albumJson.put("releaseDate", CommonUtil.dateToString(album.getReleaseDate(), "yyyy/MM/dd"));

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
        List<JSONObject> productMap = new ArrayList<>();
        Arrays.stream(album.getProductId().split(","))
                .forEach(i -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", Integer.parseInt(i));
                    jo.put("name", productService.selectProductById(Integer.parseInt(i)).getNameZh() + "(" +
                            ProductClass.getNameByIndex(productService.selectProductById(Integer.parseInt(i)).getClassification()) + ")");
                    productMap.add(jo);
                });

        JSONArray credits = JSONArray.parseArray(album.getCredits());

        albumJson.put("publishFormat", publishFormat);
        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("credits",credits);
        albumJson.put("series", seriesService.selectSeriesById(album.getSeriesId()).getNameZh());
        albumJson.put("product", product);
        albumJson.put("products", productMap);
        albumJson.put("addedTime", CommonUtil.dateToString(album.getAddedTime(), "yyyy/MM/dd hh:mm:ss"));
        albumJson.put("editedTime", CommonUtil.dateToString(album.getEditedTime(), "yyyy/MM/dd hh:mm:ss"));
        return albumJson;
    }

    //json对象转Album，以便保存到数据库
    public Album json2Album(JSONObject albumJson) throws ParseException {
        albumJson.put("releaseDate", CommonUtil.stringToDate(albumJson.getString("releaseDate"), "yyyy/MM/dd"));
        Album album = albumJson.toJavaObject(Album.class);
        album.setAddedTime(new Timestamp(System.currentTimeMillis()));
        album.setEditedTime(new Timestamp(System.currentTimeMillis()));
        System.out.println(album);
        return album;
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
    public void updateAlbumImgUrl(int id, String imgUrl) {
        albumMapper.updateAlbumImgUrl(id, imgUrl);
    }

    //更新专辑Staff
    public void updateAlbumCredits(int id, String credits) {
        albumMapper.updateAlbumCredits(id, credits);
    }

    //获取相关专辑
    public List<JSONObject> getRelatedAlbums(int id) {
        String[] productId = findAlbumById(id).getProductId().split(",");
        List<JSONObject> relatedAlbums = new ArrayList<>();
        if (productId.length == 1) {
            List<Album> tmp = selectAlbumByFilter("{\"productId\":\"" + productId[0] + "\"}");
            for (int i = 0; i < 4; i++) {
                relatedAlbums.add(album2Json(tmp.get(i)));
            }
        } else if (productId.length > 1 && productId.length <= 4) {
            relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productId[0] + "\"}").get(0)));
            relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productId[0] + "\"}").get(1)));
            relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productId[1] + "\"}").get(0)));
            relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productId[1] + "\"}").get(1)));
        } else {
            for (int i = 0; i < 4; i++) {
                relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productId[i] + "\"}").get(i)));
            }
        }
        for (JSONObject album : relatedAlbums) {


            JSONArray imgUrl = JSONArray.parseArray(album.get("imgUrl").toString());

            if (imgUrl.isEmpty()) {
                JSONObject tmp = new JSONObject();
                tmp.put("url", "http://localhost:8080/blog/img/404.jpg");
                tmp.put("name", "Cover");
                album.put("imgUrl", tmp);
            } else {
                for (int i = 0; i < imgUrl.size(); i++) {
                    JSONObject img = imgUrl.getJSONObject(i);
                    if (img.get("name").toString().equals("Cover") || img.get("name").toString().equals("Front")) {
                        album.put("imgUrl",img);
                    }
                }
            }
        }
        return relatedAlbums;
    }
}
