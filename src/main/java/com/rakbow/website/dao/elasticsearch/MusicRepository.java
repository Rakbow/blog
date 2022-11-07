package com.rakbow.website.dao.elasticsearch;

import com.rakbow.website.entity.Music;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Project_name: website
 * @Author: Rakbow
 * @Create: 2022-11-08 0:21
 * @Description:
 */
@Repository
public interface MusicRepository extends ElasticsearchRepository<Music, Integer> {
}
