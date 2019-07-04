# spring-boot-webflux-mongodb
ตัวอย่างการเขียน Spring-boot WebFlux Mongodb  

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
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-mongodb-reactive</artifactId>
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

# 3. เขียน Collection 
```java
@Data
@Document(collection = LoginHistory.COLLECTION_NAME)
public class LoginHistory implements Serializable {

    public static final String COLLECTION_NAME = "login_history";

    @Id
    private String id;

    private LocalDateTime loginDate;

    private LocalDateTime logoutDate;

    private String sessionId;

    private String agentId;

    private String userId;

    private String ipAddress;

}
```
# 4. เขียน Repository 
```java
public interface LoginHistoryRepository extends ReactiveMongoRepository<LoginHistory, String>{
    
}
```

# 5. เรียกใช้งาน Repository ผ่าน Controller
``` java
@RestController
public class LoginHistoryController {

    private final LoginHistoryRepository repository;

    @Autowired
    public LoginHistoryController(LoginHistoryRepository repository) {
        this.repository = repository;
    }

    @GetMapping({ "", "/" })
    public Flux<LoginHistory> findAll() {
        return repository.findAll();
    }

}
```

# 6. Config application.properties
```properties
#------------------------------------ Mongodb ----------------------------------
spring.data.mongodb.uri=mongodb://*****
```

# 7. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 8. Run 
``` shell 
$ mvn spring-boot:run \ 
    -Dspring.data.mongodb.uri=mongodb://<DATABASE_USERNAME>:<DATABASE_PASSWORD>@<DATABASE_HOST>:<DATABASE_PORT>/<DATABASE_NAME>
```

# 9. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
