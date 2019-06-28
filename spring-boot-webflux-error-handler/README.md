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

    @JsonProperty("error_status")
    private int errorStatus;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_timestamp")
    private long errorTimestamp;

    @JsonProperty("error_uri")
    private String errorUri;

    @JsonProperty("error_code")
    private String errorCode;

    private String state;

    @JsonProperty("error_field")
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

# 5. เขียน WebExceptionHandler 
เป็น Global Exception handler
```java
@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Autowired
    public GlobalWebExceptionHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ResponseEntity<ErrorResponse> entity = ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
                .body(
                        ErrorResponse.builder()
                                .error("server_error")
                                .errorDescription("Internal Server Error")
                                .errorStatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                                .errorTimestamp(System.currentTimeMillis())
                                .errorUri("https://developer.pamarin.com/document/error/")
                                .errorCode(UUID.randomUUID().toString())
                                .build()
                );
        return setHttpResponse(entity, exchange);
    }

    public Mono<Void> setHttpResponse(
            final ResponseEntity<ErrorResponse> entity,
            final ServerWebExchange exchange
    ) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(entity.getStatusCode());
        response.getHeaders().addAll(entity.getHeaders());
        try {
            final DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(entity.getBody()));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        } catch (final JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }
}
```

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

# ผลลัพธ์ที่ได้
```json
{
    "error": "server_error",
    "state": null,
    "error_status": 500,
    "error_description": "Internal Server Error",
    "error_timestamp": 1561611398635,
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_code": "637438dc-6e67-4706-9431-cfcd4a889011",
    "error_field": []
}
```
