package com.orange.redis.service.broker;

import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
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
    Assert.assertEquals("192.168.56.101 192.168.56.102", servers);
    Assert.assertEquals("6379", redisConfig.getPort().toString());
    Assert.assertEquals("redis_secret", redisConfig.getPassword());
    Assert.assertEquals("admin", redisConfig.getAdmin_user());
    Assert.assertEquals("admin_secret", redisConfig.getAdmin_password());
    Assert.assertFalse(redisConfig.getSentinel().isEmpty());
    Assert.assertEquals("master", redisConfig.getSentinel().getMaster_name());
    Assert.assertEquals("26379",
        redisConfig.getSentinel().getPort().toString());
    Assert.assertEquals("redis_sentinel_secret",
        redisConfig.getSentinel().getPassword());
    Assert.assertTrue(redisConfig.getTls().isEmpty());
  }
}
