package com.orange.redis.service.broker;

import java.util.Map;
import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;
import com.orange.redis.service.broker.service.RedisBindingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingRequest;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("sentinel")
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
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    for (Map.Entry<String, Object> credentials : response.getCredentials()
                                                         .entrySet()) {
      if (credentials.getKey().compareTo("Redis servers:") == 0)
        Assert.assertEquals(servers, credentials.getValue());
      if (credentials.getKey().compareTo("Redis port:") == 0)
        Assert.assertEquals(redisConfig.getPort().toString(),
                            credentials.getValue());
      if (credentials.getKey().compareTo("Redis password:") == 0)
        Assert.assertEquals(redisConfig.getPassword(), credentials.getValue());
      if (!redisConfig.getSentinel().isEmpty()) {
        if (credentials.getKey().compareTo("Redis Sentinel master name:") == 0)
          Assert.assertEquals(redisConfig.getSentinel().getMasterName(),
                              credentials.getValue());
        if (credentials.getKey().compareTo("Redis Sentinel port:") == 0)
          Assert.assertEquals(redisConfig.getSentinel().getPort().toString(),
                              credentials.getValue());
        if (credentials.getKey().compareTo("Redis Sentinel password:") == 0)
          Assert.assertEquals(redisConfig.getSentinel().getPassword(),
                              credentials.getValue());
      }
    }
  }
}