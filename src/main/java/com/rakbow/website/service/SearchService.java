package com.rakbow.website.service;

import com.rakbow.website.data.vo.album.AlbumVOGamma;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.util.SafeEncoder;

import javax.annotation.Resource;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2023-05-10 16:42
 * @Description:
 */
@Service
public class SearchService {
    private final String INDEX_NAME = "albumGamma";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 创建 Redisearch 索引
     */
    public void createIndex() {
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        String mapping = "title TEXT SORTABLE author TEXT SORTABLE price NUMERIC SORTABLE publishDate TEXT SORTABLE";
        String schema = String.format("SCHEMA %s %s", INDEX_NAME, mapping);
        jedis.execute(String.format("FT.CREATE %s ON HASH SCHEMA %s", INDEX_NAME, mapping));
    }

    /**
     * 添加书籍到 Redisearch 索引
     */
    public void addAlbumGamma(AlbumVOGamma book) {
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        String id = "book:" + book.getId();
        jedis.xadd(INDEX_NAME.getBytes(), "MAXLEN".getBytes(), "1000".getBytes(), "*", SafeEncoder.encode("id"), SafeEncoder.encode(id), SafeEncoder.encode("title"), SafeEncoder.encode(book.getTitle()), SafeEncoder.encode("author"), SafeEncoder.encode(book.getAuthor()), SafeEncoder.encode("price"), SafeEncoder.encode(book.getPrice().toString()), SafeEncoder.encode("publishDate"), SafeEncoder.encode(book.getPublishDate()));
    }

    /**
     * 搜索 Redisearch 索引
     */
    public SearchResult searchBooks(String query) {
        Jedis jedis = (Jedis) redisTemplate.getConnectionFactory().getConnection().getNativeConnection();
        Query redisQuery = new Query(query);
        redisQuery.setReturnFields("title,author,price,publishDate");
        redisQuery.setHighlightFields("title");
        redisQuery.highlightPre("<strong>").highlightPost("</strong>");
        return new SearchResults(jedis, INDEX_NAME, redisQuery).search();
    }

}
