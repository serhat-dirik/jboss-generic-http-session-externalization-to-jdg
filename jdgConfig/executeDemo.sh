#!/bin/bash
JDG_HOME="/opt/jboss/jdg/jboss-datagrid-7.0.0-server"
#Copy jdg config
rm -rf $JDG_HOME/standaloneconfiguration/clustered.xml
cp -r -a /jdgConfig/config/clustered.xml  $JDG_HOME/standalone/configuration

#######
# Start EAP
#######
_HOST_NAME="$(cat /etc/hostname)"
IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')
$JDG_HOME/bin/standalone.sh -b $IPADDR -c clustered.xml \
  -bmanagement $IPADDR \
  -Djboss.node.name=$_HOST_NAME \
  -Djava.net.preferIPv4Stack=true
