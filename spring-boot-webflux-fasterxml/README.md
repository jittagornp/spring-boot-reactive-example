# spring-boot-webflux-fasterxml
ตัวอย่างการเขียน Spring-boot WebFlux Faster XML 

# 1. เพิ่ม Dependencies

pom.xml 
``` xml
...
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.2.2.RELEASE</version>
</parent>

<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>

    <dependency>
        <groupId>com.fasterxml.jackson.dataformat</groupId>
        <artifactId>jackson-dataformat-xml</artifactId>
        <version>2.10.1</version>
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

# 3. เขียน Controller
``` java
@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {

    private final XmlMapper xmlMapper = new XmlMapper();

    @ResponseBody
    @GetMapping("/toXml")
    public Mono<String> findById() throws JsonProcessingException {
        User user = new User();
        user.setId(1L);
        user.setFirstName("jittagorn");
        user.setLastName("pitakmetagoon");
        xmlMapper.writeValueAsString(user);
        return Mono.just(xmlMapper.writeValueAsString(user));
    }

    @ResponseBody
    @GetMapping("/toUser")
    public Mono<User> update() throws JsonProcessingException {
        final String xml = "<xml>"
                + "<id>1</id>"
                + "<first_name>jittagorn</first_name>"
                + "<last_name>pitakmetagoon</last_name>"
                + "</xml>";
        final User user = xmlMapper.readValue(xml, User.class);
        log.debug("user => {}", user);
        return Mono.just(user);
    }

    @Data
    @JacksonXmlRootElement(localName = "xml")
    public static class User {

        private Long id;

        @JacksonXmlProperty(localName = "first_name")
        private String firstName;

        @JacksonXmlProperty(localName = "last_name")
        private String lastName;

    }

}
```

# 4. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 5. Run 
``` shell 
$ mvn spring-boot:run
```

# 6. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)
