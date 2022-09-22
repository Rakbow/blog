package com.rakbow.website;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.rakbow.website.dao.AlbumMapper;
// import com.rakbow.website.dao.elasticsearch.AlbumRepository;
import com.rakbow.website.entity.Album;
import com.rakbow.website.service.AlbumService;
// import com.rakbow.website.service.ElasticsearchService;
import com.rakbow.website.service.util.common.CommonUtil;
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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-09-13 0:33
 * @Description:
 */
// @RunWith(SpringRunner.class)
// @SpringBootTest
// @ContextConfiguration(classes = WebSiteApplication.class)
// public class ElasticsearchTests {
//
//     @Autowired
//     private AlbumMapper albumMapper;
//
//     @Autowired
//     private AlbumRepository albumRepository;
//
//     @Autowired
//     private RestHighLevelClient restHighLevelClient;
//
//     @Autowired
//     private AlbumService albumService;
//
//     @Autowired
//     private ElasticsearchService elasticsearchService;
//
//     @Test
//     public void  testInsert(){
//         albumRepository.deleteAll();
//     }
//
//     @Test
//     public void testInsertList(){
//         albumRepository.saveAll(albumMapper.getAll());
//     }
//
//     @Test
//     public void testAlbumRepository() throws IOException, ParseException {
//         SearchRequest searchRequest = new SearchRequest("album");//album，就是表名
//
//         SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//         //高亮
//         HighlightBuilder highlightBuilder = new HighlightBuilder();
//         highlightBuilder.field("nameJp");
//         highlightBuilder.requireFieldMatch(false);
//         highlightBuilder.preTags("<span style='color:red'>");
//         highlightBuilder.postTags("</span>");
//
//         //构建搜索条件
//         SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder()
//                 //在album索引的nameJp字段中都查询“ドラマ”
//                 .query(QueryBuilders.multiMatchQuery("ドラマ", "nameJp"))
//                 // matchQuery是模糊查询，会对key进行分词：searchSourceBuilder.query(QueryBuilders.matchQuery(key,value));
//                 // termQuery是精准查询：searchSourceBuilder.query(QueryBuilders.termQuery(key,value));
//                 .sort(SortBuilders.fieldSort("releaseDate").order(SortOrder.DESC))
//                 //一个可选项，用于控制允许搜索的时间：searchSourceBuilder.timeout(new TimeValue(60, TimeUnit.SECONDS));
//                 .from(0)// 指定从哪条开始查询
//                 .size(10)// 需要查出的总记录条数
//                 .highlighter(highlightBuilder);//高亮
//
//         searchRequest.source(searchSourceBuilder);
//         SearchResponse searchResponse = restHighLevelClient.search(searchRequest, RequestOptions.DEFAULT);
//
//         List<Album> list = new LinkedList<>();
//
//         for(SearchHit hit : searchResponse.getHits()){
//             Date date = simpleDateFormat.parse(JSON.parseObject(hit.getSourceAsString()).get("releaseDate").toString());
//             JSONObject albumJson = JSON.parseObject(hit.getSourceAsString());
//             albumJson.put("releaseDate", CommonUtil.dateToString(date, "yyyy/MM/dd"));
//             albumJson.remove("_class");
//             Album album = albumService.json2Album(albumJson);
//
//             // 处理高亮显示的结果
//             HighlightField nameJpField = hit.getHighlightFields().get("nameJp");if(nameJpField != null){
//                 album.setNameJp(nameJpField.getFragments()[0].toString());
//             }
//             System.out.println(album);
//             list.add(album);
//         }
//
//     }
//
//     @Test
//     public void test111() throws IOException, ParseException {
//         elasticsearchService.searchAlbum("MAGES").stream().forEach(i -> System.out.println(i));
//     }
//
// }
