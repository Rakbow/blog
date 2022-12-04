package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
import com.rakbow.website.data.EntityType;
import com.rakbow.website.data.ImageType;
import com.rakbow.website.data.ProductClass;
import com.rakbow.website.data.album.AlbumFormat;
import com.rakbow.website.data.album.MediaFormat;
import com.rakbow.website.data.album.PublishFormat;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.AlbumUtils;
import com.rakbow.website.util.Image.CommonImageUtils;
import com.rakbow.website.util.Image.QiniuImageHandleUtils;
import com.rakbow.website.util.Image.QiniuImageUtils;
import com.rakbow.website.util.common.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
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
    @Autowired
    private MusicService musicService;
    // @Autowired
    // private ObjectMapper objectMapper;
    @Value("${website.path.img}")
    private String imgPath;
    @Value("${website.path.domain}")
    private String domain;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    //endregion

    //region ------更删改查------

    // REQUIRED: 支持当前事务(外部事务),如果不存在则创建新事务.
    // REQUIRES_NEW: 创建一个新事务,并且暂停当前事务(外部事务).
    // NESTED: 如果当前存在事务(外部事务),则嵌套在该事务中执行(独立的提交和回滚),否则就会REQUIRED一样.

    /**
     * 新增专辑
     *
     * @param album 新增的专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addAlbum(Album album) {
        albumMapper.addAlbum(album);
    }

    /**
     * 获取表中所有数据
     *
     * @return album表中所有专辑，用list封装
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAll() {
        return albumMapper.getAll();
    }

    /**
     * 获取表中数据根据行高
     *
     * @param offset 起始行数
     * @param limit  需要多少数量
     * @return List<Album>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAlbumLimit(int offset, int limit, String where) {
        return albumMapper.getAlbumLimit(offset, limit, where);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Map<String, Object> getAlbumsByFilterList (JSONObject queryParams) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String catalogNo = filter.getJSONObject("catalogNo").getString("value");

        String nameJp = filter.getJSONObject("nameJp").getString("value");

        String sortField = queryParams.getString("sortField");

        int sortOrder = queryParams.getIntValue("sortOrder");

        int seriesId = 0;
        if (filter.getJSONObject("series").getInteger("value") != null) {
            seriesId = filter.getJSONObject("series").getIntValue("value");
        }

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        }else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        List<Integer> products = new ArrayList<>();
        List<Integer> tmpProducts = filter.getJSONObject("products").getList("value", Integer.class);
        if (tmpProducts != null) {
            products.addAll(tmpProducts);
        }

        List<Integer> publishFormat = new ArrayList<>();
        List<Integer> tmpPublishFormat = filter.getJSONObject("publishFormat").getList("value", Integer.class);
        if (tmpPublishFormat != null) {
            publishFormat.addAll(tmpPublishFormat);
        }

        List<Integer> albumFormat = new ArrayList<>();
        List<Integer> tmpAlbumFormat = filter.getJSONObject("albumFormat").getList("value", Integer.class);
        if (tmpAlbumFormat != null) {
            albumFormat.addAll(tmpAlbumFormat);
        }

        List<Integer> mediaFormat = new ArrayList<>();
        List<Integer> tmpMediaFormat = filter.getJSONObject("mediaFormat").getList("value", Integer.class);
        if (tmpMediaFormat != null) {
            mediaFormat.addAll(tmpMediaFormat);
        }

        int first = queryParams.getIntValue("first");

        int row = queryParams.getIntValue("rows");

        List<Album> albums = albumMapper.getAlbumsByFilterList(catalogNo, nameJp, seriesId, products, publishFormat,
                albumFormat, mediaFormat, hasBonus, sortField, sortOrder,  first, row);

        int total = albumMapper.getAlbumRowsByFilterList(catalogNo, nameJp, seriesId, products, publishFormat,
                albumFormat, mediaFormat, hasBonus);

        Map<String, Object> res = new HashMap<>();
        res.put("data", albums);
        res.put("total", total);

        return res;
    }

    /**
     * 根据Id获取专辑
     *
     * @param id 专辑id
     * @return album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public Album getAlbumById(int id) {
        return albumMapper.getAlbumById(id);
    }

    /**
     * 根据Id删除专辑
     *
     * @param id 专辑id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void deleteAlbumById(int id) {
        //删除前先把服务器上对应图片全部删除
        deleteAllAlbumImages(id);

        //删除专辑
        albumMapper.deleteAlbumById(id);
    }

    /**
     * 更新专辑基础信息
     *
     * @param id 专辑id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbum(int id, Album album) {
        albumMapper.updateAlbum(id, album);
    }

    /**
     * 根据条件精准搜索专辑
     *
     * @param queryParam 包含所属系列id，所属产品id数组，媒体格式数组，专辑分类数组，出版形式数组和是否包含特典
     * @return list封装的专辑
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<Album> getAlbumsByFilter(String queryParam) {

        JSONObject param = JSON.parseObject(queryParam);
        String seriesId = (param.get("seriesId") == null) ? null : param.get("seriesId").toString();

        List<Integer> productId = (param.get("productId") == null) ? null : new ArrayList<>();
        if (param.get("productId") != null) {
            List.of(param.get("productId").toString().split(",")).forEach(i -> {
                assert productId != null;
                productId.add(Integer.parseInt(i));
            });
        }

        List<Integer> publishFormat = (param.get("publishFormat") == null) ? null : new ArrayList<>();
        if (param.get("publishFormat") != null) {
            List.of(param.get("publishFormat").toString().split(",")).forEach(i -> {
                assert publishFormat != null;
                publishFormat.add(Integer.parseInt(i));
            });
        }

        List<Integer> albumFormat = (param.get("albumFormat") == null) ? null : new ArrayList<>();
        if (param.get("albumFormat") != null) {
            List.of(param.get("albumFormat").toString().split(",")).forEach(i -> {
                assert albumFormat != null;
                albumFormat.add(Integer.parseInt(i));
            });
        }

        List<Integer> mediaFormat = (param.get("mediaFormat") == null) ? null : new ArrayList<>();
        if (param.get("mediaFormat") != null) {
            List.of(param.get("mediaFormat").toString().split(",")).forEach(i -> {
                assert mediaFormat != null;
                mediaFormat.add(Integer.parseInt(i));
            });
        }

        String hasBonus = (param.get("hasBonus") == null) ? null : param.get("hasBonus").toString();

        return albumMapper.getAlbumsByFilter(seriesId, productId, publishFormat, albumFormat, mediaFormat, hasBonus);
    }

    //endregion

    //region ------暂时用不到------

    public int getAlbumRows() {
        return albumMapper.getAlbumRows();
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

    //region ------数据处理------

    /**
     * 检测数据合法性
     *
     * @param albumJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkAlbumJson(JSONObject albumJson) {
        if (StringUtils.isBlank(albumJson.getString("nameJp"))) {
            return ApiInfo.ALBUM_NAME_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("releaseDate"))) {
            return ApiInfo.ALBUM_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("series"))) {
            return ApiInfo.ALBUM_SERIES_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("products"))
                || StringUtils.equals(albumJson.getString("products"), "[]")) {
            return ApiInfo.ALBUM_PRODUCTS_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("publishFormat"))
                || StringUtils.equals(albumJson.getString("publishFormat"), "[]")) {
            return ApiInfo.ALBUM_PUBLISH_FORMAT_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("albumFormat"))
                || StringUtils.equals(albumJson.getString("albumFormat"), "[]")) {
            return ApiInfo.ALBUM_ALBUM_FORMAT_EMPTY;
        }
        if (StringUtils.isBlank(albumJson.getString("mediaFormat"))
                || StringUtils.equals(albumJson.getString("mediaFormat"), "[]")) {
            return ApiInfo.ALBUM_MEDIA_FORMAT_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送专辑数据
     *
     * @param albumJson
     * @return 处理后的album json格式数据
     * @author rakbow
     */
    public JSONObject handleAlbumJson(JSONObject albumJson) {

        String[] products = CommonUtils.str2SortedArray(albumJson.getString("products"));
        String[] publishFormat = CommonUtils.str2SortedArray(albumJson.getString("publishFormat"));
        String[] albumFormat = CommonUtils.str2SortedArray(albumJson.getString("albumFormat"));
        String[] mediaFormat = CommonUtils.str2SortedArray(albumJson.getString("mediaFormat"));

        //处理时间
        // String releaseDate = CommonUtil.dateToString(albumJson.getDate("releaseDate"));

        albumJson.put("releaseDate", albumJson.getDate("releaseDate"));
        albumJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        albumJson.put("publishFormat", "{\"ids\":[" + StringUtils.join(publishFormat, ",") + "]}");
        albumJson.put("albumFormat", "{\"ids\":[" + StringUtils.join(albumFormat, ",") + "]}");
        albumJson.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return albumJson;
    }

    /**
     * Album转Json对象，以便前端使用，转换量最大的
     *
     * @param album
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject album2Json(Album album) throws IOException {

        JSONObject albumJson = (JSONObject) JSON.toJSON(album);

        List<Music> allMusics = musicService.getMusicsByAlbumId(album.getId());

        //是否包含特典
        boolean hasBonus = (album.getHasBonus() == 1);

        //相关创作人员
        JSONArray artists = JSONArray.parseArray(album.getArtists());

        JSONArray images = JSONArray.parseArray(album.getImages());
        if (!images.isEmpty()) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                image.put("thumbUrl", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 100));

                // image.put("thumbUrl", CommonUtils.getCompressImageUrl(imgPath,
                //         StringUtils.lowerCase(EntityType.ALBUM.getNameEn()),
                //         album.getId(), CommonUtils.getImageFileNameByUrl(image.getString("url"))));
            }
        }

        //发售时间转为string
        albumJson.put("releaseDate", CommonUtils.dateToString(album.getReleaseDate()));

        //出版类型
        List<String> publishFormat = new ArrayList<>();
        JSONObject.parseObject(album.getPublishFormat()).getList("ids", Integer.class)
                .forEach(id -> publishFormat.add(PublishFormat.getNameByIndex(id)));

        //专辑分类
        List<String> albumFormat = new ArrayList<>();
        JSONObject.parseObject(album.getAlbumFormat()).getList("ids", Integer.class)
                .forEach(id -> albumFormat.add(AlbumFormat.getNameByIndex(id)));

        //媒体格式
        List<String> mediaFormat = new ArrayList<>();
        JSONObject.parseObject(album.getMediaFormat()).getList("ids", Integer.class)
                .forEach(id -> mediaFormat.add(MediaFormat.getNameByIndex(id)));

        //所属产品（详细）
        List<String> product = new ArrayList<>();
        JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class)
                .forEach(id -> product.add(productService.getProductById(id).getNameZh() + "(" +
                        ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")"));

        //所属产品
        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", album.getSeries());
        series.put("name", seriesService.selectSeriesById(album.getSeries()).getNameZh());

        //对封面图片进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", QiniuImageHandleUtils.getThumbUrl(CommonConstant.EMPTY_IMAGE_URL, 250));
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 250));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        //对展示图片进行封装
        List<JSONObject> displayImages = new ArrayList<>();
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (image.getIntValue("type") == ImageType.DISPLAY.getIndex()
                        || image.getIntValue("type") == ImageType.COVER.getIndex()) {
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

        JSONArray editDiscList = new JSONArray();
        //可供编辑的editDiscList
        if (JSONObject.parseObject(album.getTrackInfo()) != null
                && !Objects.equals(JSONObject.parseObject(album.getTrackInfo()).toJSONString(), "{}")) {
            JSONArray tmpEditDiscList = JSONObject.parseObject(album.getTrackInfo()).getJSONArray("discList");
            //临时ID，用于前端分辨碟片
            int tmpDiscId = 0;
            for (int i = 0; i < tmpEditDiscList.size(); i++) {
                JSONObject disc = tmpEditDiscList.getJSONObject(i);
                JSONArray trackList = disc.getJSONArray("trackList");
                JSONArray editTrackList = new JSONArray();
                //临时ID，用于前端分辨曲目
                int tmpTracId = 0;
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.getInteger(j);
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        JSONObject track = new JSONObject();
                        track.put("tmpDiscId", tmpDiscId);
                        track.put("tmpTrackId", tmpTracId);
                        track.put("musicId", musicId);
                        track.put("name", music.getName());
                        track.put("length", music.getAudioLength());
                        editTrackList.add(track);
                        tmpTracId++;
                    }
                }

                JSONArray tmpAlbumFormatList = new JSONArray();
                String[] tmpAlbumFormat = StringUtils.split(
                        AlbumFormat.index2NameEnArray(disc.getJSONArray("albumFormat")), ",");
                tmpAlbumFormatList.addAll(Arrays.asList(tmpAlbumFormat));

                JSONArray tmpMediaFormatList = new JSONArray();
                String[] tmpMediaFormat = StringUtils.split(
                        MediaFormat.index2NameEnArrayString(disc.getJSONArray("mediaFormat")), ",");
                tmpMediaFormatList.addAll(Arrays.asList(tmpMediaFormat));

                disc.put("tmpDiscId", tmpDiscId);
                disc.put("trackList", editTrackList);
                disc.put("albumFormat", tmpAlbumFormatList);
                disc.put("mediaFormat", tmpMediaFormatList);
                disc.remove("serial");
                disc.remove("catalogNo");
                disc.remove("discLength");
                editDiscList.add(disc);
                tmpDiscId++;
            }
        }

        //音轨信息
        List<Music> musics = new ArrayList<>();
        JSONObject trackInfo = JSONObject.parseObject(album.getTrackInfo());
        if (trackInfo != null && !Objects.equals(trackInfo.toJSONString(), "{}")) {
            JSONArray discList = trackInfo.getJSONArray("discList");
            JSONArray newDiscList = new JSONArray();
            for (int i = 0; i < discList.size(); i++) {
                JSONObject disc = discList.getJSONObject(i);
                JSONArray trackList = disc.getJSONArray("trackList");
                JSONArray newTrackList = new JSONArray();
                for (int j = 0; j < trackList.size(); j++) {
                    int musicId = trackList.getInteger(j);
                    Music music = DataFinder.findMusicById(musicId, allMusics);
                    if (music != null) {
                        JSONObject track = new JSONObject();
                        track.put("serial", music.getTrackSerial());
                        track.put("musicId", musicId);
                        track.put("name", music.getName());
                        track.put("nameEn", music.getNameEn());
                        track.put("length", music.getAudioLength());
                        newTrackList.add(track);
                        musics.add(music);
                    }
                }
                disc.put("trackList", newTrackList);
                disc.put("albumFormat", AlbumFormat.index2NameEnArray(disc.getJSONArray("albumFormat")));
                disc.put("mediaFormat", MediaFormat.index2NameEnArrayString(disc.getJSONArray("mediaFormat")));
                newDiscList.add(disc);
            }
            trackInfo.put("discList", newDiscList);
        }


        albumJson.put("images", images);
        albumJson.put("editDiscList", editDiscList);
        albumJson.put("hasBonus", hasBonus);
        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("artists", artists);
        albumJson.put("cover", cover);
        albumJson.put("displayImages", displayImages);
        albumJson.put("otherImages", otherImages);
        albumJson.put("trackInfo", trackInfo);
        albumJson.put("series", series);
        albumJson.put("product", product);
        albumJson.put("products", products);
        albumJson.put("addedTime", CommonUtils.timestampToString(album.getAddedTime()));
        albumJson.put("editedTime", CommonUtils.timestampToString(album.getEditedTime()));
        return albumJson;
    }

    /**
     * 列表转换, Album转Json对象，以便前端使用，转换量最大的
     *
     * @param albums
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> album2Json(List<Album> albums) {
        List<JSONObject> albumJsons = new ArrayList<>();

        albums.forEach(album -> {
            try {
                albumJsons.add(album2Json(album));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return albumJsons;
    }

    /**
     * Album转Json对象，供首页展示
     *
     * @param album
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject album2JsonDisplay(Album album) {

        JSONObject json = (JSONObject) JSON.toJSON(album);

        JSONArray images = JSONArray.parseArray(album.getImages());

        if (StringUtils.isBlank(json.getString("catalogNo"))) {
            json.put("catalogNo", "N/A");
        }

        json.put("releaseDate", CommonUtils.dateToString(album.getReleaseDate()));

        //出版类型
        List<String> publishFormat = new ArrayList<>();
        JSONObject.parseObject(album.getPublishFormat()).getList("ids", Integer.class)
                .forEach(id -> publishFormat.add(PublishFormat.getNameByIndex(id)));

        //专辑分类
        List<String> albumFormat = new ArrayList<>();
        JSONObject.parseObject(album.getAlbumFormat()).getList("ids", Integer.class)
                .forEach(id -> albumFormat.add(AlbumFormat.getNameByIndex(id)));

        //媒体格式
        List<String> mediaFormat = new ArrayList<>();
        JSONObject.parseObject(album.getMediaFormat()).getList("ids", Integer.class)
                .forEach(id -> mediaFormat.add(MediaFormat.getNameByIndex(id)));

        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", album.getSeries());
        series.put("name", seriesService.selectSeriesById(album.getSeries()).getNameZh());

        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", CommonConstant.EMPTY_IMAGE_URL);
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
        json.put("albumFormat", albumFormat);
        json.put("mediaFormat", mediaFormat);
        json.put("cover", cover);
        json.put("products", products);
        json.put("series", series);
        json.put("addedTime", CommonUtils.timestampToString(album.getAddedTime()));
        json.put("editedTime", CommonUtils.timestampToString(album.getEditedTime()));
        json.remove("artists");
        json.remove("bonus");
        json.remove("description");
        json.remove("images");
        json.remove("trackInfo");
        return json;
    }

    /**
     * 列表转换, Album转Json对象，供首页展示
     *
     * @param albums
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> album2JsonDisplay(List<Album> albums) {
        List<JSONObject> albumJsons = new ArrayList<>();

        albums.forEach(album -> {
            albumJsons.add(album2JsonDisplay(album));
        });
        return albumJsons;
    }

    /**
     * Album转极简Json
     *
     * @param album
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject album2JsonSimple(Album album) {

        JSONArray images = JSONArray.parseArray(album.getImages());
        //对图片封面进行处理
        JSONObject cover = new JSONObject();
        cover.put("url", CommonConstant.EMPTY_IMAGE_URL);
        cover.put("name", "404");
        if (images.size() != 0) {
            for (int i = 0; i < images.size(); i++) {
                JSONObject image = images.getJSONObject(i);
                if (Objects.equals(image.getString("type"), "1")) {
                    cover.put("url", QiniuImageHandleUtils.getThumbUrl(image.getString("url"), 50));
                    cover.put("name", image.getString("nameEn"));
                }
            }
        }

        //专辑分类
        List<String> albumFormat = new ArrayList<>();
        JSONObject.parseObject(album.getAlbumFormat()).getList("ids", Integer.class)
                .forEach(id -> albumFormat.add(AlbumFormat.getNameByIndex(id)));

        JSONObject albumJson = new JSONObject();
        albumJson.put("id", album.getId());
        albumJson.put("catalogNo", album.getCatalogNo());
        albumJson.put("releaseDate", CommonUtils.dateToString(album.getReleaseDate()));
        albumJson.put("nameJp", album.getNameJp());
        albumJson.put("nameZh", album.getNameZh());
        albumJson.put("seriesId", album.getSeries());
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("cover", cover);
        albumJson.put("addedTime", CommonUtils.timestampToString(album.getAddedTime()));
        albumJson.put("editedTime", CommonUtils.timestampToString(album.getEditedTime()));
        return albumJson;
    }

    /**
     * 列表转换, Album转极简Json
     *
     * @param albums
     * @return JSONObject
     * @author rakbow
     */
    public List<JSONObject> album2JsonSimple(List<Album> albums) {
        List<JSONObject> albumJsons = new ArrayList<>();

        albums.forEach(album -> {
            albumJsons.add(album2JsonSimple(album));
        });
        return albumJsons;
    }

    /**
     * Album转Json，供album-list界面使用
     *
     * @param album
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject album2JsonDisplayList(Album album) {
        JSONObject albumJson = (JSONObject) JSON.toJSON(album);

        //是否包含特典
        boolean hasBonus = (album.getHasBonus() == 1);

        //发售时间转为string
        albumJson.put("releaseDate", CommonUtils.dateToString(album.getReleaseDate()));

        //出版类型
        List<String> publishFormat = new ArrayList<>();
        JSONObject.parseObject(album.getPublishFormat()).getList("ids", Integer.class)
                .forEach(id -> publishFormat.add(PublishFormat.getNameByIndex(id)));

        //专辑分类
        List<String> albumFormat = new ArrayList<>();
        JSONObject.parseObject(album.getAlbumFormat()).getList("ids", Integer.class)
                .forEach(id -> albumFormat.add(AlbumFormat.getNameByIndex(id)));

        //媒体格式
        List<String> mediaFormat = new ArrayList<>();
        JSONObject.parseObject(album.getMediaFormat()).getList("ids", Integer.class)
                .forEach(id -> mediaFormat.add(MediaFormat.getNameByIndex(id)));

        //所属产品（详细）
        List<String> product = new ArrayList<>();
        JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class)
                .forEach(id -> product.add(productService.getProductById(id).getNameZh() + "(" +
                        ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")"));

        //所属产品
        List<JSONObject> products = new ArrayList<>();
        JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class)
                .forEach(id -> {
                    JSONObject jo = new JSONObject();
                    jo.put("id", id);
                    jo.put("name", productService.getProductById(id).getNameZh() + "(" +
                            ProductClass.getNameZhByIndex(productService.getProductById(id).getClassification()) + ")");
                    products.add(jo);
                });

        JSONObject series = new JSONObject();
        series.put("id", album.getSeries());
        series.put("name", seriesService.selectSeriesById(album.getSeries()).getNameZh());

        albumJson.put("hasBonus", hasBonus);
        albumJson.put("publishFormat", publishFormat);
        albumJson.put("albumFormat", albumFormat);
        albumJson.put("mediaFormat", mediaFormat);
        albumJson.put("series", series);
        albumJson.put("product", product);
        albumJson.put("products", products);
        albumJson.put("addedTime", CommonUtils.timestampToString(album.getAddedTime()));
        albumJson.put("editedTime", CommonUtils.timestampToString(album.getEditedTime()));

        albumJson.remove("trackInfo");
        albumJson.remove("artists");
        albumJson.remove("images");
        albumJson.remove("description");
        albumJson.remove("bonus");

        return albumJson;
    }

    /**
     * 列表转换, Album转Json，供album-list界面使用
     *
     * @param albums
     * @return JSONObject
     * @author rakbow
     */
    public List<JSONObject> album2JsonDisplayList(List<Album> albums) {
        List<JSONObject> albumJsons = new ArrayList<>();

        albums.forEach(album -> {
            albumJsons.add(album2JsonDisplayList(album));
        });
        return albumJsons;
    }

    /**
     * json对象转Album，以便保存到数据库
     *
     * @param albumJson
     * @return Album
     * @author rakbow
     */
    public Album json2Album(JSONObject albumJson) throws ParseException {
        // albumJson.put("releaseDate", CommonUtil.stringToDate(albumJson.getString("releaseDate"), "yyyy/MM/dd"));
        return albumJson.toJavaObject(Album.class);
    }

    /**
     * Album转json，用于存储在elasticsearch服务器中，用于搜索
     *
     * @param album
     * @return JSONObject
     * @author rakbow
     */
    public JSONObject album2StorageJson(Album album) {
        JSONObject albumJson = new JSONObject();

        albumJson.put("id", album.getId());
        albumJson.put("catalogNo", album.getCatalogNo());
        albumJson.put("releaseDate", album.getReleaseDate());
        albumJson.put("nameJp", album.getNameJp());
        albumJson.put("nameEn", album.getNameEn());
        albumJson.put("nameZh", album.getNameZh());
        albumJson.put("coverUrl", AlbumUtils.getAlbumCoverUrl(album));

        return albumJson;
    }

    /**
     * 列表转换, Album转json，用于存储在elasticsearch服务器中，用于搜索
     *
     * @param albums
     * @return List<JSONObject>
     * @author rakbow
     */
    public List<JSONObject> album2StorageJson(List<Album> albums) {
        List<JSONObject> albumJsons = new ArrayList<>();

        albums.forEach(album -> {
            albumJsons.add(album2StorageJson(album));
        });
        return albumJsons;
    }

    //endregion

    /**
     * 获取相关联专辑
     *
     * @param id 专辑id
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedAlbums(int id) {

        Album album = getAlbumById(id);
        String seriesId = Integer.toString(album.getSeries());

        List<JSONObject> relatedAlbums = new ArrayList<>();

        //该专辑包含的作品id
        List<Integer> productIds = JSONObject.parseObject(album.getProducts()).getList("ids", Integer.class);

        List<Album> result = new ArrayList<>();

        JSONObject queryParam = new JSONObject();
        queryParam.put("seriesId", seriesId);
        queryParam.put("productId", StringUtils.join(productIds.toArray(new Integer[0]), ","));
        List<Album> queryResult = getAlbumsByFilter(queryParam.toJSONString());
        for (int i = 0; i < queryResult.size(); i++) {
            if (queryResult.get(i).getId() == id) {
                queryResult.remove(i);
                break;
            }
        }

        JSONObject tmpQueryParam = new JSONObject();

        if (queryResult.size() > 5) {
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {
            List<Album> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                tmpQueryParam.put("seriesId", seriesId);
                tmpQueryParam.put("productId", productIds.get(1));
                List<Album> tmpQueryResult = getAlbumsByFilter(tmpQueryParam.toJSONString());
                for (int i = 0; i < tmpQueryResult.size(); i++) {
                    if (tmpQueryResult.get(i).getId() == id) {
                        tmpQueryResult.remove(i);
                        break;
                    }
                }
                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Album> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmpQueryParam.put("seriesId", seriesId);
                tmpQueryParam.put("productId", Integer.toString(productId));
                tmp.addAll(getAlbumsByFilter(tmpQueryParam.toJSONString()));
            }
            result = CommonUtils.removeDuplicateList(tmp);
            for (int i = 0; i < result.size(); i++) {
                if (result.get(i).getId() == id) {
                    result.remove(i);
                    break;
                }
            }
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }
        result = CommonUtils.removeDuplicateList(result);
        result.forEach(i -> relatedAlbums.add(album2JsonSimple(i)));
        return relatedAlbums;
    }

    /**
     * 获取最新收录的专辑
     *
     * @param limit 获取条数
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustAddedAlbums(int limit) {
        List<JSONObject> justAddedAlbums = new ArrayList<>();

        albumMapper.getAlbumOrderByAddedTime(limit)
                .forEach(i -> justAddedAlbums.add(album2JsonDisplay(i)));

        return justAddedAlbums;
    }

    /**
     * 获取最近编辑的专辑
     *
     * @param limit 获取条数
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getJustEditedAlbums(int limit) {
        List<JSONObject> editedAlbums = new ArrayList<>();

        albumMapper.getAlbumOrderByEditedTime(limit)
                .forEach(i -> editedAlbums.add(album2JsonDisplay(i)));

        return editedAlbums;
    }

    /**
     * 获取浏览量最高的专辑
     *
     * @param limit 获取条数
     * @return list封装的Album
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getPopularAlbums(int limit) {
        List<JSONObject> popularAlbums = new ArrayList<>();

        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.ALBUM.getId(), limit);

        visits.forEach(visit -> {
            JSONObject album = album2JsonDisplay(getAlbumById(visit.getEntityId()));
            album.put("visitNum", visit.getVisitNum());
            popularAlbums.add(album);
        });
        return popularAlbums;
    }

    //region ------更新专辑数据------

    /**
     * 新增专辑图片
     *
     * @param id     专辑id
     * @param images 新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos 新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void addAlbumImages(int id, MultipartFile[] images, JSONArray originalImagesJson, JSONArray imageInfos) throws IOException {

        //最终保存到数据库的json信息
        JSONArray finalImageJson = new JSONArray();

        //新增图片信息json
        JSONArray addImageJson = new JSONArray();

        //创建存储链接前缀
        String filePath = EntityType.ALBUM.getNameEn().toLowerCase() + "/" + id + "/";

        for (int i = 0; i < images.length; i++) {
            //上传图片
            ActionResult ar = qiniuImageUtils.uploadImageToQiniu(images[i], filePath);
            if (ar.state) {
                JSONObject jo = new JSONObject();
                jo.put("url", ar.data.toString());
                jo.put("nameEn", imageInfos.getJSONObject(i).getString("nameEn"));
                jo.put("nameZh", imageInfos.getJSONObject(i).getString("nameZh"));
                jo.put("type", imageInfos.getJSONObject(i).getString("type"));
                jo.put("uploadTime", CommonUtils.getCurrentTime());
                if (imageInfos.getJSONObject(i).getString("description") == null) {
                    jo.put("description", "");
                }else {
                    jo.put("description", imageInfos.getJSONObject(i).getString("description"));
                }
                addImageJson.add(jo);
            }
        }

        //汇总
        finalImageJson.addAll(originalImagesJson);
        finalImageJson.addAll(addImageJson);

        List<Music> musics = musicService.getMusicsByAlbumId(id);

        //若涉及封面类型图片，则更新相应的音频封面
        String coverUrl = CommonImageUtils.getCoverUrl(addImageJson);
        if (!StringUtils.isBlank(coverUrl)) {
            for (Music music : musics) {
                musicService.updateMusicCoverUrl(music.getId(), coverUrl);
            }
        }

        albumMapper.updateAlbumImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新专辑图片
     *
     * @param id    专辑id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String updateAlbumImages(int id, String images) {
        albumMapper.updateAlbumImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());
    }

    /**
     * 删除专辑图片
     *
     * @param id           专辑id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAlbumImages(int id, JSONArray deleteImages) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getAlbumById(id).getImages());

        //从七牛云上删除
        //删除结果
        List<String> deleteResult = new ArrayList<>();
        //若删除的图片只有一张，调用单张删除方法
        if (deleteImages.size() == 1) {
            ActionResult ar = qiniuImageUtils.deleteImageFromQiniu(deleteImages.getJSONObject(0).getString("url"));
            if (!ar.state) {
                return ar.message;
            }
            deleteResult.add(deleteImages.getJSONObject(0).getString("url"));
        }else {
            String[] fullImageUrlList = new String[deleteImages.size()];
            for (int i = 0; i < deleteImages.size(); i++) {
                fullImageUrlList[i] = deleteImages.getJSONObject(i).getString("url");
            }
            ActionResult ar = qiniuImageUtils.deleteImagesFromQiniu(fullImageUrlList);
            deleteResult = (List<String>) ar.data;
        }

        //根据删除结果循环删除图片信息json数组
        // 迭代器

        for (String s : deleteResult) {
            Iterator<Object> iterator = images.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (StringUtils.equals(itJson.getString("url"), s)) {
                    // 删除数组元素
                    iterator.remove();
                }
            }
        }
        albumMapper.updateAlbumImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());
    }

    /**
     * 删除该专辑所有图片
     *
     * @param id 专辑id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String deleteAllAlbumImages(int id) {
        Album album = getAlbumById(id);
        JSONArray images = JSON.parseArray(album.getImages());
        String[] deleteImageKeyList = new String[images.size()];
        //图片文件名
        String deleteImageUrl = "";
        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            deleteImageUrl = image.getString("url");
            //删除七牛服务器上对应图片文件
            deleteImageKeyList[i] = deleteImageUrl;
        }
        qiniuImageUtils.deleteImagesFromQiniu(deleteImageKeyList);
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());
    }

    /**
     * 更新专辑Artists
     *
     * @param id      专辑id
     * @param artists 专辑的创作相关信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumArtists(int id, String artists) {
        albumMapper.updateAlbumArtists(id, artists, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新音轨信息
     *
     * @param id        专辑id
     * @param _discList 专辑的音轨信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumTrackInfo(int id, String _discList) throws Exception {

        //获取该专辑对应的音乐合集
        List<Music> musics = musicService.getMusicsByAlbumId(id);

        JSONObject trackInfo = new JSONObject();

        JSONArray discList = new JSONArray();

        //计算总时长的数组
        List<String> times = new ArrayList<>();

        int totalTrack = 0;

        int discSerial = 1;

        String trackSerial = "";

        for (Object _disc : JSON.parseArray(_discList)) {
            JSONObject disc = new JSONObject();
            JSONArray track_list = new JSONArray();
            List<String> _times = new ArrayList<>();
            int i = 1;
            for (Object _track : JSON.parseArray(JSON.parseObject(_disc.toString()).getString("trackList"))) {
                JSONObject track = JSON.parseObject(_track.toString());
                if (i < 10) {
                    trackSerial = "0" + i;
                } else {
                    trackSerial = Integer.toString(i);
                }
                track.put("serial", trackSerial);

                //往music表中添加新数据
                //若musicId为0则代表该条数据为新增数据
                if (track.getInteger("musicId") == 0) {
                    Music music = new Music();
                    music.setName(track.getString("name"));
                    music.setAlbumId(id);
                    music.setDiscSerial(discSerial);
                    music.setTrackSerial(trackSerial);
                    music.setAudioLength(track.getString("length"));
                    musicService.addMusic(music);
                    track_list.add(music.getId());
                } else {//若musicId不为0则代表之前已经添加进music表中，需要进一步更新
                    Music currentMusic = DataFinder.findMusicById(track.getInteger("musicId"), musics);
                    currentMusic.setName(track.getString("name"));
                    currentMusic.setAudioLength(track.getString("length"));
                    currentMusic.setDiscSerial(discSerial);
                    currentMusic.setTrackSerial(trackSerial);
                    //更新对应music
                    musicService.updateMusic(currentMusic.getId(), currentMusic);
                    track_list.add(currentMusic.getId());
                    musics.remove(currentMusic);
                }
                _times.add(track.get("length").toString());
                i++;
            }
            times.addAll(_times);
            totalTrack += track_list.size();

            disc.put("serial", discSerial);
            disc.put("mediaFormat", MediaFormat.nameEn2IndexArray(
                    JSON.parseObject(_disc.toString()).getJSONArray("mediaFormat")));
            disc.put("albumFormat", AlbumFormat.nameEn2IndexArray(
                    JSON.parseObject(_disc.toString()).getJSONArray("albumFormat")));
            disc.put("discLength", CommonUtils.countTotalTime(_times));
            disc.put("trackList", track_list);

            discSerial++;
            discList.add(disc);
        }
        trackInfo.put("discList", discList);
        trackInfo.put("totalTracks", totalTrack);
        trackInfo.put("totalLength", CommonUtils.countTotalTime(times));
        albumMapper.updateAlbumTrackInfo(id, trackInfo.toJSONString(), new Timestamp(System.currentTimeMillis()));

        //删除对应music
        if (musics.size() != 0) {
            for (Music music : musics) {
                musicService.deleteMusicById(music.getId());
            }
        }
    }

    /**
     * 更新专辑描述
     *
     * @param id          专辑id
     * @param description 专辑的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumDescription(int id, String description) {
        albumMapper.updateAlbumDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新特典信息
     *
     * @param id    专辑id
     * @param bonus 专辑的特典信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public void updateAlbumBonus(int id, String bonus) {
        albumMapper.updateAlbumBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
    }

    //endregion

    //region ------其他------

    /**
     * 根据id获取专辑封面图，若为空返回404图片
     *
     * @param id 专辑id
     * @return 图片url
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String getAlbumCoverUrl(int id) {
        //先赋值为404图片
        String coverUrl = CommonConstant.EMPTY_IMAGE_URL;

        Album album = getAlbumById(id);
        JSONArray images = JSON.parseArray(album.getImages());

        for (int i = 0; i < images.size(); i++) {
            JSONObject image = images.getJSONObject(i);
            //若图片中包含封面类型图片
            if (image.getIntValue("type") == ImageType.COVER.getIndex()) {
                coverUrl = image.getString("url");
            }
        }
        return coverUrl;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public List<JSONObject> getRelatedAlbumsByProductId (int productId) {
        List<JSONObject> relatedAlbums = new ArrayList<>();

        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Album> albums = albumMapper.getAlbumsByFilter(null, products, null,
                null, null, null);

        relatedAlbums = album2JsonSimple(albums);

        return relatedAlbums;
    }

    //endregion

    //region ------废弃------
    /**
     * 删除专辑图片
     *
     * @param id           专辑id
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Deprecated
    @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRED)
    public String DeprecatedDeleteAlbumImages(int id, JSONArray deleteImages) {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(getAlbumById(id).getImages());

        //图片文件名
        String fileName = "";

        //循环删除
        for (int i = 0; i < deleteImages.size(); i++) {
            JSONObject image = deleteImages.getJSONObject(i);
            // 迭代器
            Iterator<Object> iterator = images.iterator();
            while (iterator.hasNext()) {
                JSONObject itJson = (JSONObject) iterator.next();
                if (image.getString("url").equals(itJson.getString("url"))) {
                    // 删除数组元素
                    String deleteImageUrl = itJson.getString("url");
                    fileName = deleteImageUrl.substring(
                            deleteImageUrl.lastIndexOf("/") + 1, deleteImageUrl.lastIndexOf("."));
                    iterator.remove();
                }
                //删除服务器上对应图片文件
                Path albumImgPath = Paths.get(imgPath + "/album/" + id);
                CommonUtils.deleteFile(albumImgPath, fileName);
            }
        }
        albumMapper.updateAlbumImages(id, images.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.ALBUM.getNameZh());
    }
    //endregion

}
