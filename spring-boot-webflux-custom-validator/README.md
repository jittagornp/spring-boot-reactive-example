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

# 3. เขียน Custom Validator  

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
- Validator ที่เราทำการ Custom เองจะอยู่ในรูปของ annotation และมี logic หรือ class validator ผูกกับ annotation นั้น ๆ เพื่อทำการ validate ข้อมูลตามที่เราต้องการ  


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

 # 6. เขียน exception handler 

เหมือนหัว [spring-boot-webflux-validation](../spring-boot-webflux-validation)
 
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
> POST : http://localhost:8080/register  
  
ได้ผลลัพธ์
```json
{
    "error": "bad_request",
    "state": null,
    "error_status": 400,
    "error_description": "Validate fail",
    "error_timestamp": 1561611940363,
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_code": "876ba285-c0f8-4fd5-b37e-c109afb5c711",
    "error_field": [
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
            "name": "email",
            "code": "NotBlank",
            "description": "require email"
        },
        {
            "name": "confirmPassword",
            "code": "PasswordEqualsConfirmPassword",
            "description": "password not equals confirm password"
        }
    ]
}
```
 
