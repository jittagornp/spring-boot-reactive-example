# spring-boot-webflux-custom-error-handler 
ตัวอย่างการเขียน Spring-boot WebFlux Custom Error Handler 

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

    ...
}
```
- design ตามนี้้ [https://developer.pamarin.com/document/error/](https://developer.pamarin.com/document/error/) 

# 5. เขียน ExceptionHandler 
ตัวจัดการ Error แต่ละประเภท   

### ประกาศ interface 
```java
public interface ErrorResponseExceptionHandler<E extends Throwable> {

    Class<E> getTypeClass();

    ErrorResponse handle(ServerWebExchange exchange, E e);

}
```
### เขียน Adapter 
```java
public abstract class ErrorResponseExceptionHandlerAdapter<E extends Throwable> implements ErrorResponseExceptionHandler<E> {

    protected abstract ErrorResponse buildError(ServerWebExchange exchange, E e);

    private String getErrorCode(ServerWebExchange exchange) {
        return UUID.randomUUID().toString();
    }

    private ErrorResponse additional(ErrorResponse err, ServerWebExchange exchange, E e) {
        ServerHttpRequest httpReq = exchange.getRequest();
        err.setState(httpReq.getQueryParams().getFirst("state"));
        err.setErrorTimestamp(System.currentTimeMillis());
        err.setErrorCode(getErrorCode(exchange));
        return err;
    }

    @Override
    public ErrorResponse handle(ServerWebExchange exchange, E e) {
        ErrorResponse err = buildError(exchange, e);
        return additional(err, exchange, e);
    }

}
```
### implementation Error แต่ละประเภท

ตัวจากการ Exception  
```java
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception> {

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, Exception ex) {
        return ErrorResponse.serverError();
    }
}
```
ตัวจากการ ResponseStatusException 
```java
@Component
public class ErrorResponseResponseStatusExceptionHandler extends ErrorResponseExceptionHandlerAdapter<ResponseStatusException> {

    @Override
    public Class<ResponseStatusException> getTypeClass() {
        return ResponseStatusException.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, ResponseStatusException ex) {
        //400
        if (ex.getStatus() == HttpStatus.BAD_REQUEST) {
            return ErrorResponse.invalidRequest(ex.getMessage());
        }
        //401
        if (ex.getStatus() == HttpStatus.UNAUTHORIZED) {
            return ErrorResponse.unauthorized(ex.getMessage());
        }
        //403
        if (ex.getStatus() == HttpStatus.FORBIDDEN) {
            return ErrorResponse.accessDenied(ex.getMessage());
        }
        //404
        if (ex.getStatus() == HttpStatus.NOT_FOUND) {
            return ErrorResponse.notFound(ex.getMessage());
        }
        return ErrorResponse.serverError();
    }
}
```
- สามารถเพิ่ม class ตัวจัดการ Exception ใหม่ได้เรื่อย ๆ 

# 6. เขียน Resolver 
สำหรับ resolve error แต่ละประเภท   
  
ประกาศ interface
```java 
public interface ErrorResponseExceptionHandlerResolver {

    ErrorResponseExceptionHandler resolve(Throwable e);

}
```

implement interface  
```java 
@Component
public class DefaultErrorResponseExceptionHandlerResolver implements ErrorResponseExceptionHandlerResolver {

    private final Map<Class, ErrorResponseExceptionHandler> registry;

    private final ErrorResponseRootExceptionHandler rootExceptionHandler;

    @Autowired
    public DefaultErrorResponseExceptionHandlerResolver(List<ErrorResponseExceptionHandler> handlers, ErrorResponseRootExceptionHandler rootExceptionHandler) {
        this.registry = handlers.stream()
                .filter(handler -> handler.getTypeClass() != Exception.class)
                .collect(toMap(ErrorResponseExceptionHandler::getTypeClass, handler -> handler));
        this.rootExceptionHandler = rootExceptionHandler;
    }

    @Override
    public ErrorResponseExceptionHandler resolve(Throwable e) {
        ErrorResponseExceptionHandler handler = registry.get(e.getClass());
        if (handler == null) {
            return rootExceptionHandler;
        }
        return handler;
    }

}
```

# 7. เขียน WebExceptionHandler  
เป็นตัวจัดการ Global Exception ทุกประเภท ซึ่ง WebFlux จะโยน Exception เข้ามาที่นี่ 
```java
@Slf4j
@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

    private final ObjectMapper objectMapper;
    
    private final ErrorResponseExceptionHandlerResolver resolver;

    @Autowired
    public GlobalWebExceptionHandler(ObjectMapper objectMapper, ErrorResponseExceptionHandlerResolver resolver) {
        this.resolver = resolver;
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable e) {
        log.debug("Throwable class => {}", e.getClass().getName());
        ErrorResponse error = resolver.resolve(e).handle(exchange, e);
        error.setErrorUri("https://developer.pamarin.com/document/error/");
        return jsonResponse(
                ResponseEntity.status(HttpStatus.valueOf(error.getErrorStatus()))
                        .body(error),
                exchange
        );
    }

    public Mono<Void> jsonResponse(
            final ResponseEntity<ErrorResponse> entity,
            final ServerWebExchange exchange
    ) {
        final ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(entity.getStatusCode());
        response.getHeaders().addAll(entity.getHeaders());
        response.getHeaders().put(HttpHeaders.CONTENT_TYPE, Arrays.asList(MediaType.APPLICATION_JSON_UTF8_VALUE));
        try {
            final DataBuffer buffer = response.bufferFactory().wrap(objectMapper.writeValueAsBytes(entity.getBody()));
            return response.writeWith(Mono.just(buffer)).doOnError(error -> DataBufferUtils.release(buffer));
        } catch (final JsonProcessingException ex) {
            return Mono.error(ex);
        }
    }
}

```

# 8. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 9. Run 
``` shell 
$ mvn spring-boot:run
```

# 10. เข้าใช้งาน

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
