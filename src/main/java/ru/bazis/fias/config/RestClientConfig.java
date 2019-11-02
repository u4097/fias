package ru.bazis.fias.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;


@Configuration
@EnableElasticsearchRepositories(basePackages = "ru.basis.fias.repository")
@ComponentScan(basePackages = "ru.basis.fias.service")
public class RestClientConfig extends AbstractElasticsearchConfiguration {

  @Override
  public RestHighLevelClient elasticsearchClient() {
    return RestClients.create(ClientConfiguration.localhost()).rest();
  }

  @Bean
  @Override
  public EntityMapper entityMapper() {
    ElasticsearchEntityMapper entityMapper = new ElasticsearchEntityMapper(
        elasticsearchMappingContext(),
        new DefaultConversionService());
    entityMapper.setConversions(elasticsearchCustomConversions());

    return entityMapper;
  }

}
