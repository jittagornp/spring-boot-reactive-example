# spring-boot-reactive-dockerfile 

> ตัวอย่างการเขียน Spring-boot Reactive + Dockerfile 

# Prerequisites 

- มีความรู้เรื่อง Docker 
    - ถ้ายังไม่แม่น สามารถอ่านเพิ่มเติมได้ที่ [พื้นฐาน Docker](https://docs.google.com/presentation/d/1NXArkIDFIJMmcvXY63cc5z7jIsbx8SDZqt76RqeuGwU/edit?usp=sharing)
- ในเครื่องมีการติดตั้ง Docker แล้ว 
    - สำหรับ Ubuntu Server สามารถทำตามนี้ได้ [ติดตั้ง Docker บน Ubuntu 18.04](https://www.jittagornp.me/blog/install-docker-on-ubuntu-18.04/)

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
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }
}
```
# 4. เขียน Dockerfile 
ไว้ที่ root ของ project /Dockerfile 
```dockerfile 
FROM openjdk:11-jre-slim
EXPOSE 8080
ADD target/*.jar /app.jar
ENTRYPOINT java $JAVA_OPTS -jar /app.jar
```

# 5. Build Code
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
```

# 6. Build Docker Image จาก Dockerfile  
``` shell 
$ docker build -t hello-world .
``` 

# 7. Run Container 
``` shell
$ docker run -d -p 8080:8080 --name hello-world hello-world 
```

# 8. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
