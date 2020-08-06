package com.orange.redis.service.broker.model;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.net.InetAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
    private String keys_dir = null;
    private String ca_cert_file = null;
    private String ca_key_file = null;
    private Integer client_key_length = null;
    private String client_cert_ou = null;
    private Integer client_cert_duration = null;

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

    public String getKeys_dir() {
      return keys_dir;
    }

    public void setKeys_dir(String keys_dir) {
      this.keys_dir = keys_dir;
    }

    public String getCa_cert_file() {
      return ca_cert_file;
    }

    public void setCa_cert_file(String ca_cert_file) {
      this.ca_cert_file = ca_cert_file;
    }

    public String getCa_key_file() {
      return ca_key_file;
    }

    public void setCa_key_file(String ca_key_file) {
      this.ca_key_file = ca_key_file;
    }

    public Integer getClient_key_length() {
      return client_key_length;
    }

    public void setClient_key_length(Integer client_key_length) {
      this.client_key_length = client_key_length;
    }

    public String getClient_cert_ou() {
      return client_cert_ou;
    }

    public void setClient_cert_ou(String client_cert_ou) {
      this.client_cert_ou = client_cert_ou;
    }

    public Integer getClient_cert_duration() {
      return client_cert_duration;
    }

    public void setClient_cert_duration(Integer client_cert_duration) {
      this.client_cert_duration = client_cert_duration;
    }

    public boolean isEmpty() {
      return (port == null || ha_port == null) && keys_dir == null
          && ca_cert_file == null && ca_key_file == null
          && client_key_length == null && client_cert_ou == null
          && client_cert_duration == null;
    }

    public static class KeyPair {
      public String certificate = null;
      public String private_key = null;

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
    }

    private static class StreamGobbler implements Runnable {
      private InputStream inputStream;
      private Consumer<String> consumer;

      public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
        this.inputStream = inputStream;
        this.consumer = consumer;
      }

      @Override
      public void run() {
        new BufferedReader(new InputStreamReader(inputStream)).lines()
            .forEach(consumer);
      }
    }

    public static KeyPair generateKeyPair(final TLS tls) {
      KeyPair keyPair = null;
      ProcessBuilder processBuilder = new ProcessBuilder();
      Map<String, String> processBuilder_env = processBuilder.environment();
      Process process = null;
      StreamGobbler streamGobbler = null;
      String ca_cert_filename = tls.getKeys_dir().concat(File.separator)
          .concat(tls.getCa_cert_file());
      String ca_private_key_filename = tls.getKeys_dir().concat(File.separator)
          .concat(tls.getCa_key_file());
      String ca_serial_file = tls.getKeys_dir().concat(File.separator)
          .concat("ca.txt");
      String subject = new String("\"/O=orange.com/OU=")
          .concat(tls.getClient_cert_ou()).concat("/CN=Redis Client\"");
      String prefix = tls.getKeys_dir().concat(File.separator).concat("client-")
          .concat(String.valueOf(ThreadLocalRandom.current().nextInt()));
      String private_key_filename = null;
      String cert_filename = null;
      private_key_filename = prefix.concat(".key");
      cert_filename = prefix.concat(".crt");
      processBuilder.command("bash", "-c",
          new String("openssl genrsa -out " + private_key_filename + " "
              + String.valueOf(tls.getClient_key_length())
              + " && dd if=/dev/urandom of=$RANDFILE bs="
              + String.valueOf(tls.getClient_key_length() / 8) + " count=1 && "
              + "openssl req -new -sha256 -key " + private_key_filename
              + " -subj " + subject + " | " + "openssl x509 -req -sha256 -CA "
              + ca_cert_filename + " -CAkey " + ca_private_key_filename
              + " -CAserial " + ca_serial_file + " -CAcreateserial -days "
              + String.valueOf(tls.getClient_cert_duration()) + " -out "
              + cert_filename));
      processBuilder_env.put("RANDFILE",
          System.getProperty("user.home") + File.separator + ".rnd");
      try {
        process = processBuilder.start();
        streamGobbler = new StreamGobbler(process.getInputStream(),
            System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        if (process.waitFor() == 0) {
          keyPair = new KeyPair();
          keyPair.setPrivate_key(
              Files.readString(Paths.get(private_key_filename)));
          keyPair.setCertificate(Files.readString(Paths.get(cert_filename)));
        }
        Files.deleteIfExists(Paths.get(private_key_filename));
        Files.deleteIfExists(Paths.get(cert_filename));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return keyPair;
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
    logger
        .info(getAdmin_password_key().concat(" ").concat(getAdmin_password()));
    if (!getSentinel().isEmpty()) {
      credentials.put(getHa_master_name_key(), getSentinel().getMaster_name());
      logger.info(getHa_master_name_key().concat(" ")
          .concat(getSentinel().getMaster_name()));
      credentials.put(getHa_port_key(), getSentinel().getPort().toString());
      logger.info(getHa_port_key().concat(" ")
          .concat(getSentinel().getPort().toString()));
      credentials.put(getHa_password_key(), getSentinel().getPassword());
      logger.info(
          getHa_password_key().concat(" ").concat(getSentinel().getPassword()));
    }
    if (!getTls().isEmpty()) {
      credentials.put(getTls_port_key(), getTls().getPort().toString());
      logger.info(
          getTls_port_key().concat(" ").concat(getTls().getPort().toString()));
      if (getTls().getHa_port() != null) {
        credentials.put(getTls_ha_port_key(), getTls().getHa_port().toString());
        logger.info(getTls_ha_port_key().concat(" ")
            .concat(getTls().getHa_port().toString()));
      }
      TLS.KeyPair keyPair = TLS.generateKeyPair(getTls());
      String ca = null;
      credentials.put(getTls_certificate_key(), keyPair.getCertificate());
      logger.info(getTls_certificate_key().concat(" ")
          .concat(keyPair.getCertificate()));
      credentials.put(getTls_private_key_key(), keyPair.getPrivate_key());
      logger.info(getTls_private_key_key().concat(" ")
          .concat(keyPair.getPrivate_key()));
      try {
        ca = Files.readString(
            Paths.get(getTls().getKeys_dir(), getTls().getCa_cert_file()));
        credentials.put(getTls_ca_key(), ca);
        logger.info(getTls_ca_key().concat(" ").concat(ca));
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    return credentials;
  }
}
