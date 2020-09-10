# spring-boot-reactive-error-handler 

> ตัวอย่างการเขียน Spring-boot Reactive Error Handler 

# 1. เพิ่ม Dependencies และ Plugins

pom.xml 
``` xml
...
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.3.2.RELEASE</version>
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

<build>
    <plugins>
        <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
            <executions>        
                <execution>            
                    <id>build-info</id>            
                    <goals>                
                        <goal>build-info</goal>            
                    </goals>        
                    <configuration>                
                        <additionalProperties>                    
                            <java.version>${java.version}</java.version>                                   
                        </additionalProperties>            
                    </configuration>        
                </execution>    
            </executions>
        </plugin>
    </plugins>
</build>
...
```

# 2. เขียน Main Class 

``` java
@SpringBootApplication
@ComponentScan(basePackages = {"me.jittagornp"})
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
       throw new RuntimeException("Error on /");
    }

}
```
- ลอง throw RuntimeException ดู 
- เราสามารถ throw Exception ประเภทอื่น ๆ ตามที่เราต้องการได้ 

# 4. เขียน Error Model 

เพื่อใช้เป็น Error Response 

``` java 
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty("error")
    private String error;

    @JsonProperty("error_status")
    private int errorStatus;

    @JsonProperty("error_description")
    private String errorDescription;

    @JsonProperty("error_at")
    private LocalDateTime errorAt;

    @JsonProperty("error_trace_id")
    private String errorTraceId;

    @JsonProperty("error_uri")
    private String errorUri;

    @JsonProperty("error_on")
    private String errorOn;

    @JsonProperty("error_fields")
    private List<Field> errorFields;

    @JsonProperty("error_data")
    private Map<String, Object> errorData;

    @JsonProperty("state")
    private String state;

    ...
    
}
```
- Design ตามนี้ [https://developer.pamarin.com/document/error/](https://developer.pamarin.com/document/error/) 

# 5. เขียน WebExceptionHandler 
เป็นตัวจัดการ Global Exception ทุกประเภท ซึ่ง WebFlux จะโยน Exception เข้ามาที่นี่ 
```java
@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class ServerWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable e) {
        log.warn("error => ", e);
        final ErrorResponse err = ErrorResponse.serverError(e.getMessage());
        err.setErrorAt(LocalDateTime.now());
        err.setErrorTraceId(UUID.randomUUID().toString());
        err.setErrorUri("https://developer.pamarin.com/document/error/");
        return produceJson(err, exchange);
    }

    private void setHeaders(final ErrorResponse err, final ServerHttpResponse response){
        final HttpHeaders headers = response.getHeaders();
        response.setStatusCode(HttpStatus.valueOf(err.getErrorStatus()));
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        } catch (UnsupportedOperationException e) {

        }
    }

    public Mono<Void> produceJson(final ErrorResponse err, final ServerWebExchange exchange) {
        return Mono.defer(() -> {
            try {
                final ServerHttpResponse response = exchange.getResponse();
                setHeaders(err, response);
                final String json = objectMapper.writeValueAsString(err);
                final DataBuffer buffer = response.bufferFactory().wrap(json.getBytes(Charset.forName("utf-8")));
                return response.writeWith(Mono.just(buffer))
                        .doOnError(e -> DataBufferUtils.release(buffer));
            } catch (final Exception e) {
                return Mono.error(e);
            }
        });
    }

}
```

# 6. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
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
    "error_status": 500,
    "error_description": "Error on /",
    "error_at": "2020-09-08T20:06:42.540141",
    "error_trace_id": "8915f725-1974-4cef-a730-2a416fdcd8bb",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": null,
    "error_fields": [ ],
    "error_data": { },
    "state": null
}
```
