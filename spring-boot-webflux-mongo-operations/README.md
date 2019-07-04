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

ประกาศ interface
```java
public interface LoginHistoryRepository {

    Mono<LoginHistory> save(LoginHistory history);

    Flux<LoginHistory> findAll();

    Mono<LoginHistory> findById(String id);

    Mono<Void> deleteById(String id);

    Mono<Void> deleteAll();

}
```

implement interface
```java
@Repository
public class LoginHistoryRepositoryImpl implements LoginHistoryRepository {

    private final MongoOperations mongoOperations;

    @Autowired
    public LoginHistoryRepositoryImpl(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public Flux<LoginHistory> findAll() {
        return Flux.fromIterable(mongoOperations.findAll(LoginHistory.class));
    }

    @Override
    public Mono<LoginHistory> findById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Mono.justOrEmpty(mongoOperations.findOne(query, LoginHistory.class))
                .switchIfEmpty(Mono.error(new NotFoundException("Not found user of id " + id)));
    }

    @Override
    public Mono<LoginHistory> save(LoginHistory history) {
        return Mono.just(mongoOperations.save(history));
    }

    @Override
    public Mono<Void> deleteById(String id) {
        Query query = Query.query(Criteria.where("id").is(id));
        return Mono.just(mongoOperations.remove(query, LoginHistory.class))
                .then();
    }

    @Override
    public Mono<Void> deleteAll() {
        mongoOperations.dropCollection(LoginHistory.class);
        return Mono.empty();
    }

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

    @GetMapping({"", "/"})
    public Flux<LoginHistory> home() {
        return findAll();
    }

    @GetMapping("/histories")
    public Flux<LoginHistory> findAll() {
        return repository.findAll();
    }

    @GetMapping("/histories/{id}")
    public Mono<LoginHistory> findById(@PathVariable("id") String id) {
        return repository.findById(id);
    }

    @PostMapping("/histories")
    public Mono<LoginHistory> save(@RequestBody LoginHistory history) {
        return repository.save(history);
    }

    @DeleteMapping("/histories")
    public Mono<Void> deleteAll() {
        return repository.deleteAll();
    }

    @DeleteMapping("/histories/{id}")
    public Mono<Void> deleteAll(@PathVariable("id") String id) {
        return repository.deleteById(id);
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
