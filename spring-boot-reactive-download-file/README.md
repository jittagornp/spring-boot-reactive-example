# spring-boot-reactive-download-file

> ตัวอย่างการเขียน Spring-boot Reactive Download File 

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
@Controller
public class ResourceController {

    @GetMapping({"", "/", "/classpath"})
    public Mono<ResponseEntity<Resource>> showClassPathImage() {
        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"reactive_spring.png\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                        .body(new ClassPathResource("static/image/reactive_spring.png"))
        );
    }

    @GetMapping({"/classpath/download"})
    public Mono<ResponseEntity<Resource>> downloadClassPathImage() {
        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"reactive_spring.png\"")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                        .body(new ClassPathResource("static/image/reactive_spring.png"))
        );
    }

    @GetMapping("/inputstream")
    public Mono<ResponseEntity<? extends Resource>> showInputStreamImage() {
        String fileName = "reactive_spring.png";
        return readStream(fileName)
                .map(stream -> {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                            .body(new InputStreamResource(stream));
                });
    }

    @GetMapping("/inputstream/download")
    public Mono<ResponseEntity<Resource>> downloadInputStreamImage() {
        String fileName = "reactive_spring.png";
        return readStream(fileName)
                .map(stream -> {
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.IMAGE_PNG_VALUE)
                            .body(new InputStreamResource(stream));
                });
    }

    private Mono<InputStream> readStream(final String fileName) {
        return Mono
                .create((MonoSink<InputStream> callback) -> {
                    try {
                        callback.success(getClass().getResourceAsStream("/static/image/" + fileName));
                    } catch (Exception ex) {
                        log.warn("read stream error => ", ex);
                        callback.error(ex);
                    }
                })
                .switchIfEmpty(Mono.error(new NotFoundException(fileName + " not found")));
    }
}
```

# 4. Build Code
cd ไปที่ root ของ project จากนั้น  
``` sh 
$ mvn clean package
```

# 5. Run 
``` sh 
$ mvn spring-boot:run
```

# 6. เข้าใช้งาน

เปิด browser แล้วเข้า 

- [http://localhost:8080](http://localhost:8080)
- [http://localhost:8080/classpath](http://localhost:8080/classpath)
- [http://localhost:8080/classpath/download](http://localhost:8080/classpath/download)
- [http://localhost:8080/inputstream](http://localhost:8080/inputstream)
- [http://localhost:8080/inputstream/download](http://localhost:8080/inputstream/download)
