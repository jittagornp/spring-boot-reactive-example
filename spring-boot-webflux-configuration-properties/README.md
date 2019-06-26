# spring-boot-webflux-configuration-properties
ตัวอย่างการเขียน Spring-boot WebFlux Configuration Properties

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

# 3. กำหนด Config 
classpath:application.properties 
``` properties
pamarin.kong.adminUrl=http://localhost:8001
pamarin.kong.serviceRegistry.name="app"
pamarin.kong.serviceRegistry.url=http://localhost:8080
pamarin.kong.serviceRegistry.routePaths[0]=/
pamarin.kong.serviceRegistry.enabled=false
```

# 4. เขียน Properties Class  
Reference ไปที่ pamarin.kong.* ในไฟล์ application.properties 
```java
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "pamarin.kong")
public class KongProperties {

    private String adminUrl;

    private ServiceRegistry serviceRegistry;

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

    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            serviceRegistry = new ServiceRegistry();
        }
        return serviceRegistry;
    }

}
```

# 5. เขียน Controller ลองเรียกใช้งาน
```java
@Slf4j
@RestController
public class HomeController {

    private final KongProperties properties;

    @Autowired
    public HomeController(KongProperties properties) {
        this.properties = properties;
    }

    @GetMapping({"", "/"})
    public Mono<KongProperties> hello() {
        return Mono.just(properties);
    }
}

```

# 6. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 6. Run 
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
