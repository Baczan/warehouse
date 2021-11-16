package com.example.resource.repositories;

import com.example.resource.elasticSearchEntities.ReturnElastic;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnElasticRepository extends ElasticsearchRepository<ReturnElastic,Integer> {
}
