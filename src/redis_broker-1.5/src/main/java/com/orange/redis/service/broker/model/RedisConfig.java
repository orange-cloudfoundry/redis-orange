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
  private String IPKey;
  @NotNull
  private String portKey;
  @NotNull
  private String passwordKey;
  @NotNull
  private String adminUserKey;
  @NotNull
  private String adminPasswordKey;
  @NotNull
  private String HAIPKey;
  @NotNull
  private String HAPortKey;
  @NotNull
  private String HAPasswordKey;
  @NotNull
  private String tlsPortKey;
  @NotNull
  private String tlsClientCERTKey;
  @NotNull
  private String tlsClientKeyKey;
  @NotNull
  private String tlsCACERTKey;
  @NotNull
  private List<InetAddress> servers = new ArrayList<>();
  @NotNull
  private Integer port;
  @NotNull
  private String password;
  @NotNull
  private String admin_user;
  @NotNull
  private String admin_password;
  @Valid
  private Sentinel sentinel = new Sentinel();
  @Valid
  private TLS tls = new TLS();

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

  public String getAdminUserKey() {
    return adminUserKey;
  }

  public void setAdminUserKey(String adminUserKey) {
    this.adminUserKey = adminUserKey;
  }

  public String getAdminPasswordKey() {
    return adminPasswordKey;
  }

  public void setAdminPasswordKey(String adminPasswordKey) {
    this.adminPasswordKey = adminPasswordKey;
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

  public String getTLSPortKey() {
    return tlsPortKey;
  }

  public void setTLSPortKey(String tlsPortKey) {
    this.tlsPortKey = tlsPortKey;
  }

  public String getTLSClientCERTKey() {
    return tlsClientCERTKey;
  }

  public void setTLSClientCERTKey(String tlsClientCERTKey) {
    this.tlsClientCERTKey = tlsClientCERTKey;
  }

  public String getTLSClientKeyKey() {
    return tlsClientKeyKey;
  }

  public void setTLSClientKeyKey(String tlsClientKeyKey) {
    this.tlsClientKeyKey = tlsClientKeyKey;
  }

  public String getTLSCACERTKey() {
    return tlsCACERTKey;
  }

  public void setTLSCACERTKey(String tlsCACERTKey) {
    this.tlsCACERTKey = tlsCACERTKey;
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

  public String getAdmin_user() {
    return admin_user;
  }

  public void setAdmin_user(String admin_user) {
    this.admin_user = admin_user;
  }

  public String getAdmin_password() {
    return admin_password;
  }

  public void setAdmin_password(String admin_password) {
    this.admin_password = admin_password;
  }

  public Sentinel getSentinel() {
    return sentinel;
  }

  public void setSentinel(Sentinel sentinel) {
    this.sentinel = sentinel;
  }

  public TLS getTLS() {
    return tls;
  }

  public void setTLS(TLS tls) {
    this.tls = tls;
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

  public static class TLS {

    private Integer port = null;

    private String certificate = null;

    private String private_key = null;

    private String ca = null;

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public String getCertificate() {
      return certificate;
    }

    public void setCertificate(String certificate) {
      this.certificate = certificate;
    }

    public String getPrivate_key() {
      return private_key;
    }

    public void setPrivate_key(String private_key) {
      this.private_key = private_key;
    }

    public String getCa() {
      return ca;
    }

    public void setCa(String ca) {
      this.ca = ca;
    }

    public boolean isEmpty() {
      return port == null && clientCERT == null && clientKey == null && caCERT == null;
    }
  }

  public Map<String, Object> toMap() {
    Map<String, Object> credentials = new HashMap<>();
    String servers = new String();
    for (InetAddress address : getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    credentials.put(getIPKey(), servers);
    logger.info(getIPKey().concat(" ").concat(servers));
    credentials.put(getPortKey(), getPort().toString());
    logger.info(getPortKey().concat(" ").concat(getPort().toString()));
    credentials.put(getPasswordKey(), getPassword());
    logger.info(getPasswordKey().concat(" ").concat(getPassword()));
    credentials.put(getAdminUserKey(), getAdmin_user());
    logger.info(getAdminUserKey().concat(" ").concat(getAdmin_user()));
    credentials.put(getAdminPasswordKey(), getAdmin_password());
    logger.info(getAdminPasswordKey().concat(" ").concat(getAdmin_password()));
    if (!getSentinel().isEmpty()) {
      credentials.put(getHAIPKey(), getSentinel().getMasterName());
      logger.info(getHAIPKey().concat(" ").concat(getSentinel().getMasterName()));
      credentials.put(getHAPortKey(), getSentinel().getPort().toString());
      logger.info(getHAPortKey().concat(" ").concat(getSentinel().getPort().toString()));
      credentials.put(getHAPasswordKey(), getSentinel().getPassword());
      logger.info(getHAPasswordKey().concat(" ").concat(getSentinel().getPassword()));
    }
    if (!getTLS().isEmpty()) {
      credentials.put(getTLSPortKey(), getTLS().getPort().toString());
      logger.info(getTLSPortKey().concat(" ").concat(getTLS().getPort().toString()));
      credentials.put(getTLSClientCERTKey(), getTLS().getCertificate());
      logger.info(getTLSClientCERTKey().concat(" ").concat(getTLS().getCertificate()));
      credentials.put(getTLSClientKeyKey(), getTLS().getPrivate_key());
      logger.info(getTLSClientKeyKey().concat(" ").concat(getTLS().getPrivate_key()));
      credentials.put(getTLSCACERTKey(), getTLS().getCa());
      logger.info(getTLSCACERTKey().concat(" ").concat(getTLS().getCa()));
    }
    return credentials;
  }
}
