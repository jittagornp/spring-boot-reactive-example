# https://hub.docker.com/_/openjdk
FROM openjdk:21-slim
EXPOSE 8080
ADD target/*.jar /app.jar
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
