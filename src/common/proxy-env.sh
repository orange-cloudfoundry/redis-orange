#!/usr/bin/env bash

function extract_host_port() {
    sed -e 's`https\?://\([^/]*\).*`\1`i'
}
set +u
declare -a JAVA_OPTS
if [ -n "$http_proxy" ]; then
    JAVA_OPTS+=(-Dhttp.proxyHost=$(echo "$http_proxy" | extract_host_port | cut -d: -f1))
    JAVA_OPTS+=(-Dhttp.proxyPort=$(echo "$http_proxy" | extract_host_port | cut -d: -f2))
fi
if [ -n "$https_proxy" ]; then
    JAVA_OPTS+=(-Dhttps.proxyHost=$(echo "$https_proxy" | extract_host_port | cut -d: -f1))
    JAVA_OPTS+=(-Dhttps.proxyPort=$(echo "$https_proxy" | extract_host_port | cut -d: -f2))
fi
export MAVEN_OPTS="${JAVA_OPTS[@]}"
