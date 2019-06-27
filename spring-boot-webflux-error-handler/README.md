# spring-boot-webflux-error-handler 
ตัวอย่างการเขียน Spring-boot WebFlux Error Handler 

# 1. เพิ่ม Dependencies

pom.xml 
``` xml
...
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.1.5.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>

...
```

# 2. เขียน Main Class 

``` java
@SpringBootApplication
@ComponentScan(basePackages = {"com.pamarin"}) 
public class AppStarter {

    public static void main(String[] args) {
        SpringApplication.run(AppStarter.class, args);
    }

}
```

# 3. เขียน Controller
``` java
@RestController
public class HomeController {


    @GetMapping({"", "/"})
    public Mono<String> hello() {
       throw new RuntimeException();
    }

}
```
- ลอง throw RuntimeException ดู 
- เราสามารถ throw Exception ประเภทอื่น ๆ ตามที่เราต้องการได้ 

# 4. เขียน Error Model 
``` java 
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private String error;

    private int errorStatus;

    private String errorDescription;

    private long errorTimestamp;

    private String errorUri;

    private String errorCode;

    private String state;

    private List<Field> errorFields;

    public List<Field> getErrorFields() {
        if (errorFields == null) {
            errorFields = new ArrayList<>();
        }
        return errorFields;
    }

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Field {

        private String name;

        private String code;

        private String description;

    }
}
```
- design ตามนี้้ [https://developer.pamarin.com/document/error/](https://developer.pamarin.com/document/error/) 

# 5. เขียน Controller Advice 
เป็น Global Exception handler
```java
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<ErrorResponse>> handle(Exception ex, ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(
                                ErrorResponse.builder()
                                        .error("server_error")
                                        .errorDescription("Internal Server Error")
                                        .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                        .errorTimestamp(System.currentTimeMillis())
                                        .errorUri("https://developer.pamarin.com/document/error/")
                                        .errorCode(UUID.randomUUID().toString())
                                        .build()
                        )
        );
    }

}
```
- เราสามารถ เพิ่ม method เพื่อ catch exception อื่น ๆ ได้ 

# 6. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 7. Run 
``` shell 
$ mvn spring-boot:run
```

# 8. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
