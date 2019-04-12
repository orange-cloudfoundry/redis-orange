package com.orange.redis.service.broker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class RedisService {

  public static void main(String[] args) {
    SpringApplication.run(RedisService.class, args);
  }

}