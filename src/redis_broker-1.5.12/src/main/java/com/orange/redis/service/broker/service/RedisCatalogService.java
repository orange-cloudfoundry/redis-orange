package com.orange.redis.service.broker.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.ServiceDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.orange.redis.service.broker.catalog.CatalogYmlReader;

@Service
public class RedisCatalogService {

  @Value("${catalog_yml}")
  private String catalogYml;

  public String getCatalog() {
    return catalogYml;
  }

  @Bean
  public Catalog catalog() {
      CatalogYmlReader catalogYmlReader = new CatalogYmlReader();
      List<ServiceDefinition> serviceDefinitions = catalogYmlReader.getServiceDefinitions(catalogYml);
      return new Catalog(serviceDefinitions);
  }
}
