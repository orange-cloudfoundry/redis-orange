package com.orange.redis.service.broker;

import java.util.Map;
import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;
import com.orange.redis.service.broker.service.RedisServiceInstanceBindingService;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.runner.RunWith;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceAppBindingResponse;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingRequest;
import org.springframework.cloud.servicebroker.model.CreateServiceInstanceBindingResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("sentinel-tls")
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
    CreateServiceInstanceBindingResponse x = service
        .createServiceInstanceBinding(
            new CreateServiceInstanceBindingRequest());
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    for (Map.Entry<String, Object> credentials : ((CreateServiceInstanceAppBindingResponse) x)
        .getCredentials().entrySet()) {
      if (credentials.getKey().compareTo(redisConfig.getIp_key()) == 0)
        Assert.assertEquals(servers, credentials.getValue());
      if (credentials.getKey().compareTo(redisConfig.getPort_key()) == 0)
        Assert.assertEquals(redisConfig.getPort().toString(),
            credentials.getValue());
      if (credentials.getKey().compareTo(redisConfig.getPassword_key()) == 0)
        Assert.assertEquals(redisConfig.getPassword(), credentials.getValue());
      if (credentials.getKey().compareTo(redisConfig.getAdmin_user_key()) == 0)
        Assert.assertEquals(redisConfig.getAdmin_user(),
            credentials.getValue());
      if (credentials.getKey()
          .compareTo(redisConfig.getAdmin_password_key()) == 0)
        Assert.assertEquals(redisConfig.getAdmin_password(),
            credentials.getValue());
      if (!redisConfig.getSentinel().isEmpty()) {
        if (credentials.getKey()
            .compareTo(redisConfig.getHa_master_name_key()) == 0)
          Assert.assertEquals(redisConfig.getSentinel().getMaster_name(),
              credentials.getValue());
        if (credentials.getKey().compareTo(redisConfig.getHa_port_key()) == 0)
          Assert.assertEquals(redisConfig.getSentinel().getPort().toString(),
              credentials.getValue());
        if (credentials.getKey()
            .compareTo(redisConfig.getHa_password_key()) == 0)
          Assert.assertEquals(redisConfig.getSentinel().getPassword(),
              credentials.getValue());
      }
      if (!redisConfig.getTls().isEmpty()) {
        if (credentials.getKey().compareTo(redisConfig.getTls_port_key()) == 0)
          Assert.assertEquals(redisConfig.getTls().getPort().toString(),
              credentials.getValue());
        if (credentials.getKey()
            .compareTo(redisConfig.getTls_ha_port_key()) == 0)
          Assert.assertEquals(redisConfig.getTls().getHa_port().toString(),
              credentials.getValue());
        if (credentials.getKey()
            .compareTo(redisConfig.getTls_certificate_key()) == 0)
          Assert.assertNotNull(credentials.getValue());
        if (credentials.getKey()
            .compareTo(redisConfig.getTls_private_key_key()) == 0)
          Assert.assertNotNull(credentials.getValue());
        if (credentials.getKey().compareTo(redisConfig.getTls_ca_key()) == 0)
          Assert.assertNotNull(credentials.getValue());
      }
    }
  }
}
