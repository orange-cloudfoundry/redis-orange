package com.orange.redis.service.broker;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.beans.factory.annotation.Autowired;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisConfigTest {

  @Autowired
  private RedisConfig redisConfig;

  @Test
  public void SingleRedisServer() {
  }
}
