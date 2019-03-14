#!/bin/bash
<%
  master=link('redis_conn')
  if_link('master_conn'){ |x| master=x }
  slave=link('redis_conn')
  if_link('slave_conn'){ |x| slave=x }
%>

ERR=0;

<% if master.p('replication') %>

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

<% end %>

exit ${ERR};
