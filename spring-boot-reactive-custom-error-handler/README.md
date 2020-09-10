# spring-boot-reactive-custom-error-handler 

> ตัวอย่างการเขียน Spring-boot Reactive Custom Error Handler 

# ปัญหา

เวลาที่จะเริ่ม Project ใหม่ Module ใหม่ หรือมีคนใหม่เข้ามาร่วมทีมกัน สิ่งแรก ๆ ที่พวกผมจะเอามาคุยกัน ก็คือเรื่องการจัดการ Error ใน Code
  
เราจะสร้างข้อตกลงร่วมกันเลยว่า
ไม่ว่าจะเขียนโปรแกรมด้วยภาษาโปรแกรมมิ่งอะไรก็ตาม
  
ทีมเราจะต้องมี Error Format เป็นแบบนี้เท่านั้น 
ต่อให้เขียน Java, NodeJS, PHP, Go, Python, C#  API จะต้องพ่น Error Format นี้เท่านั้น (ทำยังไงไม่รู้ รู้แต่ต้องไปทำมาให้ได้)
  
ห้ามต่างคนต่างทำ ต่าง Format นอกจากที่ตกลงกัน 
เพราะคนที่ลำบากคือคนที่เอา API เราไปใช้งาน
  
และจากที่ทำงานมาหลาย ๆ ที่ ผมก็ไม่ค่อยเห็นใครใส่ใจเรื่องนี้ซักเท่าไหร่ มักที่จะใช้ Error Format ตามที่ภาษาหรือ Framework นั้นมีมาให้เลย 
ปัญหามันอยู่ตรงที่พอภาษา หรือ Framework ที่ใช้ มีการ Update Version ตัว Error Format ก็เปลี่ยนตามไปด้วย Code ที่เคยใช้ได้ ก็อาจจะใช้ไม่ได้ในอนาคต
  
ซึ่งผมมองว่ามันเป็นเรื่องสำคัญเรื่องนึงที่ไม่ควรจะมองข้ามน่ะ
  
ตัวผมเองเป็นคนที่เขียน Code ด้วย Java Spring-boot Reactive
ตอนนี้ ผมใช้วิธีการจัดการ Error และ Exception ใน Code เป็นแบบนี้
  
เลยคิดว่าน่าจะเป็นประโยชน์ต่อคนอื่น เลยพยายามทำเป็นตัวอย่างเก็บไว้ครับ

# การออกแบบ Error Format 

> Error Format นี้ Design ตาม OAuth 2.0 (RFC6749) เนื่องจาก Code Authen บางตัวเราใช้ OAuth เป็น Core เราเลยทำ Error ให้เหมือนกับที่ OAuth ใช้ 

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
        throw new AuthenticationException();
    }

    @GetMapping("/invalidUsernamePassword")
    public Mono<String> invalidUsernamePassword() {
        throw new InvalidUsernamePasswordException();
    }

    @GetMapping("/serverError")
    public Mono<String> serverError() {
        throw new RuntimeException();
    }

}
```
- ลอง throw AuthenticationException, InvalidUsernamePasswordException และ RuntimeException ดู 
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

# 5. เขียน ExceptionHandler 
ตัวจัดการ Error แต่ละประเภท   

### ประกาศ interface 
```java
public interface ErrorResponseExceptionHandler<E extends Throwable> {

    Class<E> getTypeClass();

    Mono<ErrorResponse> handle(final ServerWebExchange exchange, final E e);

}
```
### เขียน Adapter 

เพื่อใช้เป็นตัวกลางในการแปลง Exception

```java
public abstract class ErrorResponseExceptionHandlerAdapter<E extends Throwable> implements ErrorResponseExceptionHandler<E> {

    protected abstract Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final E e);

    private String getErrorTraceId(final ServerWebExchange exchange) {
        return UUID.randomUUID()
                .toString()
                .replace("-", "")
                .substring(0, 8)
                .toUpperCase();
    }
    
    private HttpStatus toHttpStatus(final int statusCode){
        return (statusCode == 0)
                ? HttpStatus.INTERNAL_SERVER_ERROR
                : HttpStatus.valueOf(statusCode);
    }

    private Mono<ErrorResponse> additional(final ErrorResponse err, final ServerWebExchange exchange, final E e) {
        return Mono.fromCallable(() -> {
            final ServerHttpRequest httpReq = exchange.getRequest();
            final ServerHttpResponse httpResp = exchange.getResponse();
            err.setState(httpReq.getQueryParams().getFirst("state"));
            err.setErrorAt(now());
            if (!hasText(err.getErrorTraceId())) {
                err.setErrorTraceId(getErrorTraceId(exchange));
            }
            err.setErrorOn("0");
            err.setErrorUri("https://developer.pamarin.com/document/error/");
            httpResp.setStatusCode(toHttpStatus(err.getErrorStatus()));
            return err;
        });
    }

    @Override
    public Mono<ErrorResponse> handle(final ServerWebExchange exchange, final E e) {
        return buildError(exchange, e)
                .flatMap(err -> additional(err, exchange, e));
    }
}
```
### implementation Error แต่ละประเภท

ตัวจัดการ Exception  
```java
@Component
public class ErrorResponseRootExceptionHandler extends ErrorResponseExceptionHandlerAdapter<Exception> {

    @Override
    public Class<Exception> getTypeClass() {
        return Exception.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final Exception e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.serverError();
        });
    }
}
```
ตัวจัดการ AuthenticationException 
```java
@Component
public class ErrorResponseAuthenticationExceptionHandler extends ErrorResponseExceptionHandlerAdapter<AuthenticationException> {

    @Override
    public Class<AuthenticationException> getTypeClass() {
        return AuthenticationException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final AuthenticationException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.unauthorized();
        });
    }
}
```
ตัวจัดการ InvalidUsernamePasswordException 
```java
@Component
public class ErrorResponseInvalidUsernamePasswordExceptionHandler extends ErrorResponseExceptionHandlerAdapter<InvalidUsernamePasswordException> {

    @Override
    public Class<InvalidUsernamePasswordException> getTypeClass() {
        return InvalidUsernamePasswordException.class;
    }

    @Override
    protected Mono<ErrorResponse> buildError(final ServerWebExchange exchange, final InvalidUsernamePasswordException e) {
        return Mono.fromCallable(() -> {
            return ErrorResponse.builder()
                    .error("invalid_username_password")
                    .errorDescription("invalid username or password")
                    .errorStatus(HttpStatus.BAD_REQUEST.value())
                    .build();
        });
    }
}
```
- สามารถเพิ่ม class ตัวจัดการ Exception ใหม่ได้เรื่อย ๆ 

# 6. เขียน Resolver 
สำหรับ resolve error แต่ละประเภท   
  
### ประกาศ interface
```java 
public interface ErrorResponseExceptionHandlerResolver {

    Mono<ErrorResponseExceptionHandler> resolve(final Throwable e);

}
```

### implement interface  
```java 
@Slf4j
@Component
public class DefaultErrorResponseExceptionHandlerResolver implements ErrorResponseExceptionHandlerResolver {

    private final Map<Class, ErrorResponseExceptionHandler> registry;

    private final ErrorResponseRootErrorHandler rootErrorHandler;

    private final ErrorResponseRootExceptionHandler rootExceptionHandler;

    @Autowired
    public DefaultErrorResponseExceptionHandlerResolver(
            final List<ErrorResponseExceptionHandler> handlers,
            final ErrorResponseRootErrorHandler rootErrorHandler,
            final ErrorResponseRootExceptionHandler rootExceptionHandler
    ) {
        this.registry = handlers.stream()
                .filter(this::ignoreHandler)
                .collect(toMap(ErrorResponseExceptionHandler::getTypeClass, handler -> handler));
        this.rootErrorHandler = rootErrorHandler;
        this.rootExceptionHandler = rootExceptionHandler;
    }

    private boolean ignoreHandler(final ErrorResponseExceptionHandler handler) {
        return !(handler.getTypeClass() == Exception.class
                || handler.getTypeClass() == Error.class);
    }

    @Override
    public Mono<ErrorResponseExceptionHandler> resolve(final Throwable e) {
        ErrorResponseExceptionHandler handler = registry.get(e.getClass());
        if (handler == null) {
            if (e instanceof Error) {
                handler = rootErrorHandler;
            } else {
                handler = rootExceptionHandler;
            }
        }
        log.debug("handler => {}", handler.getClass().getName());
        return Mono.just(handler);
    }

}
```

# 7. เขียน ErrorResponseProducer 

เพื่อใช้สำหรับแปลง/พ่น Error ตาม Format ที่เราต้องการ เช่น Json, Xml, Html เป็นต้น 

### ประกาศ interface
```java
public interface ErrorResponseProducer {

    Mono<Void> produce(final ErrorResponse err, final ServerWebExchange exchange);

}
```

### implement interface  
สมมติเราใช้ format เป็น Json (ถ้ามี format อื่น ๆ หรือเงื่อนไขอื่น ๆ ก็ implement ได้ตามต้องการ)
```java
@Component
@RequiredArgsConstructor
public class JsonErrorResponseProducer implements ErrorResponseProducer {

    private final ObjectMapper objectMapper;

    private void setHeaders(final ErrorResponse err, final ServerHttpResponse response){
        final HttpHeaders headers = response.getHeaders();
        response.setStatusCode(HttpStatus.valueOf(err.getErrorStatus()));
        try {
            headers.put(HttpHeaders.CONTENT_TYPE, Collections.singletonList(MediaType.APPLICATION_JSON_VALUE));
        } catch (UnsupportedOperationException e) {

        }
    }

    @Override
    public Mono<Void> produce(final ErrorResponse err, final ServerWebExchange exchange) {
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

# 8. เขียน WebExceptionHandler  
เป็นตัวจัดการ Global Exception ทุกประเภท ซึ่ง WebFlux จะโยน Exception เข้ามาที่นี่ 
```java
@Slf4j
@Component
@Order(-2)
@RequiredArgsConstructor
public class ServerWebExceptionHandler implements WebExceptionHandler {

    private final ErrorResponseProducer producer;

    private final ErrorResponseExceptionHandlerResolver resolver;

    @Override
    public Mono<Void> handle(final ServerWebExchange exchange, final Throwable e) {
        log.warn("error => ", e);
        return resolver.resolve(e)
                .flatMap(handler -> (Mono<ErrorResponse>)handler.handle(exchange, e))
                .flatMap(err -> producer.produce(err, exchange));
    }
}
```

# 9. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
```

# 10. Run 
``` shell 
$ mvn spring-boot:run
```

# 11. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)

# ผลลัพธ์ที่ได้
```json
{
    "error": "unauthorized",
    "error_status": 401,
    "error_description": "Please login",
    "error_at": "2020-09-10T10:02:36.591176",
    "error_trace_id": "1463A078",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": "0",
    "error_fields": [ ],
    "error_data": { },
    "state": null
}
```

ลองทดสอบอีกตัวอย่าง [http://localhost:8080/invalidUsernamePassword](http://localhost:8080/invalidUsernamePassword)

```json
{
    "error": "invalid_username_password",
    "error_status": 400,
    "error_description": "invalid username or password",
    "error_at": "2020-09-09T22:14:02.377062",
    "error_trace_id": "59C1D7E4",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": "0",
    "error_fields": [ ],
    "error_data": { },
    "state": null
}
```

ลองทดสอบอีกตัวอย่าง [http://localhost:8080/serverError](http://localhost:8080/serverError)

```json
{
    "error": "server_error",
    "error_status": 500,
    "error_description": "Unknown error",
    "error_at": "2020-09-09T22:35:23.914991",
    "error_trace_id": "5056E04D",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": "0",
    "error_fields": [ ],
    "error_data": { },
    "state": null
}
```

ลองทดสอบอีกตัวอย่าง [http://localhost:8080/unknown](http://localhost:8080/unknown)

```json
{
    "error": "not_found",
    "error_status": 404,
    "error_description": "not found",
    "error_at": "2020-09-09T22:39:38.638358",
    "error_trace_id": "C318CF63",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": "0",
    "error_fields": [ ],
    "error_data": { },
    "state": null
}
```

