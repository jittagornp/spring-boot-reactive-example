# https://hub.docker.com/_/openjdk
FROM openjdk:11-jre-slim
EXPOSE 8080
ADD target/*.jar /app.jar
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
