# spring-boot-reactive-logging

> ตัวอย่างการเขียน Spring-boot Reactive Logging 

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

lombox เป็น dependency ที่ใช้สำหรับ generate code จาก annotation ต่าง ๆ   
เอกสาร [https://projectlombok.org/](https://projectlombok.org/)  

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
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        log.debug("call hello method");
        return Mono.just("Hello world.");
    }
}
```

`@Slf4j` เป็นการใช้ annotation ของ lombox เพื่อ generate Log4J Code (logging)   
ทำให้เราไม่ต้องเขียน new instance ของ log4j เอง  
การทำงานของ lombox เป็นการ inject code at compile time  

# 4. Config Logging

classpath:application.properties 
``` properties 
logging.level.org.springframework=DEBUG
logging.level.me.jittagornp=DEBUG
logging.file=/log/app.log
```
# 5. Build Code 
cd ไปที่ root ของ project จากนั้น  
``` sh
$ mvn clean package 
```

# 6. Run 
``` sh 
$ mvn spring-boot:run
```

# 7. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)


# เอกสารเพิ่มเติม 
- [https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html](https://docs.spring.io/spring-boot/docs/current/reference/html/howto-logging.html)  
