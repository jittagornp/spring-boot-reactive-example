# spring-boot-webflux-validation
ตัวอย่างการเขียน Spring-boot WebFlux Validation 

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

# 3. เขียน Model & ใส่ Validator annotation
```java
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank(message = "require username")
    @Length(max = 50, message = "username more than {max} characters")
    private String username;

    @NotBlank(message = "require password")
    @Length(min = 8, max = 50, message = "password must between {min} to {max} characters")
    private String password;

}
```
- @NotBlank คือ ห้ามเป็น null หรือ ค่าว่าง 
- @Length คือ ต้องมีขนาดตามที่ระบุ  

อ่านเพิ่มเติม : [https://beanvalidation.org/](https://beanvalidation.org/)  

# 4. เขียน Controller
``` java
@Slf4j
@RestController
public class LoginController {

    @PostMapping("/login")
    public void login(@RequestBody @Validated LoginRequest req) {
        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
```
- สังเกตว่าตรง input method ใน controller มี @Validated เพื่อบอกว่าให้ validate input ที่เป็น request body (json) ด้วย   

# 5. เขียน error handler

ตัวจัดการ Error ให้เขียนรูู้จากหัวข้อ [spring-boot-webflux-custom-error-handler](../spring-boot-webflux-custom-error-handler)

# 6. เพิ่มตัวจัดการ Error สำหรับ WebExchangeBindException
```java 
@Component
public class ErrorResponseWebExchangeBindExceptionHandler extends ErrorResponseExceptionHandlerAdapter<WebExchangeBindException> {

    @Override
    public Class<WebExchangeBindException> getTypeClass() {
        return WebExchangeBindException.class;
    }

    @Override
    protected ErrorResponse buildError(ServerWebExchange exchange, WebExchangeBindException ex) {
        return ErrorResponse.builder()
                .error("bad_request")
                .errorDescription("Validate fail")
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorFields(
                        ex.getFieldErrors()
                                .stream()
                                .map(f -> {
                                    return ErrorResponse.Field.builder()
                                            .name(f.getField())
                                            .code(f.getCode())
                                            .description(f.getDefaultMessage())
                                            .build();
                                })
                                .collect(toList())
                )
                .build();
    }
}
```

# 7. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 8. Run 
``` shell 
$ mvn spring-boot:run
```

# 9. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)

# 10. ลองยิง request ทดสอบผ่าน postman
> POST : http://localhost:8080/login  
  
ได้ผลลัพธ์
```json
{
    "error": "bad_request",
    "state": null,
    "error_status": 400,
    "error_description": "Validate fail",
    "error_timestamp": 1561611745664,
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_code": "1e71a8a1-48eb-41d1-b8c0-101a067dd176",
    "error_field": [
        {
            "name": "password",
            "code": "Length",
            "description": "password must between 8 to 50 characters"
        }
    ]
}
```
