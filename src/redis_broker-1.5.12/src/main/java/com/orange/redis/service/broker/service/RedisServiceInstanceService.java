package com.orange.redis.service.broker.service;

import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.DeleteServiceInstanceResponse;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationRequest;
import org.springframework.cloud.servicebroker.model.GetLastServiceOperationResponse;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceRequest;
import org.springframework.cloud.servicebroker.model.UpdateServiceInstanceResponse;

public class RedisServiceInstanceService implements ServiceInstanceService {
  @Override
  public CreateServiceInstanceResponse createServiceInstance(CreateServiceInstanceRequest request) {
    return new CreateServiceInstanceResponse().withDashboardUrl("");
  }

  @Override
  public DeleteServiceInstanceResponse deleteServiceInstance(DeleteServiceInstanceRequest request) {
    return null;
  }

  @Override
  public UpdateServiceInstanceResponse updateServiceInstance(UpdateServiceInstanceRequest request) {
    return null;
  }

  @Override
  public GetLastServiceOperationResponse getLastOperation(GetLastServiceOperationRequest request) {
    return null;
  }
}
