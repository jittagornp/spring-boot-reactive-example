# spring-boot-webflux-redis
Spring-boot WebFlux Redis example 


# Build & Run

cd to root of project directory 

``` shell 
$ mvn clean install

$ mvn spring-boot:run \
    -Dserver.port=8080 \
    -Dspring.redis.url=redis://<REDIS_PASSWORD>@<REDIS_HOST>:<REDIS_PORT>
```
