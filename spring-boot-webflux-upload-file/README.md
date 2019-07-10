# spring-boot-webflux-upload-file
ตัวอย่างการเขียน Spring-boot WebFlux Upload File 

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

หมายเหตุ lombox เป็น annotation code generator ตัวนึงครับ  

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
@RestController
public class UploadController {

    private static final String UPLOAD_DIRECTORY = "/temp";

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Mono<Map> upload(@RequestPart("file") Mono<FilePart> filePart) {
        return filePart
                .map((FilePart fp) -> {

                    String path = UPLOAD_DIRECTORY + "/" + fp.filename();
                    File file = Paths.get(path).toFile();
                    fp.transferTo(file);

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", file.getName());
                    map.put("path", path);
                    map.put("lastModified", file.lastModified());
                    map.put("size", file.length());
                    return map;
                });
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

# ทดสอบ
ทดลอง Upload ผ่าน Postman 

![upload-file.png](upload-file.png)
