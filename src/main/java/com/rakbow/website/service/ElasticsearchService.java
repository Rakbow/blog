package com.rakbow.website.service;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.elasticsearch.AlbumRepository;
import com.rakbow.website.dao.elasticsearch.MusicRepository;
import com.rakbow.website.entity.Album;
import com.rakbow.website.entity.Music;
import com.rakbow.website.util.common.CommonUtil;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-13 2:08
 * @Description:
 */
@Service
public class ElasticsearchService {

    @Autowired
    private AlbumRepository albumRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private AlbumService albumService;

    @Autowired
    private MusicService musicService;

    @Autowired
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    //region 专辑相关
    public void saveAlbum(Album album) {
        albumRepository.save(album);
    }

    public void deleteAlbum(int id) {
        albumRepository.deleteById(id);
    }

    public List<Album> searchAlbum(String keyword) throws IOException, ParseException {

        //搜索的索引名，album，就是表名
        SearchRequest searchRequest = new SearchRequest("album");

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        //返回结果高亮
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("nameZh");
        highlightBuilder.field("nameJp");
        highlightBuilder.field("nameEn");
        highlightBuilder.field("label");
        highlightBuilder.field("publisher");
        highlightBuilder.field("distributor");
        highlightBuilder.field("copyright");
        highlightBuilder.requireFieldMatch(false);
        highlightBuilder.preTags("<span style='color:red'>");
        highlightBuilder.postTags("</span>");

        //构建搜索条件
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
                //在album索引的以下字段中都查询keyword
                .query(QueryBuilders.multiMatchQuery(keyword,
                        "nameZh", "nameJp", "nameEn", "label", "publisher", "distributor", "copyright"))
                // matchQuery是模糊查询，会对key进行分词：searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
                // termQuery是精准查询：searchSourceBuilder.query(QueryBuilders.termQuery(key,value));
                .sort(SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC))
                .sort(SortBuilders.fieldSort("id").order(SortOrder.DESC))
                //一个可选项，用于控制允许搜索的时间：searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
                .from(0)// 指定从哪条开始查询
                // .size(limit)// 需要查出的总记录条数
                .highlighter(highlightBuilder);//高亮

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);

        List<Album> albums = new LinkedList<>();

        for (SearchHit hit : searchResponse.getHits()) {
            Date date = simpleDateFormat.parse(JSON.parseObject(hit.getSourceAsString()).get("releaseDate").toString());
            JSONObject albumJson = JSON.parseObject(hit.getSourceAsString());
            albumJson.put("releaseDate", CommonUtil.dateToString(date));
            albumJson.remove("_class");
            Album album = albumService.json2Album(albumJson);

            // 处理高亮显示的结果
            HighlightField nameZhField = hit.getHighlightFields().get("nameZh");
            if (nameZhField != null) {
                album.setNameZh(nameZhField.getFragments()[0].toString());
            }
            HighlightField nameJpField = hit.getHighlightFields().get("nameJp");
            if (nameJpField != null) {
                album.setNameJp(nameJpField.getFragments()[0].toString());
            }
            HighlightField nameEnField = hit.getHighlightFields().get("nameEn");
            if (nameEnField != null) {
                album.setNameEn(nameEnField.getFragments()[0].toString());
            }
            HighlightField labelField = hit.getHighlightFields().get("label");
            if (labelField != null) {
                album.setLabel(labelField.getFragments()[0].toString());
            }
            HighlightField publisherField = hit.getHighlightFields().get("publisher");
            if (publisherField != null) {
                album.setPublisher(publisherField.getFragments()[0].toString());
            }
            HighlightField distributorField = hit.getHighlightFields().get("distributor");
            if (distributorField != null) {
                album.setDistributor(distributorField.getFragments()[0].toString());
            }
            HighlightField copyrightField = hit.getHighlightFields().get("copyright");
            if (copyrightField != null) {
                album.setCopyright(copyrightField.getFragments()[0].toString());
            }
            albums.add(album);
        }


        return albums;
    }

    //endregion

    //region 专辑相关
    public void saveMusic(Music music) {
        musicRepository.save(music);
    }

    public void deleteMusic(int id) {
        musicRepository.deleteById(id);
    }
    //endregion

}
