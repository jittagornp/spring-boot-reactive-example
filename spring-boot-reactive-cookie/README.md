# spring-boot-reactive-cookie 

> ตัวอย่างการเขียน Spring-boot Reactive Cookie 

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

หมายเหตุ lombox เป็น annotation code generator ตัวนึงครับ  

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
@Slf4j
@RestController
public class CookieController {

    @GetMapping({"", "/", "/cookies"})
    public Mono<String> getCookie(@CookieValue(value = "access_token", defaultValue = "") final String accessToken) {
        return Mono.just("cookie value => " + accessToken);
    }

    @GetMapping("/cookies/create")
    public Mono<ResponseCookie> createCookie(final ServerWebExchange exchange) {
        final String accessToken = UUID.randomUUID().toString();
        final ResponseCookie cookie = ResponseCookie.from("access_token", accessToken).build();
        exchange.getResponse().addCookie(cookie);
        return Mono.just(cookie);
    }

    @GetMapping("/cookies/invalidate")
    public Mono<String> invalidateCookie(@CookieValue(value = "access_token", defaultValue = "") final String accessToken, final ServerWebExchange exchange) {
        final ResponseCookie cookie = ResponseCookie.from("access_token", "").maxAge(0).build();
        exchange.getResponse().addCookie(cookie);
        return Mono.just("invalidate cookie => " + accessToken);
    }
}
```

### คำอธิบาย

- `@CookieValue(value = "access_token", defaultValue = "")` เป็นการอ่านค่า Cookie ชื่อ `access_token` ที่ browser ส่งมา    
ถ้าไม่มี defaultValue จะเป็น empty string 
- การเขียน Cookie เราจะใช้ `exchange.getResponse().addCookie(cookie);` ซึ่ง spring จะเขียน http header `Set-Cookie` กลับไปกับ response จากนั้น browser ก็จะจัดเก็บค่า cookie นั้นไว้  
- การ Invalidate cookie หรือลบ cookie จะทำเหมือนกับการเขียน cookie เพียงแต่ค่าของ cookie เราจะ set เป็น empty string และ set `maxAge(0)` เพื่อให้ cookie นั้นหมดอายุ browser ก็จะ clear cookie นั้นทิ้งไป   

# 4. Build Code
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
```

# 5. Run 
``` shell 
$ mvn spring-boot:run
```

# 6. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)

# ทดสอบ
- ใช้ Google Chrome ทดลองสร้าง cookie โดยการเข้า [http://localhost:8080/cookies/create](http://localhost:8080/cookies/create)  
- จากนัั้นกด F12 ไปที่ tab Application แล้วไปที่เมนู Storage > Cookies ดังภาพ  

![cookie.png](cookie.png)
