package com.orange.redis.service.broker.service;

import com.orange.redis.service.broker.model.RedisConfig;

import org.springframework.stereotype.Service;

import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.CreateServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.DeleteServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.DeleteServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingResponse;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;

@Service
public class RedisServiceInstanceBindingService implements ServiceInstanceBindingService {
  private final RedisConfig redisConfig;

  public RedisServiceInstanceBindingService(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public CreateServiceInstanceBindingResponse
      createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
    return CreateServiceInstanceAppBindingResponse.builder().build();
  }

  @Override
  public DeleteServiceInstanceBindingResponse
      deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
    return DeleteServiceInstanceBindingResponse.builder().build();
  }

  @Override
  public GetServiceInstanceBindingResponse getServiceInstanceBinding(GetServiceInstanceBindingRequest request) {
    return GetServiceInstanceAppBindingResponse.builder().credentials(redisConfig.toMap()).build();
  }
}