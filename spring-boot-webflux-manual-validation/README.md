# spring-boot-webflux-manual-validation
ตัวอย่างการเขียน Spring-boot WebFlux Manual Validation 

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

# 4. เขียน Manual Validator 
```java
public class ManualValidation {

    private final Object object;
    private final String objectName;
    private Class<?> groupClass;

    private ManualValidation(Object object, String objectName) {
        this.object = object;
        this.objectName = objectName;
    }

    /**
     * @param object
     * @param objectName
     * @return ManualValidator instance
     */
    public static ManualValidation of(Object object, String objectName) {
        return new ManualValidation(object, objectName);
    }

    /**
     * @param object
     * @return ManualValidator instance
     */
    public static ManualValidation of(Object object) {
        return of(object, "");
    }

    /**
     * @param groupClass
     * @return ManualValidator instance
     */
    public ManualValidation group(Class<?> groupClass) {
        this.groupClass = groupClass;
        return this;
    }

    /**
     * @param validator
     * @throws BindException
     */
    public void validate(Validator validator) throws BindException {
        BindingResult bindingResult = new BindException(object, objectName);
        WebExchangeBindException ex = new WebExchangeBindException(null, bindingResult);
        ValidationUtils.invokeValidator(validator, object, ex, groupClass, Default.class);
        if (ex.hasErrors()) {
            throw ex;
        }
    }
}
```

# 5. เขียน Controller
``` java
@Slf4j
@RestController
public class LoginController {

    private final Validator validator;

    @Autowired
    public LoginController(Validator validator) {
        this.validator = validator;
    }

    @PostMapping("/login")
    public void login(@RequestBody LoginRequest req) throws BindException, NoSuchMethodException {

        ManualValidation.of(req).validate(validator);

        log.debug("username => {}", req.getUsername());
        log.debug("password => {}", req.getPassword());
    }

}
```

# 6. เขียน error handler

ตัวจัดการ Error ให้เรียนรู้จากหัวข้อ [spring-boot-webflux-custom-error-handler](../spring-boot-webflux-custom-error-handler)

# 7. เพิ่มตัวจัดการ Error สำหรับ WebExchangeBindException
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
