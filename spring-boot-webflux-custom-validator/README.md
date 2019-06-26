# spring-boot-webflux-custom-validator
ตัวอย่างการเขียน Spring-boot WebFlux Custom Validator 

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

# 3. Custom Validator  

```java 
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = Validator.class)
public @interface Email {

    String message() default "invalid email";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Component
    public class Validator implements ConstraintValidator<Email, String> {

        private static final String REGX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        @Override
        public void initialize(Email annotation) {

        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (!hasText(email)) {
                return true;
            }

            return Pattern.compile(REGX).matcher(email).matches();
        }

        public boolean isValid(String email) {
            return isValid(email, null);
        }

        public static String getEmailRegExp() {
            return REGX;
        }
    }
}

```


# 4. เขียน Model & ใส่ Validator annotation
```java
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@PasswordEqualsConfirmPassword
@PasswordNotEqualsEmail
public class RegisterForm implements PasswordEqualsConfirmPassword.Model, PasswordNotEqualsEmail.Model {

    @NotBlank(message = "require email")
    @Email(message = "invalid format")
    private String email;

    @AtLeastPassword
    @Length(min = 8, max = 50, message = "at least {min} characters")
    private String password;

    @Length(min = 8, max = 50, message = "at least {min} characters")
    private String confirmPassword;

}

```

# 5. เขียน Controller
``` java
@Slf4j
@RestController
public class RegisterController {

    @PostMapping("/register")
    public void register(@RequestBody @Validated RegisterForm req) {
        log.debug("email => {}", req.getEmail());
        log.debug("password => {}", req.getPassword());
    }

}

```
- สังเกตว่าตรง input method ใน controller มี @Validated เพื่อบอกว่าให้ validate input ที่เป็น request body (json) ด้วย   

# 6. เขียน error model
```java 
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
ได้ผลลัพธ์
```json
{
    "error": "bad_request",
    "errorStatus": 400,
    "errorDescription": "Validate fail",
    "errorTimestamp": 1561563410755,
    "errorUri": "https://developer.pamarin.com/document/error/",
    "errorCode": "678ac1e7-83bc-4325-9830-9757f78562c3",
    "state": null,
    "errorFields": [
        {
            "name": "password",
            "code": "Length",
            "description": "at least 8 characters"
        },
        {
            "name": "password",
            "code": "AtLeastPassword",
            "description": "at least one lower case letter, one upper case letter, one special character and one numeric digit"
        },
        {
            "name": "confirmPassword",
            "code": "PasswordEqualsConfirmPassword",
            "description": "password not equals confirm password"
        },
        {
            "name": "email",
            "code": "NotBlank",
            "description": "require email"
        }
    ]
}
```
 
