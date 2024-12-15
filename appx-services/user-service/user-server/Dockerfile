# Base Alpine Linux based image with OpenJDK JRE only
FROM openjdk:8-jre-alpine
RUN apk add --no-cache curl
# copy application WAR (with libraries inside)
COPY target/echo-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 8080
# specify default command
CMD ["/usr/bin/java", "-jar", "/app.jar"]