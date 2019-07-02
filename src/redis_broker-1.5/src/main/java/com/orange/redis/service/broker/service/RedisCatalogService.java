package com.orange.redis.service.broker.service;

import static java.util.Arrays.asList;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.servicebroker.model.Catalog;
import org.springframework.cloud.servicebroker.model.Plan;
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
    Catalog catalog;
    if (catalogYml == null) { // hard coded catalog is returned
      catalog =
          new Catalog(
              Collections
                  .singletonList(
                      new ServiceDefinition(
                          "redis-service",
                          "Redis for Cloud Foundry",
                          "Redis on demand on dedicated cluster",
                          true,
                          false,
                          Collections
                              .singletonList(
                                  new Plan(
                                      "redis-plan",
                                      "default",
                                      "This is a default Redis plan. All services are created equally.",
                                      getPlanMetadata())),
                          asList("Redis", "document"),
                          getServiceDefinitionMetadata(),
                          null,
                          null)));
    } else {
      CatalogYmlReader catalogYmlReader = new CatalogYmlReader();
      List<ServiceDefinition> serviceDefinitions = catalogYmlReader.getServiceDefinitions(catalogYml);
      catalog = new Catalog(serviceDefinitions);
    }
    return catalog;
  }

  /* Used by Pivotal CF console */

  private Map<String, Object> getServiceDefinitionMetadata() {
    Map<String, Object> sdMetadata = new HashMap<>();
    sdMetadata.put("displayName", "Redis");
    sdMetadata.put("imageUrl", "https://redis.io/images/redis-white.png");
    sdMetadata
        .put(
            "longDescription",
            "Redis is an open source (BSD licensed), in-memory data structure store, used as a database, cache and message broker. It supports data structures such as strings, hashes, lists, sets, sorted sets with range queries, bitmaps, hyperloglogs, geospatial indexes with radius queries and streams. Redis has built-in replication, Lua scripting, LRU eviction, transactions and different levels of on-disk persistence, and provides high availability via Redis Sentinel and automatic partitioning with Redis Cluster.");
    sdMetadata.put("providerDisplayName", "Orange");
    sdMetadata.put("documentationUrl", "https://redis.io/documentation");
    sdMetadata.put("supportUrl", "https://cap.nd-cfapi.itn.ftgroup/contact-us/");
    return sdMetadata;
  }

  private Map<String, Object> getPlanMetadata() {
    Map<String, Object> planMetadata = new HashMap<>();
    planMetadata.put("costs", getCosts());
    planMetadata.put("bullets", getBullets());
    return planMetadata;
  }

  private List<Map<String, Object>> getCosts() {
    Map<String, Object> costsMap = new HashMap<>();
    Map<String, Object> amount = new HashMap<>();
    amount.put("eur", 10.0);
    costsMap.put("amount", amount);
    costsMap.put("unit", "MONTHLY");
    return Collections.singletonList(costsMap);
  }

  private List<String> getBullets() {
    return Arrays
        .asList("Shared cassandra server", "100 MB Storage (not enforced)", "40 concurrent connections (not enforced)");
  }
}
