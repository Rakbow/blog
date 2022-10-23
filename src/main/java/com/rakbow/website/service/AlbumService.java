package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mysql.cj.xdevapi.JsonArray;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.ProductClass;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.album.MediaFormat;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.ApiInfo;
import com.rakbow.website.util.common.CommonUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-07-25 1:42
 * @Description:
 */
@Service
public class AlbumService {


    //region ------引入实例------
    @Autowired
    private AlbumMapper albumMapper;
    @Autowired
    private SeriesService seriesService;
    @Autowired
    private ProductService productService;
    @Autowired
    private VisitService visitService;
    @Value("${website.path.img}")
    private String imgPath;
    //endregion

    //region ------基础更删改查------

    //新增专辑
    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public int insertAlbum(Album album) {
        return albumMapper.insertAlbum(album);
    }

    //获取表中所有数据
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAll() {
        return albumMapper.getAll();
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

    //endregion

    //region ------暂时用不到------

    public int findAlbumRows() {
        return albumMapper.selectAlbumRows();
    }

    //获取所有字段信息
    public List<String> getAlbumFields() {
        List<String> AlbumFields = null;
        Field[] f = Album.class.getClass().getFields();
        for (int i = 0; i < f.length; i++) {
            AlbumFields.add(f[i].getName());
        }
        return AlbumFields;
    }

    //endregion

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

    //region ------数据处理------

    //Album转Json对象，以便前端使用
    public JSONObject album2Json(Album album) {

        JSONObject albumJson = (JSONObject) JSON.toJSON(album);

        JSONArray artists = JSONArray.parseArray(album.getArtists());

        JSONArray images = JSONArray.parseArray(album.getImages());

        JSONObject trackInfo = JSONObject.parseObject(album.getTrackInfo());

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

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", "http://localhost:8080/img/404.jpg");
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", image.getString("url"));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        //对展示图片进行封装
        List<JSONObject> displayImages = new ArrayList<>();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1") || Objects.equals(image.getString("type"), "0")) {
                    displayImages.add(image);
                }
            }
        }

        //对其他图片进行封装
        List<JSONObject> otherImages = new ArrayList<>();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "2")) {
                    otherImages.add(image);
                }
            }
        }


        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("artists", artists);
        albumJson.put("images", images);
        albumJson.put("cover", cover);
        albumJson.put("displayImages", displayImages);
        albumJson.put("otherImages", otherImages);
        albumJson.put("trackInfo", trackInfo);
        albumJson.put("series", seriesService.selectSeriesById(album.getSeriesId()).getNameZh());
        albumJson.put("product", product);
        albumJson.put("products", productMap);
        albumJson.put("addedTime", CommonUtil.dateToString(album.getAddedTime(), "yyyy/MM/dd hh:mm:ss"));
        albumJson.put("editedTime", CommonUtil.dateToString(album.getEditedTime(), "yyyy/MM/dd hh:mm:ss"));
        return albumJson;
    }

    //Album转Json对象，供首页展示
    public JSONObject album2JsonDisplay(Album album) {

        JSONObject json = (JSONObject) JSON.toJSON(album);

        JSONArray images = JSONArray.parseArray(album.getImages());

        if(StringUtils.isBlank(json.getString("catalogNo"))){
            json.put("catalogNo", "N/A");
        }

        json.put("releaseDate", CommonUtil.dateToString(album.getReleaseDate(), "yyyy/MM/dd"));

        List<String> publishFormat = new ArrayList();
        Arrays.stream(album.getPublishFormat().split(","))
                .forEach(i -> publishFormat.add(PublishFormat.getNameByIndex(Integer.parseInt(i))));

        List<String> albumFormat = new ArrayList();
        Arrays.stream(album.getAlbumFormat().split(","))
                .forEach(i -> albumFormat.add(AlbumFormat.getNameByIndex(Integer.parseInt(i))));

        List<String> mediaFormat = new ArrayList();
        Arrays.stream(album.getMediaFormat().split(","))
                .forEach(i -> mediaFormat.add(MediaFormat.getNameByIndex(Integer.parseInt(i))));

        List<JSONObject> products = new ArrayList<>();
        Arrays.stream(album.getProductId().split(","))
                .forEach(i -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", Integer.parseInt(i));
                    jo.put("name", productService.selectProductById(Integer.parseInt(i)).getNameZh());
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", album.getSeriesId());
        series.put("name", seriesService.selectSeriesById(album.getSeriesId()).getNameZh());

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", "http://localhost:8080/img/404.jpg");
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", image.getString("url"));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }


        json.put("publishFormat", publishFormat);
        json.put("publishFormat", publishFormat);
        json.put("albumFormat", albumFormat);
        json.put("mediaFormat", mediaFormat);
        json.put("cover", cover);
        json.put("products", products);
        json.put("series", series);
        json.put("addedTime", CommonUtil.dateToString(album.getAddedTime(), "yyyy/MM/dd hh:mm:ss"));
        json.put("editedTime", CommonUtil.dateToString(album.getEditedTime(), "yyyy/MM/dd hh:mm:ss"));
        json.remove("artists");
        json.remove("bonus");
        json.remove("description");
        json.remove("images");
        json.remove("productId");
        json.remove("seriesId");
        json.remove("trackInfo");
        return json;
    }

    //json对象转Album，以便保存到数据库
    public Album json2Album(JSONObject albumJson) throws ParseException {
        albumJson.put("releaseDate", CommonUtil.stringToDate(albumJson.getString("releaseDate"), "yyyy/MM/dd"));
        Album album = albumJson.toJavaObject(Album.class);
        album.setEditedTime(new Timestamp(System.currentTimeMillis()));
        return album;
    }

    //endregion

    //根据产品id获取所有该产品的专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAlbumsByProductId(int productId) {
        List<Album> albums = new ArrayList<>();
        albumMapper.selectAlbumByFilter(null, null, null, Integer.toString(productId))
                .stream().forEach(i -> {
                    Arrays.stream(i.getProductId().split(",")).forEach(j -> {
                        if (j.equals(productId)) {
                            albums.add(i);
                        }
                    });
                });
        return albums;
    }

    //获取相关专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedAlbums(int id) {
        String[] productIds = findAlbumById(id).getProductId().split(",");
        List<JSONObject> relatedAlbums = new ArrayList<>();
        if (productIds.length == 1) {
            List<Album> tmp = selectAlbumByFilter("{\"productId\":\"" + productIds[0] + "\"}");
            if (tmp.size() < 4) {
                tmp.stream().forEach(i -> relatedAlbums.add(album2Json(i)));
            } else {
                for (int i = 0; i < 4; i++) {
                    relatedAlbums.add(album2Json(tmp.get(i)));
                }
            }
        } else if (productIds.length > 1 && productIds.length <= 4) {
            for (int i = 0; i < productIds.length; i++) {
                relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productIds[i] + "\"}").get(0)));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                relatedAlbums.add(album2Json(selectAlbumByFilter("{\"productId\":\"" + productIds[i] + "\"}").get(0)));
            }
        }


        //处理空图片
        for (JSONObject album : relatedAlbums) {

            JSONArray images = JSONArray.parseArray(album.get("images").toString());
            JSONObject cover = new JSONObject();

            if (images.isEmpty()) {
                cover.put("url", "/img/404.jpg");
                album.put("cover", cover);
            } else {
                for (int i = 0; i < images.size(); i++) {
                    JSONObject img = images.getJSONObject(i);
                    if (Integer.parseInt(img.getString("type")) == 1) {
                        cover.put("url", img.getString("url"));
                        album.put("cover", cover);
                    }
                }
            }
        }
        return relatedAlbums;
    }

    //获取最新加入的专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustAddedAlbums(int limit){
        List<JSONObject> justAddedAlbums = new ArrayList<>();

        albumMapper.selectAlbumOrderByAddedTime(limit)
                .forEach(i -> justAddedAlbums.add(album2JsonDisplay(i)));

        return justAddedAlbums;
    }

    //获取最新编辑的专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustEditedAlbums(int limit){
        List<JSONObject> editedAlbums = new ArrayList<>();

        albumMapper.selectAlbumOrderByEditedTime(limit)
                .forEach(i -> editedAlbums.add(album2JsonDisplay(i)));

        return editedAlbums;
    }

    //获取最新编辑的专辑
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getPopularAlbums(int limit){
        List<JSONObject> popularAlbums = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.ALBUM.getId(), limit);

        visits.forEach(visit -> {
            JSONObject album = album2JsonDisplay(findAlbumById(visit.getEntityId()));
            album.put("visitNum", visit.getVisitNum());
            popularAlbums.add(album);
        });
        return popularAlbums;
    }

    //region ------更新专辑数据------

    //新增专辑图片
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void insertAlbumImages(int id, String images) {
        albumMapper.insertAlbumImages(id, images, new Timestamp(System.currentTimeMillis()));
    }

    //更改专辑图片
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String updateAlbumImages(int id, String image) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(findAlbumById(id).getImages());
        JSONObject editImage = JSONObject.parseObject(image);
        // 迭代器
        Iterator<Object> iterator = images.iterator();
        while (iterator.hasNext()) {
            JSONObject itJson = (JSONObject) iterator.next();
            if (editImage.getString("url").equals(itJson.getString("url"))) {
                // 替换数组元素
                itJson.put("nameEn", editImage.getString("nameEn"));
                itJson.put("nameZh", editImage.getString("nameZh"));
                itJson.put("type", editImage.getString("type"));
                itJson.put("description", editImage.getString("description"));
            }
        }
        albumMapper.updateAlbumImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_ALBUM_IMAGES_SUCCESS;
    }

    //删除专辑图片
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAlbumImages(int id, String imageUrl) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(findAlbumById(id).getImages());
        // 迭代器
        String fileName = "";
        Iterator<Object> iterator = images.iterator();
        while (iterator.hasNext()) {
            JSONObject itJson = (JSONObject) iterator.next();
            if (imageUrl.equals(itJson.getString("url"))) {
                // 删除数组元素
                fileName = itJson.getString("nameEn").replaceAll(" ", "");
                iterator.remove();
            }
        }
        //删除服务器上对应图片文件
        Path albumImgPath = Paths.get(imgPath + "/album/" + id);
        CommonUtil.deleteFile(albumImgPath, fileName);
        albumMapper.updateAlbumImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return ApiInfo.DELETE_ALBUM_IMAGES_SUCCESS;
    }

    //更新专辑Artists
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumArtists(int id, String artists) {
        albumMapper.updateAlbumArtists(id, artists, new Timestamp(System.currentTimeMillis()));
    }

    //更新音轨信息
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumTrackInfo(int id, String discList) {

        JSONObject trackInfo = new JSONObject();

        JSONArray disc_list = new JSONArray();

        List<String> times = new ArrayList<>();

        int totalTrack = 0;

        int serial = 1;

        for (Object _disc : JSON.parseArray(discList)) {
            JSONObject disc = new JSONObject();
            JSONArray track_list = new JSONArray();
            List<String> _times = new ArrayList<>();
            int i = 1;
            for (Object _track : JSON.parseArray(JSON.parseObject(_disc.toString()).getString("track_list"))) {
                JSONObject track = JSON.parseObject(_track.toString());
                if (i < 10) {
                    track.put("serial", "0" + i);
                } else {
                    track.put("serial", Integer.toString(i));
                }
                track_list.add(track);
                _times.add(track.get("length").toString());
                i++;
            }
            times.addAll(_times);
            totalTrack += track_list.size();

            disc.put("serial", serial);
            disc.put("media_format", JSON.parseObject(_disc.toString()).get("media_format").toString());
            disc.put("album_format", JSON.parseObject(_disc.toString()).get("album_format").toString());
            if (!StringUtils.isBlank(albumMapper.selectAlbumById(id).getCatalogNo())) {
                disc.put("catalog_no", albumMapper.selectAlbumById(id).getCatalogNo() + '-' + serial);
            } else {
                disc.put("catalog_no", "");
            }
            disc.put("disc_length", CommonUtil.countTotalTime(_times));
            disc.put("track_list", track_list);

            serial++;
            disc_list.add(disc);
        }
        trackInfo.put("disc_list", disc_list);
        trackInfo.put("total_tracks", totalTrack);
        trackInfo.put("total_length", CommonUtil.countTotalTime(times));
        albumMapper.updateAlbumTrackInfo(id, trackInfo.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    //更新描述信息
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumDescription(int id, String description) {
        albumMapper.updateAlbumDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    //更新特典信息
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumBonus(int id, String bonus) {
        albumMapper.updateAlbumBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion
}
