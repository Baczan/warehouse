package com.example.pkce.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends AbstractElasticsearchConfiguration {

  @Autowired
  private Environment env;

  @Override
  public RestHighLevelClient elasticsearchClient() {
    final ClientConfiguration clientConfiguration =
        ClientConfiguration.builder().connectedTo(env.getProperty("app.elasticsearch.url")).build();

    return RestClients.create(clientConfiguration).rest();
  }

}
