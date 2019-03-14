#!/bin/bash
<%
  master=link('redis_conn')
  if_link('master_conn'){ |x| master=x }
  slave=link('redis_conn')
  if_link('slave_conn'){ |x| slave=x }
%>

ERR=0;

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

PING="$(${REDIS_CLI} -h ${REDIS_IP} -p ${REDIS_PORT} ${REDIS_PASSWORD} PING)";
ERR=${?}
if [[ ${ERR} -eq 0 ]] && [[ "${PING}" -eq "PONG" ]];
then
  echo "Redis server at address [${REDIS_IP}] and port [${REDIS_PORT}] is available.";
fi

exit ${ERR};
