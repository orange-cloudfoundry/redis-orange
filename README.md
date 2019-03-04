# bosh-redis

A [*Redis*](https://redis.io/) release for Cloud Foundry

## Features

- High availability by [*Redis Sentinel*](https://redis.io/topics/sentinel)
- [*Redis cluster*](https://redis.io/topics/cluster-spec) with high availability
- Monitoring by [*Prometheus*](https://prometheus.io/)/[*Grafana*](https://grafana.com/)

## TODO

- Tests for [*persistence*](https://redis.io/topics/persistence)
- Backup/restore
- Logging

## Usage

### Redis

### Redis High Availability by Redis Sentinel

### Redis Cluster with High Availability

**Note**: For security purposes, we use [*CredHub*](https://docs.cloudfoundry.org/credhub/)  for passwords and to obfuscate some Redis admin commands.
