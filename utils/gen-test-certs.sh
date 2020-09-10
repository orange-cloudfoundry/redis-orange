#!/bin/bash
deployment_name=${1:?"Missing deployment name"}
type=${2:?"Missing type"}
res_file=${3:?"Missing outpout file name"}
if [[ ! -f ${deployment_name}.key ]];
then
  openssl genrsa -out ${deployment_name}.key 4096
  openssl req \
    -x509 -new -nodes -sha256 \
    -key ${deployment_name}.key \
    -days 3650 \
    -subj "/O=orange.com/CN=${deployment_name}" \
    -out ${deployment_name}.crt
fi
openssl genrsa -out ${res_file}.key 2048
openssl req \
  -new -sha256 \
  -key ${res_file}.key \
  -subj "/O=orange.com/OU=${deployment_name}/CN=${type}" | \
  openssl x509 \
    -req -sha256 \
    -CA ${deployment_name}.crt \
    -CAkey ${deployment_name}.key \
    -CAserial ${deployment_name}.txt \
    -CAcreateserial \
    -days 365 \
    -out ${res_file}.crt
