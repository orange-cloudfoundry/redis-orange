package com.orange.redis.service.broker;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingRequest;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisBindingServiceTest {

  @Autowired
  private RedisConfig redisConfig;

  private RedisBindingService service;

  @Before
  public void setUp() {
    service = new RedisBindingService(redisConfig);
  }

  @Test
  public void getServiceInstanceBinding() {
    GetServiceInstanceAppBindingResponse response =
            (GetServiceInstanceAppBindingResponse) service
                    .getServiceInstanceBinding(
                            GetServiceInstanceBindingRequest.builder().build());
    for (Map.Entry<String, Object> credentials : response.getCredentials()
                                                         .entrySet()) {
    }
  }
}