#!/bin/bash

#build source code (.jar file)
docker run --rm -v $(pwd):/app -v /root/.m2:/root/.m2 maven:3.6.2-jdk-11 mvn package -f /app/pom.xml

# remove container and images
docker stop websocket-cors || true
docker rm websocket-cors || true
docker rmi websocket-cors || true

# build docker image
docker build -t websocket-cors .

# run container
docker run -d --net=pamarin --name=websocket-cors -p 80:8080 --restart=always websocket-cors
