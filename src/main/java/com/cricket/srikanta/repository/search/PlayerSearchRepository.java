package com.cricket.srikanta.repository.search;

import com.cricket.srikanta.domain.Player;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Player entity.
 */
public interface PlayerSearchRepository extends ElasticsearchRepository<Player, Long> {
}
