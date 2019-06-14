# redis-orange

A [*Redis*](https://redis.io/) release for Cloud Foundry

## Features

- High availability by [*Redis Sentinel*](https://redis.io/topics/sentinel)
- [*Redis cluster*](https://redis.io/topics/cluster-spec) with high availability
- Multi-zone for high availability by Redis Sentinel and Redis cluster with high availability,
- Monitoring by [*Prometheus*](https://prometheus.io/)/[*Grafana*](https://grafana.com/)
- An Open Service Broker

## TODO

- Tests for [*persistence*](https://redis.io/topics/persistence)
- Backup/restore
- Logging

## Packages versions summary

- Redis [*5.0.5*](http://download.redis.io/releases/redis-5.0.5.tar.gz)
- [*redis_exporter*](https://github.com/oliver006/redis_exporter) [*1.0.0*](https://github.com/oliver006/redis_exporter/releases/download/v1.0.0/redis_exporter-v1.0.0.linux-amd64.tar.gz)
- [*OpenJDK*](https://openjdk.java.net/) [*12.0.1*](https://download.java.net/java/GA/jdk12.0.1/69cfe15208a647278a19ef0990eea691/12/GPL/openjdk-12.0.1_linux-x64_bin.tar.gz)
- [*utils.sh*](https://github.com/bosh-prometheus/prometheus-boshrelease/blob/master/src/common/utils.sh)

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

The deployment manifest is:

```yaml
---
name: ((deployment_name))

instance_groups:
- name: redis
  azs: [((default_az))]
  instances: ((node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    properties:
      bind: ((redis_bind))
      password: ((redis_password))
      rename_config_command: ((redis_rename_config_command))
      rename_save_command: ((redis_rename_save_command))
      rename_bgsave_command: ((redis_rename_bgsave_command))
      rename_bgrewriteaof_command: ((redis_rename_bgrewriteaof_command))
      rename_monitor_command: ((redis_rename_monitor_command))
      rename_debug_command: ((redis_rename_debug_command))
      rename_shutdown_command: ((redis_rename_shutdown_command))
      rename_slaveof_command: ((redis_rename_slaveof_command))
      rename_replicaof_command: ((redis_rename_replicaof_command))
      rename_sync_command: ((redis_rename_sync_command))
  - name: redis_check
    release: redis-service
  - name: redis_broker
    release: redis-service
    properties:
      bind: ((redis_bind))
  - name: redis_broker_check
    release: redis-service
  - name: redis_exporter
    release: redis-service
    properties:
      debug: ((redis_exporter_debug))

variables:
- name: redis_password
  type: password
- name: redis_rename_config_command
  type: password
- name: redis_rename_save_command
  type: password
- name: redis_rename_bgsave_command
  type: password
- name: redis_rename_bgrewriteaof_command
  type: password
- name: redis_rename_monitor_command
  type: password
- name: redis_rename_debug_command
  type: password
- name: redis_rename_shutdown_command
  type: password
- name: redis_rename_slaveof_command
  type: password
- name: redis_rename_replicaof_command
  type: password
- name: redis_rename_sync_command
  type: password

stemcells:
- alias: default
  os: ((stemcell_os))
  version: "((stemcell_version))"

releases:
- name: redis-service
  version: latest

update:
  canaries: 2
  canary_watch_time: 30000-60000
  max_in_flight: 2
  update_watch_time: 30000-60000
  serial: false
```

With the following variables file:

```yaml
---
deployment_name: redis
node_count: 1
default_vm_type: small
default_persistent_disk_type: small
default_network: redis-network
default_az: z1
stemcell_os: ubuntu-xenial
stemcell_version: 250.29
redis_bind: true
redis_exporter_debug: false
```

**Note**: For security purposes, we use [*CredHub*](https://docs.cloudfoundry.org/credhub/) for passwords and to obfuscate some Redis admin commands (e.g.: `CONFIG`, `DEBUG`), etc..

**Note**: We provide two errands:
- `redis_check` to test deployed Redis instance with create, read, write and delete operations,
- `redis_broker_check` to test deployed Redis broker by accessing Redis's catalog and service instance binding.

**Note**: Redis server and the Prometheus Redis exporter which monitor it must be on the same instance.

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

**Note**: If you use only one instance group, we use the parameter `node_count` as the number of instance in the group.

In our release, Redis quorum's value is:
- **(node_count/2)+1**, if you plan to use only one instance group, or
- **(master_node_count+slave_node_count/2)+1**, if you plan to use an instance group for Redis master and another one for Redis slaves. 

**Note**: `node_count` and `master_node_count+slave_node_count` must be an odd integer and greater or equal to 3.

**Note**: To enable Redis High Availability with Redis Sentinel, `replication` (default: `false`) property must be set to `true`.

**Note**: Take care about the `min_replicas_to_write` (default: `0`) property. See release specifications for details.

The deployment manifest is:

```yaml
---
name: ((deployment_name))

instance_groups:
- name: redis
  azs: [((default_az))]
  instances: ((node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    properties:
      bind: ((redis_bind))
      password: ((redis_password))
      rename_config_command: ((redis_rename_config_command))
      rename_save_command: ((redis_rename_save_command))
      rename_bgsave_command: ((redis_rename_bgsave_command))
      rename_bgrewriteaof_command: ((redis_rename_bgrewriteaof_command))
      rename_monitor_command: ((redis_rename_monitor_command))
      rename_debug_command: ((redis_rename_debug_command))
      rename_shutdown_command: ((redis_rename_shutdown_command))
      rename_slaveof_command: ((redis_rename_slaveof_command))
      rename_replicaof_command: ((redis_rename_replicaof_command))
      rename_sync_command: ((redis_rename_sync_command))
      replication: ((redis_replication))
      min_replicas_to_write: ((redis_min_replicas_to_write))
  - name: redis_sentinel
    release: redis-service
    properties:
      bind: ((redis_sentinel_bind))
      password: ((redis_sentinel_password))
  - name: redis_check
    release: redis-service
  - name: redis_exporter
    release: redis-service
    properties:
      debug: ((redis_exporter_debug))
- name: broker
  azs: [((default_az))]
  instances: 1
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis_broker
    release: redis-service
    consumes:
      redis_sentinel_master_conn: nil
      redis_sentinel_slave_conn: nil
    properties:
      bind: ((redis_bind))
  - name: redis_broker_check
    release: redis-service

variables:
- name: redis_password
  type: password
- name: redis_sentinel_password
  type: password
- name: redis_rename_config_command
  type: password
- name: redis_rename_save_command
  type: password
- name: redis_rename_bgsave_command
  type: password
- name: redis_rename_bgrewriteaof_command
  type: password
- name: redis_rename_monitor_command
  type: password
- name: redis_rename_debug_command
  type: password
- name: redis_rename_shutdown_command
  type: password
- name: redis_rename_slaveof_command
  type: password
- name: redis_rename_replicaof_command
  type: password
- name: redis_rename_sync_command
  type: password

stemcells:
- alias: default
  os: ((stemcell_os))
  version: "((stemcell_version))"

releases:
- name: redis-service
  version: latest

update:
  canaries: 2
  canary_watch_time: 30000-60000
  max_in_flight: 2
  update_watch_time: 30000-60000
  serial: false
```

With the following variables file:

```yaml
---
deployment_name: redis-sentinel
node_count: 3
default_vm_type: small
default_persistent_disk_type: small
default_network: redis-network
default_az: z1
stemcell_os: ubuntu-xenial
stemcell_version: 250.29
redis_bind: true
redis_exporter_debug: false
redis_replication: true
redis_sentinel_bind: true
redis_min_replicas_to_write: 1
```

##### With Distinct AZs

The deployment manifest is:

```yaml
---
name: ((deployment_name))

instance_groups:
- name: redis-master
  azs: [((default_az))]
  instances: ((master_node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    provides:
      redis_conn: {as: master}
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
    properties:
      bind: ((redis_bind))
      password: ((redis_password))
      rename_config_command: ((redis_rename_config_command))
      rename_save_command: ((redis_rename_save_command))
      rename_bgsave_command: ((redis_rename_bgsave_command))
      rename_bgrewriteaof_command: ((redis_rename_bgrewriteaof_command))
      rename_monitor_command: ((redis_rename_monitor_command))
      rename_debug_command: ((redis_rename_debug_command))
      rename_shutdown_command: ((redis_rename_shutdown_command))
      rename_slaveof_command: ((redis_rename_slaveof_command))
      rename_replicaof_command: ((redis_rename_replicaof_command))
      rename_sync_command: ((redis_rename_sync_command))
      replication: ((redis_replication))
      min_replicas_to_write: ((redis_min_replicas_to_write))
  - name: redis_sentinel
    release: redis-service
    provides:
      redis_sentinel_conn: {as: sentinel_master}
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
    properties:
      bind: ((redis_sentinel_bind))
      password: ((redis_sentinel_password))
  - name: redis_check
    release: redis-service
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
      redis_sentinel_conn: {from: sentinel_master}
  - name: redis_exporter
    release: redis-service
    consumes:
      redis_conn: {from: master}
    properties:
      debug: ((redis_exporter_debug))
- name: redis-slave
  azs: [((default_az))]
  instances: ((slave_node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    provides:
      redis_conn: {as: slave}
    consumes:
      redis_conn: {from: slave}
      master_conn: {from: master}
      slave_conn: {from: slave}
  - name: redis_sentinel
    release: redis-service
    provides:
      redis_sentinel_conn: {as: sentinel_slave}
    consumes:
      redis_conn: {from: slave}
      master_conn: {from: master}
      slave_conn: {from: slave}
    properties:
      bind: ((redis_sentinel_bind))
      password: ((redis_sentinel_password))
  - name: redis_check
    release: redis-service
    consumes:
      redis_conn: {from: slave}
      master_conn: {from: master}
      slave_conn: {from: slave}
      redis_sentinel_conn: {from: sentinel_slave}
  - name: redis_exporter
    release: redis-service
    consumes:
      redis_conn: {from: slave}
    properties:
      debug: ((redis_exporter_debug))
- name: broker
  azs: [((default_az))]
  instances: 1
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis_broker
    release: redis-service
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
      redis_sentinel_conn: {from: sentinel_master}
      redis_sentinel_master_conn: {from: sentinel_master}
      redis_sentinel_slave_conn: {from: sentinel_slave}
    properties:
      bind: ((redis_bind))
  - name: redis_broker_check
    release: redis-service

variables:
- name: redis_password
  type: password
- name: redis_sentinel_password
  type: password
- name: redis_rename_config_command
  type: password
- name: redis_rename_save_command
  type: password
- name: redis_rename_bgsave_command
  type: password
- name: redis_rename_bgrewriteaof_command
  type: password
- name: redis_rename_monitor_command
  type: password
- name: redis_rename_debug_command
  type: password
- name: redis_rename_shutdown_command
  type: password
- name: redis_rename_slaveof_command
  type: password
- name: redis_rename_replicaof_command
  type: password
- name: redis_rename_sync_command
  type: password

stemcells:
- alias: default
  os: ((stemcell_os))
  version: "((stemcell_version))"

releases:
- name: redis-service
  version: latest

update:
  canaries: 2
  canary_watch_time: 30000-60000
  max_in_flight: 2
  update_watch_time: 30000-60000
  serial: false
```

With the following variables file:

```yaml
---
deployment_name: redis-sentinel-azs
master_node_count: 1
slave_node_count: 2
default_vm_type: small
default_persistent_disk_type: small
default_network: redis-network
default_az: z1
stemcell_os: ubuntu-xenial
stemcell_version: 250.29
redis_bind: true
redis_exporter_debug: false
redis_replication: true
redis_sentinel_bind: true
redis_min_replicas_to_write: 1
```

#### Redis Cluster with High Availability

In this release, as previously with Redis High Availability with Redis Sentinel, Redis Cluster with High Availability supports at most two kinds of instance groups:
- **master group**, this group is mandatory and include Redis masters. We use the parameter `master_node_count` as the number of instance in this group.
- **slave group**, this group is optional and is useful if you plan to set Redis process in master group in an distinct AZ than Redis process in slave group. We use the parameter `slave_node_count` as the number of instance in this group.

To enable Redis Cluster with High Availability feature, the property  `cluster_replicas_per_node` (default: `0`) must be set greater than `0`. This property set the number of slave per node in a Redis Cluster. The number of slave per node is a best effort:
- If you use only one instance group,
- If you use two instance groups (i.e.: an master group and an slave group), if 

**Note**: If you use only one instance group, we use the parameter `node_count` as the number of instance in the group and the group includes Redis masters and optionally slaves, if property  `cluster_replicas_per_node` is set greater than `0`.

**Note**: To enable Redis Cluster, property `cluster_enabled` (default: `no`) must be set to `yes`.

**Note**: It is useless to use `replication` property to enable replication between master and slave in Redis Cluster. Redis replication is enable if you set `cluster_replicas_per_node` greater than `0`.

**Note**: If you set a slave group, but let `cluster_replicas_per_node` to `0`, High Availability feature is disable.

The deployment manifest is:

```yaml
---
name: ((deployment_name))

instance_groups:
- name: redis
  azs: [((default_az))]
  instances: ((node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    properties:
      bind: ((redis_bind))
      password: ((redis_password))
      rename_config_command: ((redis_rename_config_command))
      rename_save_command: ((redis_rename_save_command))
      rename_bgsave_command: ((redis_rename_bgsave_command))
      rename_bgrewriteaof_command: ((redis_rename_bgrewriteaof_command))
      rename_monitor_command: ((redis_rename_monitor_command))
      rename_debug_command: ((redis_rename_debug_command))
      rename_shutdown_command: ((redis_rename_shutdown_command))
      rename_slaveof_command: ((redis_rename_slaveof_command))
      rename_replicaof_command: ((redis_rename_replicaof_command))
      rename_sync_command: ((redis_rename_sync_command))
      cluster_enabled: ((redis_cluster_enabled))
      cluster_replicas_per_node: ((replicas_per_node_count))
      min_replicas_to_write: ((redis_min_replicas_to_write))
  - name: redis_check
    release: redis-service
  - name: redis_exporter
    release: redis-service
    properties:
      debug: ((redis_exporter_debug))
- name: broker
  azs: [((default_az))]
  instances: 1
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis_broker
    release: redis-service
    properties:
      bind: ((redis_bind))
  - name: redis_broker_check
    release: redis-service

variables:
- name: redis_password
  type: password
- name: redis_rename_config_command
  type: password
- name: redis_rename_save_command
  type: password
- name: redis_rename_bgsave_command
  type: password
- name: redis_rename_bgrewriteaof_command
  type: password
- name: redis_rename_monitor_command
  type: password
- name: redis_rename_debug_command
  type: password
- name: redis_rename_shutdown_command
  type: password
- name: redis_rename_slaveof_command
  type: password
- name: redis_rename_replicaof_command
  type: password
- name: redis_rename_sync_command
  type: password

stemcells:
- alias: default
  os: ((stemcell_os))
  version: "((stemcell_version))"

releases:
- name: redis-service
  version: latest

update:
  canaries: 2
  canary_watch_time: 30000-60000
  max_in_flight: 2
  update_watch_time: 30000-60000
  serial: false
```

With the following variables file:

```yaml
---
deployment_name: redis-cluster
node_count: 6
default_vm_type: small
default_persistent_disk_type: small
default_network: redis-network
default_az: z1
stemcell_os: ubuntu-xenial
stemcell_version: 250.29
redis_bind: true
redis_exporter_debug: false
redis_cluster_enabled: 'yes'
replicas_per_node_count: 1
redis_min_replicas_to_write: 1
```

##### With Distinct AZs

The deployment manifest is:

```yaml
---
name: ((deployment_name))

instance_groups:
- name: redis-master
  azs: [((default_az))]
  instances: ((master_node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    provides:
      redis_conn: {as: master}
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
    properties:
      bind: ((redis_bind))
      password: ((redis_password))
      rename_config_command: ((redis_rename_config_command))
      rename_save_command: ((redis_rename_save_command))
      rename_bgsave_command: ((redis_rename_bgsave_command))
      rename_bgrewriteaof_command: ((redis_rename_bgrewriteaof_command))
      rename_monitor_command: ((redis_rename_monitor_command))
      rename_debug_command: ((redis_rename_debug_command))
      rename_shutdown_command: ((redis_rename_shutdown_command))
      rename_slaveof_command: ((redis_rename_slaveof_command))
      rename_replicaof_command: ((redis_rename_replicaof_command))
      rename_sync_command: ((redis_rename_sync_command))
      cluster_enabled: ((redis_cluster_enabled))
      cluster_replicas_per_node: ((replicas_per_node_count))
      min_replicas_to_write: ((redis_min_replicas_to_write))
  - name: redis_check
    release: redis-service
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
  - name: redis_exporter
    release: redis-service
    consumes:
      redis_conn: {from: master}
    properties:
      debug: ((redis_exporter_debug))
- name: redis-slave
  azs: [((default_az))]
  instances: ((slave_node_count))
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis
    release: redis-service
    provides:
      redis_conn: {as: slave}
    consumes:
      redis_conn: {from: slave}
      master_conn: {from: master}
      slave_conn: {from: slave}
  - name: redis_check
    release: redis-service
    consumes:
      redis_conn: {from: slave}
      master_conn: {from: master}
      slave_conn: {from: slave}
  - name: redis_exporter
    release: redis-service
    consumes:
      redis_conn: {from: slave}
    properties:
      debug: ((redis_exporter_debug))
- name: broker
  azs: [((default_az))]
  instances: 1
  vm_type: ((default_vm_type))
  stemcell: default
  persistent_disk_type: ((default_persistent_disk_type))
  networks:
  - name: ((default_network))
  jobs:
  - name: redis_broker
    release: redis-service
    consumes:
      redis_conn: {from: master}
      master_conn: {from: master}
      slave_conn: {from: slave}
    properties:
      bind: ((redis_bind))
  - name: redis_broker_check
    release: redis-service

variables:
- name: redis_password
  type: password
- name: redis_rename_config_command
  type: password
- name: redis_rename_save_command
  type: password
- name: redis_rename_bgsave_command
  type: password
- name: redis_rename_bgrewriteaof_command
  type: password
- name: redis_rename_monitor_command
  type: password
- name: redis_rename_debug_command
  type: password
- name: redis_rename_shutdown_command
  type: password
- name: redis_rename_slaveof_command
  type: password
- name: redis_rename_replicaof_command
  type: password
- name: redis_rename_sync_command
  type: password

stemcells:
- alias: default
  os: ((stemcell_os))
  version: "((stemcell_version))"

releases:
- name: redis-service
  version: latest

update:
  canaries: 2
  canary_watch_time: 30000-60000
  max_in_flight: 2
  update_watch_time: 30000-60000
  serial: false
```

With the following variables file:

```yaml
---
deployment_name: redis-cluster-azs
master_node_count: 3
slave_node_count: 3
default_vm_type: small
default_persistent_disk_type: small
default_network: redis-network
default_az: z1
stemcell_os: ubuntu-xenial
stemcell_version: 250.29
redis_bind: true
redis_exporter_debug: false
redis_cluster_enabled: 'yes'
replicas_per_node_count: 1
redis_min_replicas_to_write: 1
```