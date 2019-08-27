package com.orange.redis.service.broker;

import java.util.Map;
import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;
import com.orange.redis.service.broker.service.RedisServiceInstanceBindingService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.binding.GetServiceInstanceBindingResponse;
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
  private RedisServiceInstanceBindingService service;

  @Before
  public void setUp() {
    service = new RedisServiceInstanceBindingService(redisConfig);
  }

  @Test
  public void getServiceInstanceBinding() {
    service.getServiceInstanceBinding(GetServiceInstanceBindingRequest.builder().build())
        .subscribe((GetServiceInstanceBindingResponse x) -> {
          String servers = new String();
          for (InetAddress address : redisConfig.getServers())
            servers = servers.concat(address.getHostAddress()).concat(" ");
          servers = servers.trim();
          for (Map.Entry<String, Object> credentials : ((GetServiceInstanceAppBindingResponse) x).getCredentials()
              .entrySet()) {
            if (credentials.getKey().compareTo(redisConfig.getIPKey()) == 0)
              Assert.assertEquals(servers, credentials.getValue());
            if (credentials.getKey().compareTo(redisConfig.getPortKey()) == 0)
              Assert.assertEquals(redisConfig.getPort().toString(), credentials.getValue());
            if (credentials.getKey().compareTo(redisConfig.getPasswordKey()) == 0)
              Assert.assertEquals(redisConfig.getPassword(), credentials.getValue());
            if (!redisConfig.getSentinel().isEmpty()) {
              if (credentials.getKey().compareTo(redisConfig.getHAIPKey()) == 0)
                Assert.assertEquals(redisConfig.getSentinel().getMasterName(), credentials.getValue());
              if (credentials.getKey().compareTo(redisConfig.getHAPortKey()) == 0)
                Assert.assertEquals(redisConfig.getSentinel().getPort().toString(), credentials.getValue());
              if (credentials.getKey().compareTo(redisConfig.getHAPasswordKey()) == 0)
                Assert.assertEquals(redisConfig.getSentinel().getPassword(), credentials.getValue());
            }
          }
        });
  }
}