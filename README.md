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

- Redis [*5.0.4*](http://download.redis.io/releases/redis-5.0.4.tar.gz)
- [*redis_exporter*](https://github.com/oliver006/redis_exporter) [*0.34.0*](https://github.com/oliver006/redis_exporter/releases/download/v0.342.0/redis_exporter-v0.32.0.linux-amd64.tar.gz)
- [*OpenJDK*](https://openjdk.java.net/) [*12.0.1*](https://download.java.net/java/GA/jdk12.0.1/69cfe15208a647278a19ef0990eea691/12/GPL/openjdk-12.0.1_linux-x64_bin.tar.gz)
- [*utils.sh*](https://github.com/bosh-prometheus/prometheus-boshrelease/blob/master/src/common/utils.sh)

## Usage

### Clone the repository:
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
stemcell_version: 250.4
redis_bind: true
redis_exporter_debug: false
```
**Note**: For security purposes, we use [*CredHub*](https://docs.cloudfoundry.org/credhub/) for passwords and to obfuscate some Redis admin commands (e.g.: `CONFIG`, `DEBUG`), etc..
#### Redis High Availability with Redis Sentinel
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
    properties: {}
  - name: redis_exporter
    release: redis-service
    properties:
      debug: ((redis_exporter_debug))

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
stemcell_version: 250.4
redis_bind: true
redis_exporter_debug: false
redis_replication: true
redis_sentinel_bind: true
redis_min_replicas_to_write: 1
```
##### With Distinct AZs
