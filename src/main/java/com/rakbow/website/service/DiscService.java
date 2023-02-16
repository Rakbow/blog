package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.DiscMapper;
import com.rakbow.website.data.ApiInfo;
import com.rakbow.website.data.SearchResult;
import com.rakbow.website.data.emun.common.EntityType;
import com.rakbow.website.data.vo.album.AlbumVOAlpha;
import com.rakbow.website.data.vo.disc.DiscVOAlpha;
import com.rakbow.website.data.vo.disc.DiscVOBeta;
import com.rakbow.website.entity.Book;
import com.rakbow.website.entity.Disc;
import com.rakbow.website.entity.User;
import com.rakbow.website.entity.Visit;
import com.rakbow.website.util.common.CommonUtils;
import com.rakbow.website.util.common.DataSorter;
import com.rakbow.website.util.convertMapper.DiscVOMapper;
import com.rakbow.website.util.file.QiniuFileUtils;
import com.rakbow.website.util.file.QiniuImageUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-12-14 23:24
 * @Description: disc业务层
 */
@Service
public class DiscService {

    //region ------注入依赖------

    @Autowired
    private DiscMapper discMapper;
    @Autowired
    private QiniuImageUtils qiniuImageUtils;
    @Autowired
    private QiniuFileUtils qiniuFileUtils;
    @Autowired
    private VisitService visitService;

    private final DiscVOMapper discVOMapper = DiscVOMapper.INSTANCES;

    //endregion

    //region ------更删改查------

    /**
     * 新增碟片
     *
     * @param disc 新增的碟片
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void addDisc(Disc disc) {
        discMapper.addDisc(disc);
    }

    /**
     * 根据Id获取碟片
     *
     * @param id 碟片id
     * @return disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Disc getDisc(int id) {
        return discMapper.getDisc(id, true);
    }

    /**
     * 根据Id获取需要判断权限
     *
     * @param id id
     * @return Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public Disc getDiscWithAuth(int id, int userAuthority) {
        if(userAuthority > 2) {
            return discMapper.getDisc(id, true);
        }
        return discMapper.getDisc(id, false);
    }

    /**
     * 根据Id删除碟片
     *
     * @param disc 碟片
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void deleteDisc(Disc disc) {
        //删除前先把服务器上对应图片全部删除
        qiniuFileUtils.commonDeleteAllFiles(JSON.parseArray(disc.getImages()));

        discMapper.deleteDisc(disc.getId());
    }

    /**
     * 更新碟片基础信息
     *
     * @param id 碟片id
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateDisc(int id, Disc disc) {
        discMapper.updateDisc(id, disc);
        return String.format(ApiInfo.UPDATE_DATA_SUCCESS, EntityType.DISC.getNameZh());
    }

    //endregion

    //region ------数据处理------

    /**
     * json对象转Disc，以便保存到数据库
     *
     * @param discJson discJson
     * @return Disc
     * @author rakbow
     */
    public Disc json2Disc(JSONObject discJson) {
        return JSON.to(Disc.class, discJson);
    }

    /**
     * 检测数据合法性
     *
     * @param discJson discJson
     * @return string类型错误消息，若为空则数据检测通过
     * @author rakbow
     */
    public String checkDiscJson(JSONObject discJson) {
        if (StringUtils.isBlank(discJson.getString("name"))) {
            return ApiInfo.DISC_NAME_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("releaseDate"))) {
            return ApiInfo.DISC_RELEASE_DATE_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("franchises"))
                || StringUtils.equals(discJson.getString("franchises"), "[]")) {
            return ApiInfo.FRANCHISES_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("products"))
                || StringUtils.equals(discJson.getString("products"), "[]")) {
            return ApiInfo.PRODUCTS_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("mediaFormat"))
                || StringUtils.equals(discJson.getString("mediaFormat"), "[]")) {
            return ApiInfo.DISC_MEDIA_FORMAT_EMPTY;
        }
        if (StringUtils.isBlank(discJson.getString("region"))) {
            return ApiInfo.DISC_REGION_EMPTY;
        }
        return "";
    }

    /**
     * 处理前端传送碟片数据
     *
     * @param discJson discJson
     * @return 处理后的disc json格式数据
     * @author rakbow
     */
    public JSONObject handleDiscJson(JSONObject discJson) {

        String[] products = CommonUtils.str2SortedArray(discJson.getString("products"));
        String[] franchises = CommonUtils.str2SortedArray(discJson.getString("franchises"));
        String[] mediaFormat = CommonUtils.str2SortedArray(discJson.getString("mediaFormat"));

        discJson.put("releaseDate", discJson.getDate("releaseDate"));
        discJson.put("products", "{\"ids\":[" + StringUtils.join(products, ",") + "]}");
        discJson.put("franchises", "{\"ids\":[" + StringUtils.join(franchises, ",") + "]}");
        discJson.put("mediaFormat", "{\"ids\":[" + StringUtils.join(mediaFormat, ",") + "]}");

        return discJson;
    }

    //endregion

    //region ------图片操作------

    /**
     * 新增碟片图片
     *
     * @param id                 碟片id
     * @param images             新增图片文件数组
     * @param originalImagesJson 数据库中现存的图片json数据
     * @param imageInfos         新增图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String addDiscImages(int id, MultipartFile[] images, JSONArray originalImagesJson, JSONArray imageInfos, User user) throws IOException {

        JSONArray finalImageJson = qiniuImageUtils.commonAddImages
                (id, EntityType.DISC, images, originalImagesJson, imageInfos, user);

        discMapper.updateDiscImages(id, finalImageJson.toJSONString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.INSERT_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    /**
     * 更新碟片图片
     *
     * @param id     碟片id
     * @param images 需要更新的图片json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateDiscImages(int id, String images) {
        discMapper.updateDiscImages(id, images, new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.UPDATE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    /**
     * 删除碟片图片
     *
     * @param disc           碟片
     * @param deleteImages 需要删除的图片jsonArray
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String deleteDiscImages(Disc disc, JSONArray deleteImages) throws Exception {
        //获取原始图片json数组
        JSONArray images = JSONArray.parseArray(disc.getImages());

        JSONArray finalImageJson = qiniuFileUtils.commonDeleteFiles(images, deleteImages);

        discMapper.updateDiscImages(disc.getId(), finalImageJson.toString(), new Timestamp(System.currentTimeMillis()));
        return String.format(ApiInfo.DELETE_IMAGES_SUCCESS, EntityType.DISC.getNameZh());
    }

    //endregion

    //region ------更新数据------

    /**
     * 更新碟片规格信息
     *
     * @param id          碟片id
     * @param spec 碟片的规格信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateDiscSpec(int id, String spec) {
        discMapper.updateDiscSpec(id, spec, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_DISC_SPEC_SUCCESS;
    }

    /**
     * 更新碟片描述
     *
     * @param id          碟片id
     * @param description 碟片的描述json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public void updateDiscDescription(int id, String description) {
        discMapper.updateDiscDescription(id, description, new Timestamp(System.currentTimeMillis()));
    }

    /**
     * 更新特典信息
     *
     * @param id    碟片id
     * @param bonus 碟片的特典信息json数据
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class)
    public String updateDiscBonus(int id, String bonus) {
        discMapper.updateDiscBonus(id, bonus, new Timestamp(System.currentTimeMillis()));
        return ApiInfo.UPDATE_BONUS_SUCCESS;
    }

    //endregion

    //region ------特殊查询------

    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public SearchResult getDiscsByFilterList (JSONObject queryParams, int userAuthority) {

        JSONObject filter = queryParams.getJSONObject("filters");

        String sortField = queryParams.getString("sortField");
        int sortOrder = queryParams.getIntValue("sortOrder");
        int first = queryParams.getIntValue("first");
        int row = queryParams.getIntValue("rows");

        String catalogNo = filter.getJSONObject("catalogNo").getString("value");
        String name = filter.getJSONObject("name").getString("value");
        String region = filter.getJSONObject("region").getString("value");

        List<Integer> products = filter.getJSONObject("products").getList("value", Integer.class);
        List<Integer> franchises = filter.getJSONObject("franchises").getList("value", Integer.class);
        List<Integer> mediaFormat = filter.getJSONObject("mediaFormat").getList("value", Integer.class);

        String limited;
        if (filter.getJSONObject("limited").getBoolean("value") == null) {
            limited = null;
        }else {
            limited = filter.getJSONObject("limited").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        String hasBonus;
        if (filter.getJSONObject("hasBonus").getBoolean("value") == null) {
            hasBonus = null;
        }else {
            hasBonus = filter.getJSONObject("hasBonus").getBoolean("value")
                    ?Integer.toString(1):Integer.toString(0);
        }

        List<Disc> discs = discMapper.getDiscsByFilter(catalogNo, name, region, franchises, products,
                mediaFormat, limited, hasBonus, userAuthority > 2, sortField, sortOrder,  first, row);

        int total = discMapper.getDiscsRowsByFilter(catalogNo, name, region, franchises, products,
                mediaFormat, limited, hasBonus, userAuthority > 2);

        return new SearchResult(total, discs);
    }

    /**
     * 获取相关联Disc
     *
     * @param id 碟片id
     * @return list封装的Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOBeta> getRelatedDiscs(int id) {

        List<Disc> result = new ArrayList<>();

        Disc disc = getDisc(id);

        //该Disc包含的作品id
        List<Integer> productIds = JSONObject.parseObject(disc.getProducts()).getList("ids", Integer.class);

        //该系列所有Disc
        List<Disc> allDiscs = discMapper.getDiscsByFilter(null, null, null,
                        CommonUtils.ids2List(disc.getFranchises()), null, null, null,
                        null, false, "releaseDate", 1, 0, 0)
                .stream().filter(tmpDisc -> tmpDisc.getId() != disc.getId()).toList();

        List<Disc> queryResult = allDiscs.stream().filter(tmpDisc ->
                StringUtils.equals(tmpDisc.getProducts(), disc.getProducts())).toList();

        if (queryResult.size() > 5) {//结果大于5
            result.addAll(queryResult.subList(0, 5));
        } else if (queryResult.size() == 5) {//结果等于5
            result.addAll(queryResult);
        } else if (queryResult.size() > 0) {//结果小于5不为空
            List<Disc> tmp = new ArrayList<>(queryResult);

            if (productIds.size() > 1) {
                List<Disc> tmpQueryResult = allDiscs.stream().filter(tmpDisc ->
                        JSONObject.parseObject(tmpDisc.getProducts()).getList("ids", Integer.class)
                                .contains(productIds.get(1))).toList();

                if (tmpQueryResult.size() >= 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult.subList(0, 5 - queryResult.size()));
                } else if (tmpQueryResult.size() > 0 && tmpQueryResult.size() < 5 - queryResult.size()) {
                    tmp.addAll(tmpQueryResult);
                }
            }
            result.addAll(tmp);
        } else {
            List<Disc> tmp = new ArrayList<>(queryResult);
            for (int productId : productIds) {
                tmp.addAll(
                        allDiscs.stream().filter(tmpDisc ->
                                JSONObject.parseObject(tmpDisc.getProducts()).getList("ids", Integer.class)
                                        .contains(productId)).toList()
                );
            }
            result = CommonUtils.removeDuplicateList(tmp);
            if (result.size() >= 5) {
                result = result.subList(0, 5);
            }
        }

        return discVOMapper.disc2VOBeta(CommonUtils.removeDuplicateList(result));
    }

    /**
     * 根据作品id获取关联disc
     *
     * @param productId 作品id
     * @return List<JSONObject>
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOBeta> getDiscsByProductId(int productId) {

        List<Integer> products = new ArrayList<>();
        products.add(productId);

        List<Disc> discs = discMapper.getDiscsByFilter(null, null, null, null, products,
                null, null, null, false, "releaseDate", -1,  0, 0);

        return discVOMapper.disc2VOBeta(discs);
    }

    /**
     * 获取最新收录的Disc
     *
     * @param limit 获取条数
     * @return list封装的Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOAlpha> getJustAddedDiscs(int limit) {
        return discVOMapper.disc2VOAlpha(discMapper.getDiscsOrderByAddedTime(limit));
    }

    /**
     * 获取最近编辑的Disc
     *
     * @param limit 获取条数
     * @return list封装的Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOAlpha> getJustEditedDiscs(int limit) {
        return discVOMapper.disc2VOAlpha(discMapper.getDiscsOrderByEditedTime(limit));
    }

    /**
     * 获取浏览量最高的Disc
     *
     * @param limit 获取条数
     * @return list封装的Disc
     * @author rakbow
     */
    @Transactional(isolation = Isolation.SERIALIZABLE, rollbackFor = Exception.class, readOnly = true)
    public List<DiscVOAlpha> getPopularDiscs(int limit) {
        List<Visit> visits = visitService.selectVisitOrderByVisitNum(EntityType.DISC.getId(), limit);

        List<Integer> ids = new ArrayList<>();

        visits.sort(DataSorter.visitSortByEntityId);
        visits.forEach(visit -> ids.add(visit.getEntityId()));

        List<DiscVOAlpha> discs = discVOMapper.disc2VOAlpha(discMapper.getDiscs(ids));

        for (int i = 0; i < discs.size(); i++) {
            discs.get(i).setVisitNum(visits.get(i).getVisitNum());
        }

        discs.sort(Collections.reverseOrder(DataSorter.discSortByVisitNum));

        return discs;
    }

    //endregion

}
