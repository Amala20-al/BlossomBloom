FROM maven:3.8.6-openjdk-8 AS build

WORKDIR /app

COPY . .

WORKDIR /app/BlossomBloom

RUN mvn clean package

FROM tomcat:9.0-jdk8

RUN rm -rf /usr/local/tomcat/webapps/*

COPY --from=build /app/BlossomBloom/target/*.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]
