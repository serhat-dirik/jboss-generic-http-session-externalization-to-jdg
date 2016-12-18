cp /deployments/jdg-service4tomcat.war /usr/local/tomcat/webapps/jdg-service.war
cp /tomcatConfig/config/* /usr/local/tomcat/conf
/usr/local/tomcat/bin/catalina.sh run
