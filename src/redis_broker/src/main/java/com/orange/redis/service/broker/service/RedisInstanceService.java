package com.orange.redis.service.broker.service;

import com.orange.redis.service.broker.model.RedisConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.instance.GetServiceInstanceResponse;

public class RedisInstanceService implements ServiceInstanceService {
  private static final Logger logger =
          LogManager.getLogger(RedisInstanceService.class);

  private final RedisConfig redisConfig;

  @Autowired
  public RedisInstanceService(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Override
  public CreateServiceInstanceResponse createServiceInstance(
          CreateServiceInstanceRequest request) {
    return CreateServiceInstanceResponse.builder().build();
  }

  @Override
  public GetServiceInstanceResponse getServiceInstance(
          GetServiceInstanceRequest request) {
    return GetServiceInstanceResponse.builder().parameters(redisConfig.toMap())
                                     .build();
  }

  @Override
  public DeleteServiceInstanceResponse deleteServiceInstance(
          DeleteServiceInstanceRequest request) {
    return DeleteServiceInstanceResponse.builder().build();
  }
}
