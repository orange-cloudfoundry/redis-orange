package com.orange.redis.service.broker;

import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("sentinel")
public class RedisSentinelConfigTest {
  @Autowired
  private RedisConfig redisConfig;

  @Test
  public void RedisSentinel() {
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    Assertions.assertEquals("192.168.56.101 192.168.56.102", servers);
    Assertions.assertEquals("6379", redisConfig.getPort().toString());
    Assertions.assertEquals("redis_secret", redisConfig.getPassword());
    Assertions.assertEquals("admin", redisConfig.getAdmin_user());
    Assertions.assertEquals("admin_secret", redisConfig.getAdmin_password());
    Assertions.assertFalse(redisConfig.getSentinel().isEmpty());
    Assertions.assertEquals("master", redisConfig.getSentinel().getMaster_name());
    Assertions.assertEquals("26379", redisConfig.getSentinel().getPort().toString());
    Assertions.assertEquals("redis_sentinel_secret", redisConfig.getSentinel().getPassword());
    Assertions.assertTrue(redisConfig.getTls().isEmpty());
  }
}
