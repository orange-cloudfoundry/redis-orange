package com.orange.redis.service.broker;

import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotEmpty;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@ConfigurationProperties(prefix = "orange.redis")
@Validated
public class RedisConfig {

  @NotEmpty
  private List<InetAddress> servers = new ArrayList<>();

  @NotNull
  private Integer port;

  @NotEmpty
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
}
