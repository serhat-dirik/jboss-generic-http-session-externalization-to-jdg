#!/bin/bash
JBOSS_HOME=/opt/jboss/wildfly
cp -r -a /deployments/jdg-service.war $JBOSS_HOME/standalone/deployments
echo "" > $JBOSS_HOME/standalone/deployments/jdg-service.war.dodeploy

_HOST_NAME="$(cat /etc/hostname)"
IPADDR=$(hostname -I)
IP_JDG_NODE1=$(ping -c 1 jdg-node1 | gawk -F'[()]' '/PING/{print $2}')
IP_JDG_NODE2=$(ping -c 1 jdg-node2 | gawk -F'[()]' '/PING/{print $2}')
IP_JDG_NODE3=$(ping -c 1 jdg-node3 | gawk -F'[()]' '/PING/{print $2}')

cp -r -a /wildflyConfig/config/* $JBOSS_HOME/standalone/configuration
chmod -R 777 $JBOSS_HOME/standalone/config/*
#Start Wildfly
$JBOSS_HOME/bin/standalone.sh -b $IPADDR -c standalone.xml  \
 -bmanagement $IPADDR  \
 --debug 8787 \
 -Djboss.node.name=$_HOST_NAME \
 -Djava.net.preferIPv4Stack=true\
 -Djdg-node1.addr=$IP_JDG_NODE1 \
 -Djdg-node2.addr=$IP_JDG_NODE2 \
 -Djdg-node3.addr=$IP_JDG_NODE3 \
 -Djdg-node1.port=11222 \
 -Djdg-node2.port=11222 \
 -Djdg-node3.port=11222
