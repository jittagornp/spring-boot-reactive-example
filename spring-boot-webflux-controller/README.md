# spring-boot-webflux-mongodb
Spring-boot WebFlux Mongodb example 


# Build & Run

cd to root of project directory 

``` shell 
$ mvn clean install

$ mvn spring-boot:run \
    -Dserver.port=8080 \
    -Dspring.data.mongodb.uri=mongodb://<DATABASE_USERNAME>:<DATABASE_PASSWORD>@<DATABASE_HOST>:<DATABASE_PORT>/<DATABASE_NAME>
```
