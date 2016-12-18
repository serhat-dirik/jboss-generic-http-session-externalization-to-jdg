#!/bin/bash
EAP_HOME=/opt/jboss/eap/jboss-eap-7.0
cp -r -a /deployments/jdg-service.war $EAP_HOME/standalone/deployments
echo "" > $EAP_HOME/standalone/deployments/jdg-service.war.dodeploy

_HOST_NAME="$(cat /etc/hostname)"
IPADDR=$(ip a s | sed -ne '/127.0.0.1/!{s/^[ \t]*inet[ \t]*\([0-9.]\+\)\/.*$/\1/p}')
IP_JDG_NODE1=$(ping -c 1 jdg-node1 | gawk -F'[()]' '/PING/{print $2}')
IP_JDG_NODE2=$(ping -c 1 jdg-node2 | gawk -F'[()]' '/PING/{print $2}')
IP_JDG_NODE3=$(ping -c 1 jdg-node3 | gawk -F'[()]' '/PING/{print $2}')

cp -r -a /eapConfig/config/* $EAP_HOME/standalone/configuration
chmod -R 777 $EAP_HOME/standalone/config/*
#Start EAP
$EAP_HOME/bin/standalone.sh -b $IPADDR -c standalone.xml  \
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
