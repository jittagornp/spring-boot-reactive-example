# spring-boot-reactive-form-data

> ตัวอย่างการเขียน Spring-boot Reactive Form Data

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

หมายเหตุ lombox เป็น annotation code generator ตัวนึง

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

# 3. เขียน Model หรือ DTO
```java
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterForm {

    private String firstName;

    private String lastName;

    private String email;
}
```

# 4. เขียน Controller
``` java
@Slf4j
@RestController
public class RegisterFormController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }

    @PostMapping(value = "/register1", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Mono<RegisterForm> register1(final RegisterForm form) {
        return Mono.just(form);
    }

    @PostMapping(value = "/register2", consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    public Mono<RegisterForm> register2(final ServerWebExchange exchange) {
        return exchange.getFormData()
                .map(formData -> {
                    return RegisterForm.builder()
                            .firstName(formData.getFirst("firstName"))
                            .lastName(formData.getFirst("lastName"))
                            .email(formData.getFirst("email"))
                            .build();
                });
    }

    @PostMapping(value = "/register3", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public Mono<RegisterForm> register3(final RegisterForm form) {
        return Mono.just(form);
    }
}
```

# 5. Build Code
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
```

# 6. Run 
``` shell 
$ mvn spring-boot:run
```

# 7. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)

# ทดสอบ

ลองยิง postman POST ไปยัง 
 
- `/register1` (Content-Type : application/x-www-form-urlencoded)

 ![](result-register1.png)

 - `/register2` (Content-Type : application/x-www-form-urlencoded)

 ![](result-register2.png)

 - `/register3` (Content-Type : multipart/form-data; boundary=...)

 ![](result-register3.png)
