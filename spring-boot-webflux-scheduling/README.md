# spring-boot-webflux-scheduling
ตัวอย่างการเขียน Spring-boot WebFlux Scheduling

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

# 3. Config Scheduling 
```java 
@Configuration
@EnableScheduling
public class SchedulingConfig {
    
}
```

# 4. เขียน Scheduler หรือ Task Runner 
ประกาศ interface 
```java 
public interface TaskRunner {

    void run();

}
```
implement interface
```java 
@Slf4j
@Component
public class CleanSessionExpiredTaskRunner implements TaskRunner {

    @Override
    @Scheduled(fixedDelay = 1000)
    public void run() {
        log.debug("running...." + System.currentTimeMillis());
    }

}
```

- @Scheduled เป็น annotation ที่บอกว่าให้ทำ scheduling ที่ method นี้ 
- fixedDelay 1000 คือ ให้ทำทุก 1000 millisecond หรือ 1 วินาที 

### ข้อควรระวัง
fixedDelay vs fixedRate
- fixedDelay ทำงานตามเวลาที่ตั้งไว้ เช่น ทุก 1 วินาที ทุก 5 วินาที หรือ ทุก 10 วินาที ถ้างานที่สั่งให้ทำ มันยังทำไม่เสร็จ  มันจะรอให้งานั้น ๆ เสร็จก่อน แล้วจึงค่อยทำงานถัดไป
- fixedRate จะทำงานคล้าย ๆ fixedDelay แต่จะทำตามเวลาเป๊ะ ๆ เรา fixed ไว้เท่าไหร่มันก็ทำเท่านั้น ไม่รอให้งานก่อนหน้าเสร็จ มันก็ทำต่อ 

# 5. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 6. Run 
``` shell 
$ mvn spring-boot:run
```

# 7. ดูผลัพธ์ที่ Console 
