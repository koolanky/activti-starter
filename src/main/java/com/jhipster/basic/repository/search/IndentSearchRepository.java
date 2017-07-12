package com.jhipster.basic.repository.search;

import com.jhipster.basic.domain.Indent;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Indent entity.
 */
public interface IndentSearchRepository extends ElasticsearchRepository<Indent, Long> {
}
