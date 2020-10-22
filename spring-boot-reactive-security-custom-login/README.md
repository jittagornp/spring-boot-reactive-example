# spring-boot-reactive-security-custom-login

> ตัวอย่างการเขียน Spring-boot Reactive Custom Login  

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

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
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
- ในที่นี้เราจะใช้ Thymleaf ทำ View (Server Side) Rendering น่ะครับ  

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

# 3. Config Spring-Security 

```java
@Slf4j
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(final ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .authorizeExchange()
                .pathMatchers("/login").permitAll()
                .anyExchange().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .logout()
                .logoutUrl("/logout")
                .requiresLogout(ServerWebExchangeMatchers.pathMatchers(HttpMethod.GET, "/logout"))
                .and()
                .build();
    }

    @Bean
    public ReactiveUserDetailsService reactiveUserDetailsService(final PasswordEncoder passwordEncoder) {
        return username -> {
            log.debug("login with username => {}", username);
            return Mono.just(
                    User.withUsername(username)
                            .password(passwordEncoder.encode("password"))
                            .authorities(Collections.emptyList())
                            .build()
            );
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
```

- จะเหมือนหัวข้อ [spring-boot-reactive-security](../spring-boot-reactive-security) เพียงแต่มีการเพิ่ม configuration `securityWebFilterChain()` เข้ามา 
- สังเกตว่ามีการกำหนด login entry point หรือ login page เอง 
- ทุก ๆ entry point `.anyExchange().authenticated()` จะ require login ยกเว้น `.pathMatchers("/login").permitAll()` ที่อนุญาตให้ทุกคนเข้าถึงได้  
- `.csrf().disable()` มีการ disabled csrf token 

# 4. เขียน Controller
``` java
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello(final Authentication authentication) {
        return Mono.just("Hello => " + authentication.getName());
    }
}
```

# 5. เขียน Login Controller
```java
@Controller
public class LoginController {
    
    @GetMapping("/login")
    public Mono<String> login(){
        return Mono.just("custom-login");
    }
    
}
```

# 6. เขียน custom-login.html
```html
<!DOCTYPE html>
<html>
    <head>
        <title>Custom Login</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        <h1>Custom Login Page</h1>
        <form method="post" action="/login">
            <label>Username</label>
            <input name="username" type="text"/>
            <br/>
            <label>Password</label>
            <input name="password" type="password" />
            <br/>
            <button type="submit">Login</button>
        </form>
    </body>
</html>
```

# 7. Config Thymleaf
classpath:application.properties
```properties
#--------------------------------- Thymleaf ------------------------------------
spring.thymeleaf.cache=false
spring.thymeleaf.check-template=true
spring.thymeleaf.check-template-location=true
spring.thymeleaf.content-type=text/html
spring.thymeleaf.enabled=true
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML5
spring.thymeleaf.prefix=classpath:/static/
spring.thymeleaf.suffix=.html
```

# 8. Build Code 
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package 
```

# 9. Run 
``` shell 
$ mvn spring-boot:run
```

# 10. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
  
หลังจากนั้นมันจะเด้งเข้าหน้า login

![](./login.png)
  
### Username/Password สำหรับเข้าใช้งาน
- username = test
- password = password  

![](./login_success.png)
