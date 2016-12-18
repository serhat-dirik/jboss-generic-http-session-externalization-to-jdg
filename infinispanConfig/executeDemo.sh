#!/bin/bash
JDG_HOME="/opt/jboss/infinispan-server"
#Copy jdg config
rm -rf $JDG_HOME/standaloneconfiguration/clustered.xml
cp -r -a /infinispanConfig/config/*  $JDG_HOME/standalone/configuration

#######
# Start EAP
#######
_HOST_NAME="$(cat /etc/hostname)"
IPADDR=$(hostname -I)
$JDG_HOME/bin/standalone.sh -b $IPADDR -c clustered.xml \
  -bmanagement $IPADDR \
  -Djboss.node.name=$_HOST_NAME \
  -Djava.net.preferIPv4Stack=true
