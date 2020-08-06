#!/usr/bin/env bash
export RANDFILE=$HOME/.rnd;

CERTIFICATE_ORGANIZATION="orange.com";

function gen_seed() {
  local private_key_length="${1:?"Missing private key length"}";
  dd if=/dev/urandom of=$RANDFILE bs="$((private_key_length/8))" count=1;
  return ${?};
}

function gen_ca() {
  local private_key_file="${1:?"Missing private key file name"}";
  local certificate_file="${2:?"Missing certificate file name"}";
  local private_key_length="${3:?"Missing private key length"}";
  local certificate_duration="${4:?"Missing certificate duration"}";
  local certificate_cn="${5:?"Missing certificate common name"}";
  rm -f "${private_key_file}" \
    "${certificate_file}" && \
  openssl genrsa -out "${private_key_file}" "${private_key_length}" && \
  gen_seed "${private_key_length}" && \
  openssl req \
    -x509 -new -nodes -sha256 \
    -key "${private_key_file}" \
    -days "${certificate_duration}" \
    -subj "/O=${CERTIFICATE_ORGANIZATION}/CN=${certificate_cn}" \
    -out "${certificate_file}";
  return ${?};
}

function gen_signed_cert() {
  local ca_private_key_file="${1:?"Missing CA private key file"}";
  local ca_certificate_file="${2:?"Missing CA certificate file"}";
  local ca_serial_file="${3:?"Missing CA serial file"}";
  local private_key_file="${4:?"Missing private key file name"}";
  local certificate_file="${5:?"Missing certificate file name"}";
  local private_key_length="${6:?"Missing private key length"}";
  local certificate_duration="${7:?"Missing certificate duration"}";
  local certificate_ou="${8:?"Missing certificate organization unit"}";
  local certificate_cn="${9:?"Missing certificate common name"}";
  rm -f "${private_key_file}" \
    "${certificate_file}" && \
  openssl genrsa -out "${private_key_file}" "${private_key_length}" && \
  gen_seed "${private_key_length}" && \
  openssl req \
    -new -sha256 \
    -key "${private_key_file}" \
    -subj "/O=${CERTIFICATE_ORGANIZATION}/OU=${certificate_ou}/CN=${certificate_cn}" | \
    openssl x509 \
      -req -sha256 \
      -CA "${ca_certificate_file}" \
      -CAkey "${ca_private_key_file}" \
      -CAserial "${ca_serial_file}" \
      -CAcreateserial \
      -days "${certificate_duration}" \
      -out "${certificate_file}";
  return ${?};
}

function gen_dh {
  local dh_file="${1:?"Missing DH file name"}";
  local dh_length="${2:?"Missing DH length"}";
  rm -f "${dh_file}" && \
  openssl dhparam -out \
    "${dh_file}" \
    "${dh_length}";
  return ${?};
}