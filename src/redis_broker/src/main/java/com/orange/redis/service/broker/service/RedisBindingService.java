package com.orange.redis.service.broker.service;

import com.orange.redis.service.broker.model.RedisConfig;

import reactor.core.publisher.Mono;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;

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
public class RedisBindingService implements ServiceInstanceBindingService {

  private static final Logger logger = LogManager.getLogger(RedisBindingService.class);

  private final RedisConfig redisConfig;

  @Autowired
  public RedisBindingService(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public Mono<CreateServiceInstanceBindingResponse>
      createServiceInstanceBinding(CreateServiceInstanceBindingRequest request) {
    return Mono.just(CreateServiceInstanceAppBindingResponse.builder().build());
  }

  @Override
  public Mono<DeleteServiceInstanceBindingResponse>
      deleteServiceInstanceBinding(DeleteServiceInstanceBindingRequest request) {
    return Mono.just(DeleteServiceInstanceBindingResponse.builder().build());
  }

  @Override
  public Mono<GetServiceInstanceBindingResponse> getServiceInstanceBinding(GetServiceInstanceBindingRequest request) {
    return Mono.just(GetServiceInstanceAppBindingResponse.builder().credentials(redisConfig.toMap()).build());
  }
}