package com.orange.redis.service.broker;

import java.util.List;
import java.util.ArrayList;
import java.net.InetAddress;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "orange.redis")
@Validated
public
class RedisConfig {

  @NotNull
  private List<InetAddress> servers = new ArrayList<>();

  @NotNull
  private Integer port;

  @NotNull
  private String password;

  @Valid
  private Sentinel sentinel = new Sentinel();

  public
  List<InetAddress> getServers() {
    return servers;
  }

  public
  void setServers(List<InetAddress> servers) {
    this.servers = servers;
  }

  public
  int getPort() {
    return port;
  }

  public
  void setPort(int port) {
    this.port = port;
  }

  public
  String getPassword() {
    return password;
  }

  public
  void setPassword(String password) {
    this.password = password;
  }

  public
  Sentinel getSentinel() {
    return sentinel;
  }

  public
  void setSentinel(Sentinel sentinel) {
    this.sentinel = sentinel;
  }

  public static
  class Sentinel {

    @NotNull
    private Integer port;

    @NotNull
    private String password;

    @NotNull
    private String masterName;

    public
    int getPort() {
      return port;
    }

    public
    void setPort(int port) {
      this.port = port;
    }

    public
    String getPassword() {
      return password;
    }

    public
    void setPassword(String password) {
      this.password = password;
    }

    public
    String getMasterName() {
      return masterName;
    }

    public
    void setMasterName(String masterName) {
      this.masterName = masterName;
    }
  }
}
