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
เพราะถ้าไม่แปลง error เอง spring จะพ่น json error ออกเป็นหน้าตาประมาณนี้   
```json
{
    "timestamp": "2019-06-26T13:54:15.687+0000",
    "path": "/login",
    "status": 400,
    "error": "Bad Request",
    "message": "Validation failed for argument at index 0 in method: public void com.pamarin.learning.webflux.controller.LoginController.login(com.pamarin.learning.webflux.model.LoginRequest), with 1 error(s): [Field error in object 'loginRequest' on field 'password': rejected value [pass]; codes [Length.loginRequest.password,Length.password,Length.java.lang.String,Length]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [loginRequest.password,password]; arguments []; default message [password],50,8]; default message [password must between 8 to 50 characters]] ",
    "errors": [
        {
            "codes": [
                "Length.loginRequest.password",
                "Length.password",
                "Length.java.lang.String",
                "Length"
            ],
            "arguments": [
                {
                    "codes": [
                        "loginRequest.password",
                        "password"
                    ],
                    "arguments": null,
                    "defaultMessage": "password",
                    "code": "password"
                },
                50,
                8
            ],
            "defaultMessage": "password must between 8 to 50 characters",
            "objectName": "loginRequest",
            "field": "password",
            "rejectedValue": "pass",
            "bindingFailure": false,
            "code": "Length"
        }
    ]
}
```
ซึ่งผมมองว่ามันไม่สวย แล้วข้อมูล information ต่าง ๆ ของ framework หรือ system หลุดออกมาด้วย เลยเขียน error เอง

# 6. เขียน error model
```java 
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

อ้างอิง error : [https://developer.pamarin.com/document/error/](https://developer.pamarin.com/document/error/)  

# 7. เขียน Controller Advice
เพื่อแปลง Error ไปเป็น format ตามที่เราต้องการ  
```java
@ControllerAdvice
public class ErrorControllerAdvice {

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<ErrorResponse>> validationError(WebExchangeBindException ex, ServerWebExchange exchange) {
        return Mono.just(
                ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(
                                ErrorResponse.builder()
                                        .error("bad_request")
                                        .errorDescription("Validate fail")
                                        .errorStatus(HttpStatus.BAD_REQUEST.value())
                                        .errorTimestamp(System.currentTimeMillis())
                                        .errorUri("https://developer.pamarin.com/document/error/")
                                        .errorCode(UUID.randomUUID().toString())
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
                                        .build()
                        )
        );
    }
    
    ...
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

# 11. ลองยิง request ทดสอบผ่าน postman
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
