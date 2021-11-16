package com.example.pkce.services;

import com.example.pkce.elasticSearchEntities.ElasticUser;
import com.example.pkce.exceptions.ElasticUserNotFound;
import com.example.pkce.repositories.ElasticUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ElasticUserService {

  @Autowired private ElasticUserRepository elasticUserRepository;

  public ElasticUser findById(int id) throws ElasticUserNotFound {
    Optional<ElasticUser> elasticUser = elasticUserRepository.findById(id);
    return elasticUser.orElseThrow(() -> new ElasticUserNotFound("elastic_user_not_found"));
  }
}
