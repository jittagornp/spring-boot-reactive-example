# spring-boot-webflux-postgresql
Spring-boot WebFlux Postgresql example 


# Build & Run

cd to root of project directory 

``` shell 
$ mvn clean install

$ mvn spring-boot:run \
    -Dserver.port=8080 \
    -Dspring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/<DATABASE_NAME>?sslmode=require \
    -Dspring.datasource.username=<DATABASE_USERNAME> \
    -Dspring.datasource.password=<DATABASE_PASSWORD> \
    -Dspring.jpa.properties.hibernate.default_schema=<DATABASE_SCHEMA>
```
