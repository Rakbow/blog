package com.rakbow.database.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.database.dao.AlbumMapper;
import com.rakbow.database.data.ProductClass;
import com.rakbow.database.data.album.AlbumFormat;
import com.rakbow.database.data.album.MediaFormat;
import com.rakbow.database.data.album.PublishFormat;
import com.rakbow.database.entity.Album;
import com.rakbow.database.service.util.common.ApiResultInfo;
import com.rakbow.database.service.util.common.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @Project_name: database
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
    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String insertAlbum(Album album) {
        albumMapper.insertAlbum(album);
        return ApiResultInfo.INSERT_DATA_SUCCESS;
    }

    //获取表中所有数据
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAll() {
        return albumMapper.getAll();
    }

    //根据具体搜索条件查询
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Album findAlbumById(int id) {
        return albumMapper.selectAlbumById(id);
    }

    //根据Id删除专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public int deleteAlbumById(int id) {
        return albumMapper.deleteAlbumById(id);
    }

    //更新专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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

        JSONArray artists = JSONArray.parseArray(album.getArtists());

        JSONArray imgUrl = JSONArray.parseArray(album.getImgUrl());

        albumJson.put("publishFormat", publishFormat);
        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("artists",artists);
        albumJson.put("imgUrl",imgUrl);
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
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumImgUrl(int id, String imgUrl) {
        albumMapper.updateAlbumImgUrl(id, imgUrl);
    }

    //更新专辑Staff
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumArtists(int id, String credits) {
        albumMapper.updateAlbumArtists(id, credits);
    }

    //获取相关专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
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
                tmp.put("url", "/db/img/404.jpg");
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
