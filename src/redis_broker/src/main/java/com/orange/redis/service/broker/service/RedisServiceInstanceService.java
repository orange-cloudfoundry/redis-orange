package com.orange.redis.service.broker.service;

import com.orange.redis.service.broker.model.RedisConfig;

import reactor.core.publisher.Mono;

import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceResponse;

public class RedisServiceInstanceService implements ServiceInstanceService {
  private final RedisConfig redisConfig;

  public RedisServiceInstanceService(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public Mono<CreateServiceInstanceResponse> createServiceInstance(CreateServiceInstanceRequest request) {
    return Mono.just(CreateServiceInstanceResponse.builder().build());
  }

  @Override
  public Mono<GetServiceInstanceResponse> getServiceInstance(GetServiceInstanceRequest request) {
    return Mono.just(GetServiceInstanceResponse.builder().parameters(redisConfig.toMap()).build());
  }

  @Override
  public Mono<DeleteServiceInstanceResponse> deleteServiceInstance(DeleteServiceInstanceRequest request) {
    return Mono.just(DeleteServiceInstanceResponse.builder().build());
  }
}
