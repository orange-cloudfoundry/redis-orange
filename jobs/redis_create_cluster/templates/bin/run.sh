#!/bin/bash
<%
  master=link('redis_conn')
  if_link('master_conn'){ |x| master=x }
  slave=link('redis_conn')
  if_link('slave_conn'){ |x| slave=x }
%>

<% if master.p('cluster_enabled').eql?('yes') && master.instances.find{ |x| x.address.eql?(spec.address) }.bootstrap %>
REDIS_HOME="/var/vcap/packages/redis";
REDIS_CLI="${REDIS_HOME}/bin/redis-cli";

REDIS_PORT="<%= master.p('port') %>";

REDIS_PASSWORD="";
<%
  master.if_p('password') do |password|
    unless password.to_s.empty? %>
REDIS_PASSWORD="-a <%= password %>";
<%  end
  end
%>

<%
  addresses=''
  master.instances.each{ |x| addresses.concat(x.address).concat(' ') }
  instances=master.instances.length
%>
REDIS_MASTER_ADDRESSES="<%= addresses %>";
REDIS_MASTER_INSTANCES="<%= instances %>";
<%
  slaves_addresses=''
  slave_instances=0
  if !slave.instances.eql?(master.instances)
    slave.instances.each{ |x| slaves_addresses.concat(x.address).concat(' ') }
    slave_instances=slave.instances.length
  end
  addresses.concat(slaves_addresses)
  instances+=slave_instances
%>
REDIS_SLAVE_ADDRESSES="<%= slaves_addresses %>";
REDIS_SLAVE_INSTANCES="<%= slave_instances %>";
REDIS_ADDRESSES="<%= addresses %>";
REDIS_INSTANCES="<%= instances %>";

REDIS_SLAVE_PER_INSTANCE="";
SLAVE_PER_INSTANCE=0;
<% master.if_p('cluster_replicas_per_node') do |x| %>
REDIS_SLAVE_PER_INSTANCE="--cluster-replicas <%= x %>";
SLAVE_PER_INSTANCE=<%= x.to_i %>;
<% end %>

REDIS_MASTER_NODES="";
for i in ${REDIS_MASTER_ADDRESSES};
do
  REDIS_MASTER_NODES+=" ${i}:${REDIS_PORT}";
done

REDIS_SLAVE_NODES="";
for i in ${REDIS_SLAVE_ADDRESSES};
do
  REDIS_SLAVE_NODES+=" ${i}:${REDIS_PORT}";
done

AVAILABLE_NODES=0;
while [[ ${REDIS_INSTANCES} -gt ${AVAILABLE_NODES} ]];
do
  AVAILABLE_NODES=0;
  for i in ${REDIS_ADDRESSES};
  do
    PING="$(${REDIS_CLI} -h ${i} -p ${REDIS_PORT} ${REDIS_PASSWORD} PING)";
    if [[ "${PING}" == "PONG" ]];
    then
      ((AVAILABLE_NODES++));
    fi
  done
done

if [[ ${REDIS_INSTANCES} -eq ${AVAILABLE_NODES} ]];
then
<%
  if slave.instances.eql?(master.instances) %>
  ${REDIS_CLI} \
    ${REDIS_PASSWORD} \
    --cluster create ${REDIS_MASTER_NODES} ${REDIS_SLAVE_NODES} \
    ${REDIS_SLAVE_PER_INSTANCE} <<< "yes";
  exit ${?};
<% else %>
  if [[ ${REDIS_MASTER_INSTANCES} -ge ${REDIS_SLAVE_INSTANCES}/${SLAVE_PER_INSTANCE} ]];
  then
    echo "ERROR: Not enough slave: master\[${REDIS_MASTER_INSTANCES}\], slave\[${REDIS_SLAVE_INSTANCES}\], slave per master\[${SLAVE_PER_INSTANCE}\]" >&2;
    exit 1;
  fi
  ${REDIS_CLI} \
    ${REDIS_PASSWORD} \
    --cluster create ${REDIS_MASTER_NODES} <<< "yes";
  if [[ ! ${?} -eq 0 ]];
  then
    exit ${?};
  fi
  for i in ${REDIS_MASTER_ADDRESSES};
  do
    MASTER_ID="$(${REDIS_CLI} \
      ${REDIS_PASSWORD} \
      -h ${i} -p ${REDIS_PORT} \
      CLUSTER MYID)";
    j=0;
    while [[ ${j} -lt ${SLAVE_PER_INSTANCE} ]];
    do
      ${REDIS_CLI} \
        ${REDIS_PASSWORD} \
        --cluster add-node ${REDIS_SLAVE_ADDRESSES%% *}:${REDIS_PORT} \
        ${i}:${REDIS_PORT} \
        --cluster-slave \
        --cluster-master-id ${MASTER_ID};
      if [[ ! ${?} -eq 0 ]];
      then
        exit ${?};
      fi
      REDIS_SLAVE_ADDRESSES="${REDIS_SLAVE_ADDRESSES#* }";
      ((j++));
    done
  done
<% end %>
fi
<% end %>

exit 0;
