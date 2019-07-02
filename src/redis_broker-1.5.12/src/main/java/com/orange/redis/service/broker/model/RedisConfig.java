package com.orange.redis.service.broker.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.InetAddress;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "orange.redis")
@Validated
public class RedisConfig {
  private static final Logger logger = LogManager.getLogger(RedisConfig.class);
  @NotNull
  private List<InetAddress> servers = new ArrayList<>();
  @NotNull
  private Integer port;
  @NotNull
  private String password;
  @Valid
  private Sentinel sentinel = new Sentinel();

  public List<InetAddress> getServers() {
    return servers;
  }

  public void setServers(List<InetAddress> servers) {
    this.servers = servers;
  }

  public Integer getPort() {
    return port;
  }

  public void setPort(Integer port) {
    this.port = port;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Sentinel getSentinel() {
    return sentinel;
  }

  public void setSentinel(Sentinel sentinel) {
    this.sentinel = sentinel;
  }

  public static class Sentinel {

    private String masterName = null;

    private Integer port = null;

    private String password = null;

    public String getMasterName() {
      return masterName;
    }

    public void setMasterName(String masterName) {
      this.masterName = masterName;
    }

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public String getPassword() {
      return password;
    }

    public void setPassword(String password) {
      this.password = password;
    }

    public boolean isEmpty() {
      return masterName == null && port == null && password == null;
    }
  }

  public Map<String, Object> toMap() {
    Map<String, Object> credentials = new HashMap<>();
    String servers = new String();
    String key;
    for (InetAddress address : getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    key = "Redis servers:";
    credentials.put(key, servers);
    logger.info(key.concat(" ").concat(servers));
    key = "Redis port:";
    credentials.put(key, getPort().toString());
    logger.info(key.concat(" ").concat(getPort().toString()));
    key = "Redis password:";
    credentials.put(key, getPassword());
    logger.info(key.concat(" ").concat(getPassword()));
    key = "Redis Sentinel:";
    logger.info(key.concat(" ").concat(getSentinel().isEmpty() ? "false" : "true"));
    if (!getSentinel().isEmpty()) {
      key = "Redis Sentinel master name:";
      credentials.put(key, getSentinel().getMasterName());
      logger.info(key.concat(" ").concat(getSentinel().getMasterName()));
      key = "Redis Sentinel port:";
      credentials.put(key, getSentinel().getPort().toString());
      logger.info(key.concat(" ").concat(getSentinel().getPort().toString()));
      key = "Redis Sentinel password:";
      credentials.put(key, getSentinel().getPassword());
      logger.info(key.concat(" ").concat(getSentinel().getPassword()));
    }
    return credentials;
  }
}