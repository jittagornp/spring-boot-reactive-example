# spring-boot-webflux-postgresql
ตัวอย่างการเขียน Spring-boot WebFlux Postgresql  

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
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>

    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-core</artifactId>
        <version>5.4.3.Final</version>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.2.5</version>
    </dependency>

    <dependency>
        <groupId>com.zaxxer</groupId>
        <artifactId>HikariCP</artifactId>
        <version>3.3.1</version>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <scope>provided</scope>
    </dependency>
</dependencies>

...
```
### dependencies
- `spring-boot-starter-webflux` ใช้สำหรับเขียน webflux   
- `spring-boot-starter-data-jpa` ไว้สำหรับเขียนคำสั่ง query, method query   
- `hibernate-core`  สำหรับทำ ORM (Object Relational Mapping) ไว้เขียนพวก entity class  
- `postgresql` เป็น postgresql database driver  
- `HikariCP` เป็นตัวจัดการ database connection pool  
- `lombok` เป็น annotation code generator สามารถ generate code at compile time ได้ ทำให้เราไม่ต้องเขียน code บางส่วนเอง เช่น getter setter method 

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

# 3. เขียน entity 
```java
@Data
@Entity
@Table(name = User.TABLE_NAME)
public class User implements Serializable {

    public static final String TABLE_NAME = "user";

    @Id
    private String id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

}
```

# 4. เขียน Repository 
```java
public interface UserRepository extends JpaRepository<User, String>{
    
}
```

# 5. เรียกใช้งาน Repository ผ่าน Controller
``` java
@RestController
public class UserController {

    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public Flux<User> findAll() {
        return Flux.fromIterable(userRepository.findAll());
    }

}
```

# 6. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 7. Run 
``` shell 
$ mvn spring-boot:run \
    -Dserver.port=8080 \
    -Dspring.datasource.url=jdbc:postgresql://<HOST>:<PORT>/<DATABASE_NAME>?sslmode=require \
    -Dspring.datasource.username=<DATABASE_USERNAME> \
    -Dspring.datasource.password=<DATABASE_PASSWORD> \
    -Dspring.jpa.properties.hibernate.default_schema=<DATABASE_SCHEMA>
```

# 8. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
