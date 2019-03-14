#!/bin/bash
<%
  master=link('redis_conn')
  if_link('master_conn'){ |x| master=x }
  slave=link('redis_conn')
  if_link('slave_conn'){ |x| slave=x }
%>

ERR=0;

<% if master.p('cluster_enabled').eql?('yes') %>

REDIS_HOME="/var/vcap/packages/redis";
REDIS_CLI="${REDIS_HOME}/bin/redis-cli";

<% if master.p('bind') %>
REDIS_IP="<%= spec.ip %>";
<% else %>
REDIS_IP="127.0.0.1";
<% end %>
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
  if !slave.instances[0].name.eql?(master.instances[0].name)
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

REDIS_CLUSTER_NODES_COMMAND="$(${REDIS_CLI} ${REDIS_PASSWORD} \
  -h ${REDIS_IP} -p ${REDIS_PORT} CLUSTER NODES)";
ERR=${?};
if [[ ${ERR} -eq 0 ]];
then
  echo "Redis CLUSTER NODES command results:";
  echo "${REDIS_CLUSTER_NODES_COMMAND}";
  AVAILABLE_NODES=0;
  for i in $(echo ${REDIS_CLUSTER_NODES_COMMAND} | awk '{print $2;}');
  do
    for j in ${REDIS_ADDRESSES};
    do
      if [[ "${i}" -eq "${j}" ]];
      then
        ((AVAILABLE_NODES++));
      fi
    done
  done
  if [[ [[ ${REDIS_INSTANCES} -eq ${AVAILABLE_NODES} ]];
  then
    echo "The requested Redis nodes [${REDIS_INSTANCES}] is equal to the available nodes [${AVAILABLE_NODES}]";
<% if !slave.instances[0].name.eql?(master.instances[0].name) %>
    REDIS_CLUSTER_SLAVE_NODES="$(echo ${REDIS_CLUSTER_NODES_COMMAND} | \
      awk '$3~/slave/{print $2;}')";
    AVAILABLE_NODES=0;
    for i in ${REDIS_SLAVE_ADDRESSES};
    do
      for j in ${REDIS_CLUSTER_SLAVE_NODES};
      do
        if [[ "${i}" -eq "${j}" ]];
        then
          ((AVAILABLE_NODES++));
        fi
      done
    done
    if [[ ${REDIS_SLAVE_INSTANCES} -eq ${AVAILABLE_NODES} ]];
    then
      echo "The requested Redis slave nodes [${REDIS_SLAVE_INSTANCES}] is equal to the available slave nodes [${AVAILABLE_NODES}]";
      ERR=0;
    else
      echo "ERROR: The requested Redis slave nodes [${REDIS_SLAVE_INSTANCES}] is not equal to the available slave nodes [${AVAILABLE_NODES}]" >&2;
      ERR=1;
    fi
<% end %>
  else
    echo "ERROR: The requested Redis nodes [${REDIS_INSTANCES}] is not equal to the available nodes [${AVAILABLE_NODES}]" >&2;
    ERR=1;
  fi
else
  echo "ERROR: Redis CLUSTER NODES command failed." >&2;
  ERR=1;
fi

<% end %>

exit ${ERR};
