package com.orange.redis.service.broker.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.net.InetAddress;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

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
  private String IPKey;
  @NotNull
  private String portKey;
  @NotNull
  private String passwordKey;
  @NotNull
  private String HAIPKey;
  @NotNull
  private String HAPortKey;
  @NotNull
  private String HAPasswordKey;
  @NotEmpty
  private List<InetAddress> servers = new ArrayList<>();
  @NotNull
  private Integer port;
  @NotEmpty
  private String password;
  @Valid
  private Sentinel sentinel = new Sentinel();

  public String getIPKey() {
    return IPKey;
  }

  public void setIPKey(String IPKey) {
    this.IPKey = IPKey;
  }

  public String getPortKey() {
    return portKey;
  }

  public void setPortKey(String portKey) {
    this.portKey = portKey;
  }

  public String getPasswordKey() {
    return passwordKey;
  }

  public void setPasswordKey(String passwordKey) {
    this.passwordKey = passwordKey;
  }

  public String getHAIPKey() {
    return HAIPKey;
  }

  public void setHAIPKey(String HAIPKey) {
    this.HAIPKey = HAIPKey;
  }

  public String getHAPortKey() {
    return HAPortKey;
  }

  public void setHAPortKey(String HAPortKey) {
    this.HAPortKey = HAPortKey;
  }

  public String getHAPasswordKey() {
    return HAPasswordKey;
  }

  public void setHAPasswordKey(String HAPasswordKey) {
    this.HAPasswordKey = HAPasswordKey;
  }

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
    servers = servers.trim();
    credentials.put(getIPKey(), servers);
    logger.info(getIPKey().concat(" ").concat(servers));
    credentials.put(getPortKey(), getPort().toString());
    logger.info(getPortKey().concat(" ").concat(getPort().toString()));
    credentials.put(getPasswordKey(), getPassword());
    logger.info(getPasswordKey().concat(" ").concat(getPassword()));
    if (!getSentinel().isEmpty()) {
      credentials.put(getHAIPKey(), getSentinel().getMasterName());
      logger.info(getHAIPKey().concat(" ").concat(getSentinel().getMasterName()));
      credentials.put(getHAPortKey(), getSentinel().getPort().toString());
      logger.info(getHAPortKey().concat(" ").concat(getSentinel().getPort().toString()));
      credentials.put(getHAPasswordKey(), getSentinel().getPassword());
      logger.info(getHAPasswordKey().concat(" ").concat(getSentinel().getPassword()));
    }
    return credentials;
  }
}