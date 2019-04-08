package com.orange.redis.service.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

@SpringBootApplication
// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class RedisService {

  private static final Logger logger = LogManager.getLogger(RedisService.class);

  public static void main(String[] args) {
    SpringApplication.run(RedisService.class, args);
  }

}