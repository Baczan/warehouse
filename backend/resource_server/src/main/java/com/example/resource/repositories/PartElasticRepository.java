package com.example.resource.repositories;

import com.example.resource.elasticSearchEntities.PartElastic;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartElasticRepository extends ElasticsearchRepository<PartElastic,Integer> {

}
