package com.orange.redis.service.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.servicebroker.service.ServiceInstanceBindingService;
import org.springframework.cloud.servicebroker.service.ServiceInstanceService;
import org.springframework.context.annotation.Bean;

import com.orange.redis.service.broker.model.RedisConfig;
import com.orange.redis.service.broker.service.RedisServiceInstanceBindingService;
import com.orange.redis.service.broker.service.RedisServiceInstanceService;

@SpringBootApplication
// same as @Configuration @EnableAutoConfiguration @ComponentScan
public class RedisBrokerApplication {
  private final RedisConfig redisConfig;

  @Autowired
  public RedisBrokerApplication(final RedisConfig redisConfig) {
    this.redisConfig = redisConfig;
  }

  @Bean
  public ServiceInstanceService serviceInstanceService() {
    return new RedisServiceInstanceService(redisConfig);
  }

  @Bean
  public ServiceInstanceBindingService serviceInstanceBindingService() {
    return new RedisServiceInstanceBindingService(redisConfig);
  }

  public static void main(String[] args) {
    SpringApplication.run(RedisBrokerApplication.class, args);
  }
}