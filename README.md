# redis-orange

A [*Redis*](https://redis.io/) release for Cloud Foundry

## Features

- High availability by [*Redis Sentinel*](https://redis.io/topics/sentinel),
- [*Redis cluster*](https://redis.io/topics/cluster-spec) with high availability,
- Multi-zone for high availability by Redis Sentinel and Redis cluster with high availability,
- Monitoring by [*Prometheus*](https://prometheus.io/)/[*Grafana*](https://grafana.com/),
- An Open Service Broker with [*Spring Boot*](https://spring.io/projects/spring-boot) 2.2.2 and [*Spring Cloud Open Service Broker*](https://spring.io/projects/spring-cloud-open-service-broker) 3.1.0 and another broker with *Spring Boot* 1.5.22 and [*Spring Cloud - Cloud Foundry Service Broker*](https://spring.io/projects/spring-cloud-cloudfoundry-service-broker) 1.0.4.

## TODO

- Backup/restore,
- Logging.

## Packages versions summary

- Redis [*5.0.10*](https://download.redis.io/releases/redis-5.0.10.tar.gz)
- [*redis_exporter*](https://github.com/oliver006/redis_exporter) [*1.16.0*](https://github.com/oliver006/redis_exporter/releases/download/v1.16.0/redis_exporter-v1.16.0.linux-amd64.tar.gz)
- [*redis_sentinel_exporter*](https://github.com/leominov/redis_sentinel_exporter) [*1.7.1*](https://github.com/leominov/redis_sentinel_exporter/releases/download/v1.7.1/redis_sentinel_exporter-1.7.1.linux-amd64.tar.gz)
- [*OpenJDK*](https://openjdk.java.net/) [*13.0.2*](https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz)
- [*utils.sh*](https://github.com/bosh-prometheus/prometheus-boshrelease/blob/master/src/common/utils.sh)

## Memory Management

In default configuration, Redis has no memory limit. This is the default behavior for 64 bit systems, while 32 bit systems use an implicit memory limit of 3GB. No memory limit means:

- If the operating system has swap, when memory runs out the operating system's virtual memory starts to get used up (i.e. swap), and performance drops tremendously.
- If operating system does not have swap and your Redis instance accidentally consumes too much memory, either Redis will crash for out of memory or the operating system kernel OOM killer will kill the Redis process.

**Note**: Since Redis 2.0, Redis has a [*Virtual Memory*](https://redis.io/topics/) feature; Redis Virtual Memory is deprecated since 2.4.

When Redis has no memory limit,
> [Make sure to setup some swap in your system (we suggest as much as swap as memory). If Linux does not have swap and your Redis instance accidentally consumes too much memory, either Redis will crash for out of memory or the Linux kernel OOM killer will kill the Redis process. When swapping is enabled Redis will work in a bad way, but you'll likely notice the latency spikes and do something before it's too late.](https://redis.io/topics/admin)

In order to configure Redis to use a specified amount of memory for the data set, use the `maxmemory` property. Setting `maxmemory` to zero results into no memory limits. **It's its default value in this release**.

**Note**: If you plan to use [*Grafana*](https://grafana.com/) and let `maxmemory` to zero, the *Memory Usage* metric is useless.

You should,
> [Set an explicit `maxmemory` option limit in your instance in order to make sure that the instance will report errors instead of failing when the system memory limit is near to be reached. Note that `maxmemory` should be set calculating the overhead that Redis has, other than data, and the fragmentation overhead. So if you think you have 10 GB of free memory, set it to 8 or 9.](https://redis.io/topics/admin)

When you specify an amount of memory for Redis (by setting `maxmemory` property with an integer greater than zero) and there is no more room for data, it is possible to select among different behaviors, called **policies**. Redis can just return errors for commands that could result in more memory being used, or it can evict some old data in order to return back to the specified limit every time new data is added.

**Note**: To simplify Redis synchronizations, all Redis instances have the same `maxmemory` value. So, in yours deployments, insure that all Redis hosts have an amount of memory greater than `maxmemory` value.

### Eviction Policies

The exact behavior Redis follows when the `maxmemory` limit is reached is configured using the `maxmemory_policy` property.

The following policies are available:
> - **noeviction**: return errors when the memory limit was reached and the client is trying to execute commands that could result in more memory to be used (most write commands, but DEL and a few more exceptions).
> - **allkeys-lru**: evict keys by trying to remove the less recently used (LRU) keys first, in order to make space for the new data added.
> - **volatile-lru**: evict keys by trying to remove the less recently used (LRU) keys first, but only among keys that have an expire set, in order to make space for the new data added.
> - **allkeys-lfu**: Evict any key using approximated LFU.
> - **volatile-lfu**: Evict using approximated LFU among the keys with an expire set.
> - **allkeys-random**: evict keys randomly in order to make space for the new data added.
> - **volatile-random**: evict keys randomly in order to make space for the new data added, but only evict keys with an expire set.
> - **volatile-ttl**: evict keys with an expire set, and try to evict keys with a shorter time to live (TTL) first, in order to make space for the new data added.

Details about Redis evictions policies, specialy for LRU (Last Recently Used) and LFU (Last Frequently Used), are here [*Using Redis as an LRU cache*](https://redis.io/topics/lru-cache)

In this release, the default `maxmemory_policy` is `noeviction`. So Redis users must manage themself their data when there is no more room for Redis to store data.

**Note**:
- **volatile-lru**, **volatile-lfu**, **volatile-random** and **volatile-ttl** policies requiert an **expire set** and behave like **noeviction** if there are no keys to evict matching the prerequisites.
- > [It is also worth noting that setting an expire to a key costs memory, so using policies like **allkeys-lru** or **allkeys-lfu** are more memory efficient since there is no need to set an expire for the key to be evicted under memory pressure.](https://redis.io/topics/lru-cache)
- Redis LRU algorithm is not an exact implementation. This means that Redis is not able to pick the best candidate for eviction, that is, the access that was accessed the most in the past. Instead it will try to run an approximation of the LRU algorithm, by sampling a small number of keys, and evicting the one that is the best (with the oldest access time) among the sampled keys. You can tune the precision of the Redis LRU algorithm by changing the number of samples to check for every eviction. This parameter is controlled by the `maxmemory_samples` (default: 5) property. You can tune `maxmemory_samples` for speed or accuracy. 10 Approximates very closely true LRU but costs more CPU. 3 is faster but not very accurate.
- Starting with Redis 4.0, a new [*Least Frequently Used eviction mode*](http://antirez.com/news/109) is available. This mode may work better (provide a better hits/misses ratio) in certain cases, since using LFU Redis will try to track the frequency of access of items, so that the ones used rarely are evicted while the one used often have an higher chance of remaining in memory. LFU is approximated like LRU: it uses a probabilistic counter, called a [*Morris counter*](https://en.wikipedia.org/wiki/Approximate_counting_algorithm) in order to estimate the object access frequency using just a few bits per object, combined with a decay period so that the counter is reduced over time: at some point we no longer want to consider keys as frequently accessed, even if they were in the past, so that the algorithm can adapt to a shift in the access pattern. LFU has certain tunable parameters:
  - **lfu_log_factor** (default: 10): it changes how many hits are needed in order to saturate the frequency counter, which is just in the range 0-255. The higher the factor, the more accesses are needed in order to reach the maximum. The lower the factor, the better is the resolution of the counter for low accesses.
  - **lfu_decay_time** (default: 1): it is the amount of minutes a counter should be decayed, when sampled and found to be older than that value. A special value of 0 means: always decay the counter every time is scanned, and is rarely useful.

  Instructions about how to tune **lfu_log_factor** and **lfu_decay_time** can be found inside the example `redis.conf` file in the source distribution.

## Replication and automatic restart

To address the following issue:
> [If you are using replication, make sure that either your master has persistence enabled, or that it does not automatically restarts on crashes: replicas will try to be an exact copy of the master, so if a master restarts with an empty data set, replicas will be wiped as well.](https://redis.io/topics/admin)

To avoid persistence and to maintain automatic restart, we add a delay before starting Redis instances when high availability is used (i.e. when you use replications in Redis Sentinel or Redis Cluster). The value of the delay is:
- For Redis Sentinel High Availability: `(3*down_after_milliseconds)/2` milliseconds,
- For Redis Cluster with High Availability: `(3*cluster_node_timeout)/2` milliseconds.

**Notes**:
- By default, for obvious performance reasons, we don't use persistence, so we use the new diskless replication feature as it was recommended:
  > [Even if you have persistence disabled, Redis will need to perform RDB saves if you use replication, unless you use the new diskless replication feature. If you have no disk usage on the master, make sure to enable diskless replication.](https://redis.io/topics/admin)
- If you use persistence, the start delay is avoided.

## Usage

### Clone the repository

```shell
git clone https://github.com/orange-cloudfoundry/redis-orange
```

### Create and upload release

```shell
bosh create-release --force
bosh upload-release
```

### Deploy

#### Single Redis Server

A deployment example manifest is: [redis.yml](manifests-1.5/redis.yml)

With the following example variables file: [redis.yml](secrets/redis.yml)

**Notes**:
  - For security purposes, we use [*CredHub*](https://docs.cloudfoundry.org/credhub/) for passwords and to obfuscate some Redis admin commands (e.g.: `CONFIG`, `DEBUG`), etc..
  - We provide two errands:
    - `redis_check` to test deployed Redis instance with create, read, write and delete operations,
    - `redis_broker_check` to test deployed Redis broker by accessing Redis's catalog and service instance binding.

#### Redis High Availability with Redis Sentinel

We use a simple setup: each instance runs both a Redis process and a Sentinel process. For example:

```
       +----+
       | M1 |
       | S1 |
       +----+
          |
+----+    |    +----+
| R2 |----+----| R3 |
| S2 |         | S3 |
+----+         +----+

Configuration: quorum = 2
```

Where `M1` is the Redis master, `R2` and `R3` are Redis slaves, and `S1`, `S2` and `S3` are Redis Sentinel.

This release supports at most two kinds of instance groups:
- **master group**, this group is mandatory and include at last Redis master, other instances are Redis slaves. In our release, at start-up, the bootstrap instance is associated with the Redis master process. We use the parameter `master_node_count` as the number of instance in this group.
- **slave group**, this group is optional and is useful if you plan to set Redis process in master group in an distinct AZ than Redis process in slave group. We use the parameter `slave_node_count` as the number of instance in this group.

**Notes**:
- The **quorum** is set by the `max_detected_failures` (default: `2`) property in Redis instance. [Some precisions about Redis **quorum**](https://redis.io/topics/sentinel):
  > - The **quorum** is the number of Sentinels that need to agree about the fact the master is not reachable, in order to really mark the master as failing, and eventually start a failover procedure if possible.
  > - However **the quorum is only used to detect the failure**. In order to actually perform a failover, one of the Sentinels need to be elected leader for the failover and be authorized to proceed. This only happens with the vote of the **majority of the Sentinel processes**.
  >
  > So for example if you have 5 Sentinel processes, and the quorum for a given master set to the value of 2, this is what happens:
  > - If two Sentinels agree at the same time about the master being unreachable, one of the two will try to start a failover.
  > - If there are at least a total of three Sentinels reachable, the failover will be authorized and will actually start.
  >
  > In practical terms this means during failures **Sentinel never starts a failover if the majority of Sentinel processes are unable to talk** (aka no failover in the minority partition).
- `node_count` or `master_node_count+slave_node_count` must be greater or equal to 3.
- To enable Redis High Availability with Redis Sentinel, `replication` (default: `false`) property must be set to `true`.

A deployment example manifest is: [redis-sentinel.yml](manifests-1.5/redis-sentinel.yml)

With the following example variables file: [redis-sentinel.yml](secrets/redis-sentinel.yml)

##### With Distinct AZs

A deployment example manifest is: [redis-sentinel-azs.yml](manifests-1.5/redis-sentinel-azs.yml)

With the following example variables file: [redis-sentinel-azs.yml](secrets/redis-sentinel-azs.yml)

#### Redis Cluster with High Availability

In this release, as previously with Redis High Availability with Redis Sentinel, Redis Cluster with High Availability supports at most two kinds of instance groups:
- **master group**, this group is mandatory and include Redis masters. We use the parameter `master_node_count` as the number of instance in this group.
- **slave group**, this group is optional and is useful if you plan to set Redis process in master group in an distinct AZ than Redis process in slave group. We use the parameter `slave_node_count` as the number of instance in this group.

**Notes**:
- Redis Cluster requires at least 3 master nodes.
- If you use only one instance group, we use the parameter `node_count` as the number of instance in the group and the group includes Redis masters and optionally slaves, if property  `cluster_replicas_per_node` is set greater than `0`.

To enable Redis Cluster with High Availability feature, the property  `cluster_replicas_per_node` (default: `0`) must be set greater than `0`. This property allow to set the number of slave per master in a Redis Cluster. The number of slave per master is a best effort:
- If you use only one instance group, the number of Redis master in the Redis Cluster is integer part of `node_count/(cluster_replicas_per_node+1)`. If `node_count%(cluster_replicas_per_node+1) != 0` then some Redis masters in Redis Cluster could have more than expected Redis slaves. For example, with `node_count=7` and `cluster_replicas_per_node=1`, you would have `3` Redis masters and `4` Redis slaves where:

  - Two Redis masters with one Redis slave each, and
  - One Redis master with two Redis slaves. This is useful for *replica migration feature*, see [*Redis Cluster Tutorial*](https://redis.io/topics/cluster-tutorial) or [*Redis Cluster Specification*](https://redis.io/topics/cluster-spec)

  **Note**: If you use only one instance, a configuration with `node_count=7` and `cluster_replicas_per_node=2` is invalide because there is not the minimum required Redis master (i.e.: `7/(2+1) < 3`) and the Redis Cluster is not created.

- If you use two instance groups (i.e.: an master group and an slave group), if `slave_node_count < master_node_count*cluster_replicas_per_node` then some Redis master have the expected number of Redis slaves (i.e.: `cluster_replicas_per_node`), some less than `cluster_replicas_per_node` and some no Redis slave. If `slave_node_count > master_node_count*cluster_replicas_per_node`, then some Redis masters have more than expected Redis slaves.

So, take care to set:
- When you use only one instance group, `node_count/(cluster_replicas_per_node+1) >= 3` with , and
- When you use two instance groups, `slave_node_count >= master_node_count*cluster_replicas_per_node` with `master_node_count >= 3`.

**Notes**:
- It is recommended to set `node_count/(cluster_replicas_per_node+1) > 3`, or `slave_node_count > master_node_count*cluster_replicas_per_node` with `master_node_count >= 3`, to use the replica migration feature. See the parameter `cluster_migration_barrier` is the Redis job's specification.
- To enable Redis Cluster, property `cluster_enabled` (default: `no`) must be set to `yes`.
- It is useless to use `replication` property to enable replication between master and slave in Redis Cluster. Redis replication is enable if you set `cluster_replicas_per_node` greater than `0`.
- If you set a slave group, but let `cluster_replicas_per_node` to `0`, High Availability feature is disable.
- When you enable Redis Cluster with High Availability feature, take care about the `min_replicas_to_write` (default: `0`) property. See release specifications for details.

A deployment example manifest is: [redis-cluster.yml](manifests-1.5/redis-cluster.yml)

With the following example variables file: [redis-cluster.yml](secrets/redis-cluster.yml)

##### With Distinct AZs

A deployment example manifest is: [redis-cluster-azs.yml](manifests-1.5/redis-cluster-azs.yml)

With the following example variables file: [redis-cluster-azs.yml](secrets/redis-cluster-azs.yml)