package com.orange.redis.service.broker.service;

import com.orange.redis.service.broker.model.RedisConfig;

import org.springframework.stereotype.Service;

import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceBindingRequest;

@Service
public class RedisServiceInstanceBindingService
    implements ServiceInstanceBindingService {
  private final RedisConfig redisConfig;

  public RedisServiceInstanceBindingService(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
      CreateServiceInstanceBindingRequest request) {
    return new CreateServiceInstanceAppBindingResponse()
        .withCredentials(redisConfig.toMap());
  }

  @Override
  public void deleteServiceInstanceBinding(
      DeleteServiceInstanceBindingRequest request) {
  }
}
