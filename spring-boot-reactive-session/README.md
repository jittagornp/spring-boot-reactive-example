# spring-boot-reacive-session

> ตัวอย่างการเขียน Spring-boot Reactive Session

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
public class SessionController {

    @GetMapping({"", "/", "/session"})
    public Mono<WebSession> getSession(final WebSession webSession) {
        return Mono.just(webSession);
    }

    @GetMapping("/session/create")
    public Mono<String> createSession(final WebSession webSession) {
        webSession.start();
        return Mono.just("create session => " + webSession.getId());
    }

    @GetMapping("/session/invalidate")
    public Mono<String> invalidateSession(final WebSession webSession) {
        return webSession.invalidate()
                .then(Mono.just("invalidate session => " + webSession.getId()));
    }
}
```

### 3.1 Get Current Session  
ถ้าเราเรียก `/` หรือ `/session` Spring จะ return Session หน้าตาแบบนี้ออกไป 
```json
{
    "id": "00d60831-8c1f-409d-9b18-7d0d15cd91c5",
    "attributes": {},
    "creationTime": "2019-07-08T13:11:12.856Z",
    "lastAccessTime": "2019-07-08T13:11:12.856Z",
    "maxIdleTime": "PT30M",
    "expired": false,
    "started": false
}
```
ตรง `id` เป็น uuid และจะได้ค่าใหม่เสมอ (random)    
สังเกตตรง `started` จะเป็น false คือ ยังไม่ได้สั่ง start ใช้งาน session นี้  

### 3.2 Start Session 
ต่อมา ทดลองเรียก `/session/create` แล้วกลับไปเรียก `/session` ใหม่หลาย ๆ ครั้ง จะพบว่า   

ไม่ว่าจะเรียก `/session` กี่ครั้ง ก็จะได้ session id เดิม เนื่องจากเราสั่ง start ใช้งาน session นี้แล้ว  
  
โดยเมื่อเราสั่ง webSession.start() spring จะ save() session นี้ลง session store (repository) และจะ    
write http response header `Set-Cookie` กลับไปยัง browser เพื่อให้ browser จดจำ cookie id นี้ไว้  
  
เมื่อเข้ามาใหม่ browser ก็จะส่ง cookie id เดิมกลับมาด้วย    
ทำให้เห็นว่า ไม่ว่าจะเรียก `/session` กี่ครั้ง ก็ยังได้ session id เดิม  
และสังเกตตรง `started` จะเป็น true เพราะเราสั่ง start session นี้แล้ว   
```json
{
    "id": "52f0a4c2-4706-406f-9c2b-9410d5ff324b",
    "attributes": {},
    "creationTime": "2019-07-08T13:34:18.655Z",
    "lastAccessTime": "2019-07-08T13:34:24.669Z",
    "maxIdleTime": "PT30M",
    "expired": false,
    "started": true
}
```

### 3.3 Invalidate Session 
จากนั้นลองเรียก `/session/invalidate` จะเป็นการ invalidate หรือ revoke หรือ stop การใช้งาน session นั้น ๆ  
  
โดยเมื่อเราเรียก webSession.invalidate() spring จะ delete() session นั้นออกจาก session store (repository) และจะ 
write empty cookie ไปกับ http response header `Set-Cookie` เพื่อกลับไป clear ค่า session id ที่ browser เคยเก็บไว้  
  
ทำให้เมื่อเราเรียก `/session` อีกครั้ง ก็จะกลับมาได้ session id ใหม่เสมอ เหมือนข้อ 3.1  


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
