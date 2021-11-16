package com.example.pkce.repositories;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticUserRepository extends ElasticsearchRepository<ElasticUser, Integer> {}
