# spring-boot-reactive-r2dbc-postgresql

> ตัวอย่างการเขียน Spring-boot Reactive R2DBC (The Reactive Relational Database Connectivity) + Postgresql

![](./PVLG-R2DBC-Logo-RGB.png)

- R2DBC (The Reactive Relational Database Connectivity) เป็น Library/Dependency ฝั่งภาษา Java สำหรับการเขียน Code เพื่อเชื่อมต่อไปยัง Database แบบ Reactive (Non-Block I/O) 
- มี Spring-data รองรับ เพื่อให้สามารถเขียน CRUD และเขียน Query อื่น ๆ ได้ง่ายขึ้น     
  
เว็บไซต์ 
- [https://r2dbc.io/](https://r2dbc.io/)
- [https://spring.io/projects/spring-data-r2dbc](https://spring.io/projects/spring-data-r2dbc)

# Prerequisites

- เตรียมฐานข้อมูล PostgreSQL ให้พร้อม
- สร้าง schema `app`
- สร้าง table `user` ที่ schema `app` โดยใช้ SQL นี้

```sql
CREATE TABLE "app"."user" (
    "id" UUID NOT NULL,
    "username" varchar(50) NOT NULL,
    "first_name" varchar(50) NOT NULL,
    "last_name" varchar(50) NOT NULL,
    PRIMARY KEY ("id")
);
```

![](./create_table.png)

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

    <!-- Database ****************************************************** -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-r2dbc</artifactId>
    </dependency>

    <dependency>
        <groupId>io.r2dbc</groupId>
        <artifactId>r2dbc-postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    <!-- Database ****************************************************** -->
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

**คำอธิบาย**

- `r2dbc-postgresql` เป็น dependency r2dbc สำหรับ postgresql 
- `spring-boot-starter-data-r2dbc` เป็น dependency สำหรับใช้ spring-data ร่วมกับ r2dbc 

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
```properties
#---------------------------------- Logging ------------------------------------
logging.level.me.jittagornp=DEBUG
logging.level.org.springframework.data.r2dbc=DEBUG

#---------------------------------- R2dbc --------------------------------------
spring.r2dbc.url=r2dbc:postgresql://<DATABASE_HOST_IP>/<DATA_BASE_NAME>?schema=app
spring.r2dbc.username=<DATABASE_USERNAME>
spring.r2dbc.password=<DATABASE_PASSWORD>
```

**หมายเหตุ**

- อย่าลืมแก้ `<DATABASE_HOST_IP>`, `<DATABASE_NAME>`, `<DATABASE_USERNAME>` และ `<DATABASE_PASSWORD>`

# 4. เขียน Entity

> Entity จะเป็นตัว Map ไปยัง Table `app.user`

```java
@Data
@Builder
@Table("app.user")
public class UserEntity {
    
    //Primary Key
    @Id
    private UUID id;

    private String username;

    @Column("first_name")
    private String firstName;

    @Column("last_name")
    private String lastName;
}
```

**คำอธิบาย**

- Annotation ต่าง ๆ ที่ใช้ ไม่ได้เป็นของ `javax.persistence.*` แต่เป็น Annotation ของ Spring-data เอง 
- ความสามารถของ Annotation จะไม่เท่ากับใน `javax.persistence.*` คือ ไม่สามารถกำหนด length, nullable ไม่สามาถทำ Join ต่าง ๆ ได้ ทำได้อย่างเดียวคือ Mapping Table/Column และกำหนด Primary Key ได้เท่านั้น
- ความสามารถเรื่องการ Join หรือ Constraint ต่าง ๆ จะใช้ Native SQL ทำเป็นหลัก 

เรื่อง Annotation ที่สามารถใช้ได้ ให้ดูจากเอกสารหน้านี้ [https://docs.spring.io/spring-data/r2dbc/docs/1.1.4.RELEASE/reference/html/#mapping.usage.annotations](https://docs.spring.io/spring-data/r2dbc/docs/1.1.4.RELEASE/reference/html/#mapping.usage.annotations)

# 5. เขียน Repository 

เพื่อทำ Default CRUD 
```java
public interface UserRepository extends ReactiveCrudRepository<UserEntity, UUID> {

}
```

**หมายเหตุ**

- Extend `ReactiveCrudRepository<T, S>` เพื่อให้ Spring ทำ Default CRUD สำหรับ Repository นี้ให้ 
  - Type Parameter `T` คือ Entity Type ในที่นี้เป็น UserEntity 
  - Type Parameter `S` คือ Id Type ของ Entity ซึ่งในที่นี้เราใช้ UUID 

# 6. เขียน Controller
``` java
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final DatabaseClient databaseClient;

    private final UserRepository userRepository;

    @GetMapping
    public Flux<UserEntity> findAll() {
        return userRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<UserEntity> findById(@PathVariable("id") final UUID id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new NotFoundException("User id \"" + id.toString() + "\"not found")));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<UserEntity> create(@RequestBody final UserEntity entity) {
        entity.setId(UUID.randomUUID());
        return databaseClient.insert()
                .into(UserEntity.class)
                .using(entity)
                .then()
                .thenReturn(entity);
    }

    @PutMapping("/{id}")
    public Mono<UserEntity> update(@PathVariable("id") final UUID id, @RequestBody final UserEntity entity) {
        return findById(id)
                .flatMap(dbEntity -> {
                    dbEntity.setFirstName(entity.getFirstName());
                    dbEntity.setLastName(entity.getLastName());
                    return userRepository.save(dbEntity);
                });
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") final UUID id) {
        return findById(id)
                .flatMap(dbEntity -> {
                    return userRepository.deleteById(dbEntity.getId());
                });
    }
}
```

**หมายเหตุ**

- ตอน insert ใช้ `databaseClient` แทน repository เนื่องจาก repository จะไม่สามารถ insert entity ที่มีการ set id ตั้งต้นได้ 

# 7. Build Code
cd ไปที่ root ของ project จากนั้น  
``` sh
$ mvn clean package
```

# 8. Run 
``` sh 
$ mvn spring-boot:run
```

# 9. ทดสอบด้วย Postman

Create User

![](./create_user.png)

Get all Users

![](get_all_users.png)

Select Users from table 

![](./select_user.png)

