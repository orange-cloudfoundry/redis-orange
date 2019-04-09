package com.orange.redis.service.broker;

import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public
class RedisSentinelConfigTest {

  private static final Logger logger =
          LogManager.getLogger(RedisSentinelConfigTest.class);

  @Autowired
  private RedisConfig redisConfig;

  @Test
  public
  void RedisSentinel() {
    String servers = new String();
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    Assert.assertEquals("192.168.56.101 192.168.56.102 ", servers);
    Assert.assertEquals("6379", redisConfig.getPort().toString());
    Assert.assertEquals("redis_secret", redisConfig.getPassword());
    Assert.assertFalse(redisConfig.getSentinel().isEmpty());
    Assert.assertEquals("master", redisConfig.getSentinel().getMasterName());
    Assert.assertEquals("26379",
                        redisConfig.getSentinel().getPort().toString());
    Assert.assertEquals("redis_sentinel_secret",
                        redisConfig.getSentinel().getPassword());
  }
}
