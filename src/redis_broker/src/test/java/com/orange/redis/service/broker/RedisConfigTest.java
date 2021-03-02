package com.orange.redis.service.broker;

import java.net.InetAddress;

import org.springframework.beans.factory.annotation.Autowired;

import com.orange.redis.service.broker.model.RedisConfig;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class RedisConfigTest {
  @Autowired
  private RedisConfig redisConfig;

  @Test
  public void Redis() {
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    Assertions.assertEquals("192.168.56.101 192.168.56.102", servers);
    Assertions.assertEquals("6379", redisConfig.getPort().toString());
    Assertions.assertEquals("redis_secret", redisConfig.getPassword());
    Assertions.assertEquals("admin", redisConfig.getAdmin_user());
    Assertions.assertEquals("admin_secret", redisConfig.getAdmin_password());
    Assertions.assertTrue(redisConfig.getSentinel().isEmpty());
    Assertions.assertTrue(redisConfig.getTls().isEmpty());
  }
}
