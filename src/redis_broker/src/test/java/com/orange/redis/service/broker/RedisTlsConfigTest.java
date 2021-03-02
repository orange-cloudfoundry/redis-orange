package com.orange.redis.service.broker;

import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;

import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("tls")
public class RedisTlsConfigTest {
  @Autowired
  private RedisConfig redisConfig;

  @Test
  public void Redis() {
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    Assertions.assertEquals("192.168.56.101 192.168.56.102", servers);
    Assertions.assertEquals("0", redisConfig.getPort().toString());
    Assertions.assertEquals("redis_secret", redisConfig.getPassword());
    Assertions.assertEquals("admin", redisConfig.getAdmin_user());
    Assertions.assertEquals("admin_secret", redisConfig.getAdmin_password());
    Assertions.assertTrue(redisConfig.getSentinel().isEmpty());
    Assertions.assertFalse(redisConfig.getTls().isEmpty());
    Assertions.assertEquals("6379", redisConfig.getTls().getPort().toString());
    Assertions.assertEquals(null, redisConfig.getTls().getHa_port());
    Assertions.assertEquals("/home/attu7372/src/bosh/redis-orange/src/redis_broker/src/test/keys",
        redisConfig.getTls().getKeys_dir());
    Assertions.assertEquals("ca.crt", redisConfig.getTls().getCa_cert_file());
    Assertions.assertEquals("ca.key", redisConfig.getTls().getCa_key_file());
    Assertions.assertEquals("2048", redisConfig.getTls().getClient_key_length().toString());
    Assertions.assertEquals("redis-tls", redisConfig.getTls().getClient_cert_ou());
    Assertions.assertEquals("365", redisConfig.getTls().getClient_cert_duration().toString());
  }
}
