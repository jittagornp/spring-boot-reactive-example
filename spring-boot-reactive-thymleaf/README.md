# spring-boot-reactive-thymleaf

> ตัวอย่างการเขียน Spring-boot Reactive Thymleaf เพื่อทำ Server Side Rendering

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
        <artifactId>spring-boot-starter-thymeleaf</artifactId>
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
@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public Mono<String> hello(final Model model) {
        model.addAttribute("name", "Jittagorn");
        return Mono.just("index");
    }
}

@Controller
@RequiredArgsConstructor
public class UserController {

    private final TemplateEngine templateEngine;

    @GetMapping("/user")
    public Mono<ResponseEntity<String>> hello() {
        return Mono.fromCallable(() -> {
            final Context context = new Context();
            context.setVariable("name", "jittagorn pitakmetagoon");
            return ResponseEntity.ok()
                    .body(templateEngine.process("user", context));
        });
    }
}
```

# 4. Config 
classpath:application.properties 
``` properties 
spring.thymeleaf.cache=false
spring.thymeleaf.check-template=true
spring.thymeleaf.check-template-location=true
spring.thymeleaf.content-type=text/html
spring.thymeleaf.enabled=true
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=LEGACYHTML5
spring.thymeleaf.prefix=classpath:/static/
spring.thymeleaf.suffix=.html 
```

# 5. Build
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

# เอกสารอ่านเพิ่มเติม

- [https://www.thymeleaf.org/](https://www.thymeleaf.org/)  
