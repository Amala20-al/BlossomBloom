# This Dockerfile is NOT used for deployment
# Safe fallback only - DO NOT USE FOR RENDER BUILD

FROM tomcat:9.0-jdk8

RUN rm -rf /usr/local/tomcat/webapps/*

# Use wildcard so it doesn't break if WAR name changes
COPY target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
