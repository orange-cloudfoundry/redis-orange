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
  private String ip_key;
  @NotNull
  private String port_key;
  @NotNull
  private String password_key;
  @NotNull
  private String admin_user_key;
  @NotNull
  private String admin_password_key;
  @NotNull
  private String ha_master_name_key;
  @NotNull
  private String ha_port_key;
  @NotNull
  private String ha_password_key;
  @NotNull
  private String tls_port_key;
  @NotNull
  private String tls_ha_port_key;
  @NotNull
  private String tls_certificate_key;
  @NotNull
  private String tls_private_key_key;
  @NotNull
  private String tls_ca_key;
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

  public String getIp_key() {
    return ip_key;
  }

  public void setIp_key(String ip_key) {
    this.ip_key = ip_key;
  }

  public String getPort_key() {
    return port_key;
  }

  public void setPort_key(String port_key) {
    this.port_key = port_key;
  }

  public String getPassword_key() {
    return password_key;
  }

  public void setPassword_key(String password_key) {
    this.password_key = password_key;
  }

  public String getAdmin_user_key() {
    return admin_user_key;
  }

  public void setAdmin_user_key(String admin_user_key) {
    this.admin_user_key = admin_user_key;
  }

  public String getAdmin_password_key() {
    return admin_password_key;
  }

  public void setAdmin_password_key(String admin_password_key) {
    this.admin_password_key = admin_password_key;
  }

  public String getHa_master_name_key() {
    return ha_master_name_key;
  }

  public void setHa_master_name_key(String ha_ip_key) {
    this.ha_master_name_key = ha_ip_key;
  }

  public String getHa_port_key() {
    return ha_port_key;
  }

  public void setHa_port_key(String ha_port_key) {
    this.ha_port_key = ha_port_key;
  }

  public String getHa_password_key() {
    return ha_password_key;
  }

  public void setHa_password_key(String ha_password_key) {
    this.ha_password_key = ha_password_key;
  }

  public String getTls_port_key() {
    return tls_port_key;
  }

  public void setTls_port_key(String tls_port_key) {
    this.tls_port_key = tls_port_key;
  }

  public String getTls_ha_port_key() {
    return tls_ha_port_key;
  }

  public void setTls_ha_port_key(String tls_ha_port_key) {
    this.tls_ha_port_key = tls_ha_port_key;
  }

  public String getTls_certificate_key() {
    return tls_certificate_key;
  }

  public void setTls_certificate_key(String tls_certificate_Key) {
    this.tls_certificate_key = tls_certificate_Key;
  }

  public String getTls_private_key_key() {
    return tls_private_key_key;
  }

  public void setTls_private_key_key(String tls_private_key_Key) {
    this.tls_private_key_key = tls_private_key_Key;
  }

  public String getTls_ca_key() {
    return tls_ca_key;
  }

  public void setTls_ca_key(String tls_ca_key) {
    this.tls_ca_key = tls_ca_key;
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

  public TLS getTls() {
    return tls;
  }

  public void setTls(TLS tls) {
    this.tls = tls;
  }

  public static class Sentinel {

    private String master_name = null;

    private Integer port = null;

    private String password = null;

    public String getMaster_name() {
      return master_name;
    }

    public void setMaster_name(String master_name) {
      this.master_name = master_name;
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
      return master_name == null && port == null && password == null;
    }
  }

  public static class TLS {

    private Integer port = null;

    private Integer ha_port = null;

    private String certificate = null;

    private String private_key = null;

    private String ca = null;

    public Integer getPort() {
      return port;
    }

    public void setPort(Integer port) {
      this.port = port;
    }

    public Integer getHa_port() {
      return ha_port;
    }

    public void setHa_port(Integer ha_port) {
      this.ha_port = ha_port;
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
      return (port == null || ha_port == null) && certificate == null && private_key == null && ca == null;
    }
  }

  public Map<String, Object> toMap() {
    Map<String, Object> credentials = new HashMap<>();
    String servers = new String();
    for (InetAddress address : getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    credentials.put(getIp_key(), servers);
    logger.info(getIp_key().concat(" ").concat(servers));
    credentials.put(getPort_key(), getPort().toString());
    logger.info(getPort_key().concat(" ").concat(getPort().toString()));
    credentials.put(getPassword_key(), getPassword());
    logger.info(getPassword_key().concat(" ").concat(getPassword()));
    credentials.put(getAdmin_user_key(), getAdmin_user());
    logger.info(getAdmin_user_key().concat(" ").concat(getAdmin_user()));
    credentials.put(getAdmin_password_key(), getAdmin_password());
    logger.info(getAdmin_password_key().concat(" ").concat(getAdmin_password()));
    if (!getSentinel().isEmpty()) {
      credentials.put(getHa_master_name_key(), getSentinel().getMaster_name());
      logger.info(getHa_master_name_key().concat(" ").concat(getSentinel().getMaster_name()));
      credentials.put(getHa_port_key(), getSentinel().getPort().toString());
      logger.info(getHa_port_key().concat(" ").concat(getSentinel().getPort().toString()));
      credentials.put(getHa_password_key(), getSentinel().getPassword());
      logger.info(getHa_password_key().concat(" ").concat(getSentinel().getPassword()));
    }
    if (!getTls().isEmpty()) {
      credentials.put(getTls_port_key(), getTls().getPort().toString());
      logger.info(getTls_port_key().concat(" ").concat(getTls().getPort().toString()));
      if (getTls().getHa_port() != null) {
        credentials.put(getTls_ha_port_key(), getTls().getHa_port().toString());
        logger.info(getTls_ha_port_key().concat(" ").concat(getTls().getHa_port().toString()));
      }
      credentials.put(getTls_certificate_key(), getTls().getCertificate());
      logger.info(getTls_certificate_key().concat(" ").concat(getTls().getCertificate()));
      credentials.put(getTls_private_key_key(), getTls().getPrivate_key());
      logger.info(getTls_private_key_key().concat(" ").concat(getTls().getPrivate_key()));
      credentials.put(getTls_ca_key(), getTls().getCa());
      logger.info(getTls_ca_key().concat(" ").concat(getTls().getCa()));
    }
    return credentials;
  }
}
