# spring-boot-reactive-configuration-properties

> ตัวอย่างการเขียน Spring-boot Reactive Configuration Properties

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

# 3. กำหนด Config 
classpath:application.properties 
``` properties
jittagornp.kong.adminUrl=http://localhost:8001
jittagornp.kong.serviceRegistry.name="app"
jittagornp.kong.serviceRegistry.url=http://localhost:8080
jittagornp.kong.serviceRegistry.routePaths[0]=/
jittagornp.kong.serviceRegistry.enabled=false
```

# 4. เขียน Properties Class  
Reference ไปที่ jittagornp.kong.* ในไฟล์ application.properties 
```java
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "jittagornp.kong")
public class KongProperties {

    private String adminUrl;

    private ServiceRegistry serviceRegistry;

    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            serviceRegistry = new ServiceRegistry();
        }
        return serviceRegistry;
    }

    @Getter
    @Setter
    public static class ServiceRegistry {

        private String name;

        private String url;

        private List<String> routePaths;

        private boolean enabled;

        public List<String> getRoutePaths() {
            if (routePaths == null) {
                routePaths = new ArrayList<>();
                routePaths.add("/");
            }
            return routePaths;
        }

        public boolean getEnabled() {
            return enabled;
        }

    }
}
```

# 5. เขียน Controller ลองเรียกใช้งาน
```java
@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final KongProperties properties;

    @GetMapping({"", "/"})
    public Mono<KongProperties> hello() {
        return Mono.just(properties);
    }
}
```

# 6. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean package
```

# 7. Run 
``` shell 
$ mvn spring-boot:run
```

# 8. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)  
  
ผลลัพธ์
```json
{
    "adminUrl": "http://localhost:8001",
    "serviceRegistry": {
        "name": "\"app\"",
        "url": "http://localhost:8080",
        "routePaths": [
            "/"
        ],
        "enabled": false
    }
}
```
