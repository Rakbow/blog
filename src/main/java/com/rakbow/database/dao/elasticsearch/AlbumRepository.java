package com.rakbow.database.dao.elasticsearch;

import com.rakbow.database.entity.Album;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Project_name: database
 * @Author: Rakbow
 * @Create: 2022-09-13 0:31
 * @Description:
 */
@Repository
public interface AlbumRepository extends ElasticsearchRepository<Album, Integer> {
}
