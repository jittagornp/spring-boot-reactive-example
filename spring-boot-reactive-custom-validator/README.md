# spring-boot-reactive-custom-validator

> ตัวอย่างการเขียน Spring-boot WebFlux Custom Validator 

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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-validation</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>

    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
        <version>21.0</version>
        <type>jar</type>
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

### หมายเหตุ

- `spring-boot-starter-validation` เป็น Dependency สำหรับการทำ Validation (ตั้งแต่ Spring-boot 2.3 RELEASE เป็นต้นไป มีการตัด Validation `javax.validation.*` ออก ต้อง Add Dependencies เข้าไปเอง อ้างอิงจาก [Spring Boot 2.3 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-2.3-Release-Notes#validation-starter-no-longer-included-in-web-starters))
- `com.google.guava` เป็น Dependency Utilities Codes ของ Google ชื่อ Guava

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

        private static final String REGEXP = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        @Override
        public void initialize(Email annotation) {

        }

        @Override
        public boolean isValid(String email, ConstraintValidatorContext context) {
            if (!hasText(email)) {
                return true;
            }

            return Pattern.compile(REGEXP).matcher(email).matches();
        }

        public static String getEmailRegExp() {
            return REGEXP;
        }
    }
}
```
- Validator ที่เราทำการ Custom เองจะอยู่ในรูปของ annotation และมี logic หรือ class validator ผูกกับ annotation นั้น ๆ เพื่อทำการ validate ข้อมูลตามที่เราต้องการ  


# 4. เขียน Model & ใส่ Validator annotation
```java
@Data
@PasswordEqualsConfirmPassword
@PasswordNotEqualsEmail
public class RegisterForm implements PasswordEqualsConfirmPassword.Model, PasswordNotEqualsEmail.Model {

    @NotBlank(message = "Required")
    @Email(message = "Invalid format")
    private String email;

    @NotBlank(message = "Required")
    @AtLeastPassword
    @Length(min = 8, max = 50, message = "At least {min} characters")
    private String password;

    @NotBlank(message = "Required")
    @Length(min = 8, max = 50, message = "At least {min} characters")
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

เหมือนหัวข้อ [spring-boot-reactive-validation](../spring-boot-reactive-validation)
 
# 7. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
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
    "error": "invalid_request",
    "error_status": 400,
    "error_description": "Validate fail",
    "error_at": "2020-09-13T16:05:47.880872",
    "error_trace_id": "EA135789",
    "error_uri": "https://developer.pamarin.com/document/error/",
    "error_on": "0",
    "error_fields": [
        {
            "name": "email",
            "code": "email",
            "description": "Invalid format"
        },
        {
            "name": "confirmPassword",
            "code": "password_equals_confirm_password",
            "description": "Password not equals Confirm password"
        },
        {
            "name": "password",
            "code": "length",
            "description": "At least 8 characters"
        },
        {
            "name": "password",
            "code": "password_not_equals_email",
            "description": "Password equals Email"
        },
        {
            "name": "password",
            "code": "at_least_password",
            "description": "At least one lower case letter, one upper case letter, one special character and one numeric digit"
        },
        {
            "name": "confirmPassword",
            "code": "not_blank",
            "description": "Required"
        }
    ],
    "error_data": {},
    "state": null
}
```
 
