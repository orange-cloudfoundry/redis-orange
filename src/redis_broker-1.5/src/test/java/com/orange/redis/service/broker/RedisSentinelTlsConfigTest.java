package com.orange.redis.service.broker;

import java.net.InetAddress;

import com.orange.redis.service.broker.model.RedisConfig;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("sentinel-tls")
public class RedisSentinelTlsConfigTest {
  @Autowired
  private RedisConfig redisConfig;

  @Test
  public void RedisSentinel() {
    String servers = new String();
    String newLine = System.getProperty("line.separator");
    String certificate = String.join(newLine,
        "-----BEGIN CERTIFICATE-----",
        "MIID4jCCAcoCFFEXfvMQuyMWfZcYX0nlal62wTEOMA0GCSqGSIb3DQEBCwUAMDUx",
        "EzARBgNVBAoMClJlZGlzIFRlc3QxHjAcBgNVBAMMFUNlcnRpZmljYXRlIEF1dGhv",
        "cml0eTAeFw0yMDA4MjExOTIxMTRaFw0yMTA4MjExOTIxMTRaMCYxEzARBgNVBAoM",
        "ClJlZGlzIFRlc3QxDzANBgNVBAMMBkNsaWVudDCCASIwDQYJKoZIhvcNAQEBBQAD",
        "ggEPADCCAQoCggEBAKUQa0iBA9b4y4eTDaMtDFPjR/X2qZVSNkGydZK+ffQhbm8v",
        "2RmsvjYirnZVGBAGbIKuBNGmPjAwrpGvmkSzeGveOsboa5HAnsucWWu54oopE6ok",
        "IKK/gJGBFGN4QbXpAyq2UD7fsSWqtvkf2KNvuuGiIVP57ZxX6pQzIDYpt8fd72yU",
        "TlFdYxMhFyxVMKWEfsc4n8qo4EZnD3MCJTmcW1M3BT4tKm8y2ti/9sSr6TxfUqVt",
        "czS1M3QSgKoFYfZMaNwm0DRNvcmvoqU8prybHTfnvY0ANgzZp3DWwwhdhZFi2jzE",
        "6xo5Pqxy+T0Ag3lP68Sn9Ayvi3ePeeiyT741vEsCAwEAATANBgkqhkiG9w0BAQsF",
        "AAOCAgEAEVQCO1k1OiGzjF3uLmyFhDMDzziwMXV+ReW+dSYpxQufHvynnGPrA8mz",
        "EQIwP9WJDGaZBeM7pYLFr7yY+potEPyG4tmTT+5MFpZMek3TqMMRkmaau3xieyT7",
        "Ix1Z7VMazF8WcM5gkirWGKnYrta4sJKxUIhXUMCK5w52OwRzy4kjxyYUnAferYes",
        "3otZGFZAYh/6dmkPPh66LlUkAChu3/r913nNABy6akBW60IAP2pX4tIKALJC6I5n",
        "8HI2Z+Lp8bIlYG5Q3hr3YrvaQJ0EHmiAZnw7i/kR0QWCaC3Dt+fvO6YKOqHG/Jic",
        "NShaSWCk3uD85QLH00eHSVPi7Tpc4JrgV5JWgvVuYKJ5PFC4+vytgJjWR53CzkD7",
        "gpdEVSFiQtov7vwZbzKhF1SE1HC9kMhzSmpPFQyFV1Z4zSPVJQiujCXgb0ZssBgB",
        "ZktSr1TQRIaLX42OH4wWKeYJuGotu73BavTfigUH46fqig4rmgtX6R7P3dFmmw5o",
        "YhDp8t4UNXBgLwXILzcZC/YPTQVF6Flst7DbJYRffGyW3DJqDYukapc8nMuBsuwL",
        "v9YJov9EACImJbavxSpblVuHyLacvGjm1X2KongGagdaZzfwni/g5WR/5urnTn7K",
        "4wvQQq1jwqMbhUFmyFQWQ6w4EYX1MF9I5ylME6JcyS2y6i1yBmE=",
        "-----END CERTIFICATE-----",
        "");
    String private_key = String.join(newLine,
        "-----BEGIN RSA PRIVATE KEY-----",
        "MIIEowIBAAKCAQEApRBrSIED1vjLh5MNoy0MU+NH9faplVI2QbJ1kr599CFuby/Z",
        "Gay+NiKudlUYEAZsgq4E0aY+MDCuka+aRLN4a946xuhrkcCey5xZa7niiikTqiQg",
        "or+AkYEUY3hBtekDKrZQPt+xJaq2+R/Yo2+64aIhU/ntnFfqlDMgNim3x93vbJRO",
        "UV1jEyEXLFUwpYR+xzifyqjgRmcPcwIlOZxbUzcFPi0qbzLa2L/2xKvpPF9SpW1z",
        "NLUzdBKAqgVh9kxo3CbQNE29ya+ipTymvJsdN+e9jQA2DNmncNbDCF2FkWLaPMTr",
        "Gjk+rHL5PQCDeU/rxKf0DK+Ld4956LJPvjW8SwIDAQABAoIBADhd/wTnadRDad2G",
        "TiXnAEq1VqGb9hVT2ctdkeDmxERWFkxJ3q1x3BLIjSNDs8/lIbEOk/6z8pCqTzLY",
        "8Kl9UCYl4ZSDUhiX2qJVZgPvmnjrxhdX9v0cwF7v4XKGYoooZDh1wbl20hu2zH0O",
        "bwCA6ySaJR90PbwiGa06OEnGvUe26Y/EP8oLDZmyJBxOpmOUuS5BChjabJnhwKus",
        "kxPoYB5G61NEZ4GR8OJ4FxNEMW+YPVgumMsn5K/O0EuCwT7n3dS3a3TX/k5K2+9o",
        "aZpsvwaiftX70HJDYw6PlygksdZys69m3c5Q25SzrNQziJuD+rxhi2t9XMco9nMk",
        "pMD7/6ECgYEA2/181JFFMKc2S6KgLkHsZrM6zYIoh/EVKEhX7bPxjtLWg231HRPD",
        "ZyvDhcujjoPcIVkjICVjQlGYbFq33p3IjXnr6PeyR99+5ibl2UdqWlYmsXbUp1B7",
        "BrOdSEBLUl0/OVxmQ9UTfDUzNsljXJkaDW7r9eg5XEw0w/XWBb+fXAUCgYEAwBVM",
        "pexxyiMT9DtjcV2QiSaWo7qEZo9Dx/24GE/GHUZTINVNBaXRELPiTCWysVZwpqe1",
        "s1q3eXFRlbl3L/X91OzyA2s88LIJFj05vIqrI8Cn3XTmEs6gup1B1UnJ4l04mwqe",
        "SPSKz6Ad7dQy5jBd4OSf2wlR5bn0BI4Gy/dZeA8CgYAX2X5sCSfII4imYIYfHbSE",
        "Wc/5MbA1NLTHfjhSI9z/T+pDq2sUcO0RRVRYMACWVcNKtkAxnvwI15G/xYIDl3WD",
        "qQEja58fXaKDTxpCGRT1oDtnp+tcDESaMGTPzXCdCFDvZg4MMpB/OcFIokKsakjB",
        "xhG8wiVd1UEvMXq3etPLBQKBgGv+zbRwRt3ecQadKWEAuRXTg1iUsoIzRGm6Bfpt",
        "R7cH5g8MvhxRyUS5zp7/hwNb1URuAKPRelymULd8qj1l8gv4GG3BqmatwpwlWkPe",
        "NzCHzhmd8wlZ8VZaujvgG8jQntXU13+vNihcHterud5gDTg3coUlLU2bCk5RSBL/",
        "g+cTAoGBANW/edyQiuaTUxTL6mU0VhQWTwSXZOqPV5XW4+GPj7PYWUajjLOyOsNV",
        "78BKFuXQv0+6ni3SdaeImxGdSL7HSD7XeSQft6wl1t0M4EM/9NwPzOnxj5Ge1CCh",
        "8bbEWR0Y29lyFf7dI2mPCFQZuAksMKr/ulwdvzHjL1tVYvI73oVw",
        "-----END RSA PRIVATE KEY-----",
        "");
    String ca = String.join(newLine,
        "-----BEGIN CERTIFICATE-----",
        "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",
        "5gtIRle/3xCIHAdA4LL6",
        "-----END CERTIFICATE-----",
        "");
    for (InetAddress address : redisConfig.getServers())
      servers = servers.concat(address.getHostAddress()).concat(" ");
    servers = servers.trim();
    Assert.assertEquals("192.168.56.101 192.168.56.102", servers);
    Assert.assertEquals("0", redisConfig.getPort().toString());
    Assert.assertEquals("redis_secret", redisConfig.getPassword());
    Assert.assertEquals("admin", redisConfig.getAdmin_user());
    Assert.assertEquals("admin_secret", redisConfig.getAdmin_password());
    Assert.assertFalse(redisConfig.getSentinel().isEmpty());
    Assert.assertEquals("master", redisConfig.getSentinel().getMaster_name());
    Assert.assertEquals("0", redisConfig.getSentinel().getPort().toString());
    Assert.assertEquals("redis_sentinel_secret", redisConfig.getSentinel().getPassword());
    Assert.assertFalse(redisConfig.getTls().isEmpty());
    Assert.assertEquals("6379", redisConfig.getTls().getPort().toString());
    Assert.assertEquals("26379", redisConfig.getTls().getHa_port().toString());
    Assert.assertEquals(certificate, redisConfig.getTls().getCertificate());
    Assert.assertEquals(private_key, redisConfig.getTls().getPrivate_key());
    Assert.assertEquals(ca, redisConfig.getTls().getCa());
  }
}
