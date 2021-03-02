## redis-orange 1.0.3.1
- Added:
  - pre-start, post-start and pre-stop in Redis job,
  - pre-start and pre-stop to Redis Sentinel job
  
  to support BOSH recreate to support Redis high availability with Redis Sentinel and Redis Cluster high availability. Your deployment must be serial and having a persistent storage to support this feature.
- Components update:
  - [*Redis*](https://redis.io/) 6.0.9 -> 6.0.11,
  - [*redis_exporter*](https://github.com/oliver006/redis_exporter) 1.12.1 -> 1.17.1,
  - [*OpenJDK*](https://openjdk.java.net/) 15.0.1 -> 15.0.2,
  - [*Spring Boot*](https://spring.io/projects/spring-boot) 2.3.5 -> 2.4.3,
  - [*Spring Cloud Open Service Broker*](https://spring.io/projects/spring-cloud-open-service-broker) 3.1.2 -> 3.3.0,
  - [*havegd*](https://www.issihosts.com/haveged/) 1.9.13 -> 1.9.14.

## redis-orange 1.0.3

- Added features introduced in Redis 6.x:
  - TSL/SSL
  - ACL
  - Multi-threading
- Added [*havegd*](https://www.issihosts.com/haveged/) [*1.9.13*](https://github.com/jirka-h/haveged/releases/tag/v1.9.13) to provide a better unpredictable random number generator.
- Added support for reconfiguring Sentinel at runtime to share Sentinels with several Redis masters.
- Components update:
  - [*Redis*](https://redis.io/) 5.0.7 -> 6.0.9,
  - [*redis_exporter*](https://github.com/oliver006/redis_exporter) 1.3.4 -> 1.12.1,
  - [*redis_sentinel_exporter*](https://github.com/leominov/redis_sentinel_exporter) 1.3.0 -> 1.7.1,
  - [*OpenJDK*](https://openjdk.java.net/) 13.0.2 -> 15.0.1,
  - [*Spring Boot*](https://spring.io/projects/spring-boot) 2.2.2 -> 2.3.5,
  - [*Spring Cloud Open Service Broker*](https://spring.io/projects/spring-cloud-open-service-broker) 3.1.0 -> 3.1.2.

## redis-orange 1.0.2.1

- Correction for misunderstanding about Sentinel's quorum by adding `max_detected_failures` property in Redis's specifications. It's default value is 2.

## redis-orange 1.0.2

- Refine Redis memory usage by `maxmemory` property and memory eviction policies.
- Delay automatic restart to avoid persistence on disk to deal with the issue:
> [If you are using replication, make sure that either your master has persistence enabled, or that it does not automatically restarts on crashes: replicas will try to be an exact copy of the master, so if a master restarts with an empty data set, replicas will be wiped as well.](https://redis.io/topics/admin)
- Add redis_sentinel_exporter to monitor Redis Sentinel with Prometheus.
- RDB persistence was enabled by default. Since this release, no persistence (i.e. no RDB neither AOF)is the default and, by default, diskless is used for replication.
- `CONFIG REWRITE failed: Permission denied` in Redis Sentinel or Redis Cluster with High Availability is fixed.
- Update [*Spring Boot*](https://spring.io/projects/spring-boot) 2.2.1 -> 2.2.2.
- Update [*Open JDK*](https://jdk.java.net/) 13.0.1 -> 13.0.2.

## redis-orange 1.0.1

Components update:
- [*Redis*](https://redis.io/) 5.0.5 -> 5.0.7
- [*OpenJDK*](https://openjdk.java.net/) 12.0.1 -> 13.0.1
- [*redis_exporter*](https://github.com/oliver006/redis_exporter) 1.0.3 -> 1.3.4
- [*Spring Boot*](https://spring.io/projects/spring-boot) 2.1.6 -> 2.2.1 and 1.5.21 -> 1.5.22
- [*Spring Cloud Open Service Broker*](https://spring.io/projects/spring-cloud-open-service-broker) 3.0.3 -> 3.1.0
- [*Spring Cloud - Cloud Foundry Service Broker*](https://spring.io/projects/spring-cloud-cloudfoundry-service-broker) 1.0.3 -> 1.0.4

## redis-orange 1.0

Initial release