# spring-boot-webflux-session
ตัวอย่างการเขียน Spring-boot WebFlux Session

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

หมายเหตุ lombox เป็น annotation code generator ตัวนึงครับ  

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
@Slf4j
@RestController
public class SessionController {

    @GetMapping({"", "/", "/session"})
    public Mono<WebSession> statelessSession(WebSession webSession) {
        return Mono.just(webSession);
    }

    @GetMapping("/session/create")
    public Mono<String> createSession(WebSession webSession) {
        webSession.start();
        return Mono.just("create session => " + webSession.getId());
    }

    @GetMapping("/session/invalidate")
    public Mono<String> invalidateSession(WebSession webSession) {
        return webSession.invalidate().then(Mono.just("invalidate session => " + webSession.getId()));
    }
}
```

# 4. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 5. Run 
``` shell 
$ mvn spring-boot:run
```

# 6. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
