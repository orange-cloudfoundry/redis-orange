package com.orange.redis.service.broker;

import java.util.Map;

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
  private final RedisConfig redisConfig;

  @Autowired
  public RedisBindingService(RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public CreateServiceInstanceBindingResponse createServiceInstanceBinding(
          CreateServiceInstanceBindingRequest request) {

    return CreateServiceInstanceAppBindingResponse.builder().async(true)
                                                  .build();
  }

  @Override
  public DeleteServiceInstanceBindingResponse deleteServiceInstanceBinding(
          DeleteServiceInstanceBindingRequest request) {
    return DeleteServiceInstanceBindingResponse.builder().async(true).build();
  }

  @Override
  public GetServiceInstanceBindingResponse getServiceInstanceBinding(
          GetServiceInstanceBindingRequest request) {
    Map<String, String> credentials;
    return GetServiceInstanceAppBindingResponse.builder()
                                               .credentials(credentials)
                                               .build();
  }
}