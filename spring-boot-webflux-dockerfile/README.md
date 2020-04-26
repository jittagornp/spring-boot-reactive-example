# spring-boot-webflux-dockerfile 
ตัวอย่างการเขียน Spring-boot WebFlux Dockerfile 

# Requires 

- ในเครื่องมีการติดตั้ง Docker แล้ว  

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
@RestController
public class HomeController {

    @GetMapping({"", "/"})
    public Mono<String> hello() {
        return Mono.just("Hello world.");
    }
}
```
# 4. เขียน Dockerfile 
ไว้ที่ root ของ project /Dockerfile 
```dockerfile 
# Use official base image of Java Runtim
FROM openjdk:8-jdk-alpine

# Set volume point to /tmp
VOLUME /tmp

# Make port 8080 available to the world outside container
EXPOSE 8080

# Set application's JAR file
ARG JAR_FILE=target/*.jar

# Add the application's JAR file to the container
ADD ${JAR_FILE} app.jar

# Run the JAR file
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8080", "-jar", "/app.jar"]
```

### คำอธิบาย
- `FROM openjdk:8-jdk-alpine` คือ ใช้ base image เป็น openjdk 8 alpine  
- `VOLUME` เป็นการ mount พื้นที่เก็บข้อมูล (volume) ใน container ว่าให้ชี้ไปที่ /tmp (temp)
- `EXPOSE` เป็นการเปิด port 8080 เพื่อให้ข้างนอกสามารถ access container ได้ 
- `ARG JAR_FILE=target/*.jar` เป็นการกำหนด arguments ที่ใช้สำหรับ build image 
- `ADD ${JAR_FILE} app.jar` เพิ่ม หรือ copy ข้อมูลตาม arguments ที่กำหนด เข้าไปใน container โดยใช้ชื่อเป็น app.jar  
- `ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-Dserver.port=8080", "-jar", "/app.jar"]` เป็นการ run command ภายใน container ในที่นี้คือสั่ง run java application (.jar)  

# 5. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 6. Build Image จาก Dockerfile  
``` shell 
$ docker build -t hello-world -f Dockerfile .
```
- `docker build` เป็นการใช้คำสั่งเพื่อ build image 
- `-t hello-world` เป็นการกำหนด ชื่อ + tag สำหรับ image
- `-f Dockerfile` เป็นการระบุว่าให้อ้างอิงไปที่ Dockerfile ไหน  (ถ้าไม่ระบุ default จะเป็น Dockerfile จาก path ปัจจุบัน)  
- `.` คือ สัมพันธ์ หรือให้ Dockerfile อิงกับ path ปัจจบัน  

# 7. Run Container 
``` shell
$ docker run -d -p 8080:8080 --name hello-world hello-world 
```
- `docker run` เป็นการใช้คำสั่งเพื่อ run container  
- `-d` คือ ให้ทำงานแบบ background process 
- `-p 8080:8080` เป็นการ map port จากข้างนอก 8080 ไปยัง container port 8080 
- `--name hello-world` เป็นการตั้งชื่อ container ว่า hello-world 
- `hello-world` ตัวสุดท้าย เป็นการบอกว่า run โดยใช้ image ชื่อ container  

# 8. เข้าใช้งาน

เปิด browser แล้วเข้า [http://localhost:8080](http://localhost:8080)

# คำสั่งอื่น ๆ ของ Docker  

- `docker ps -a` ไว้ดู container ทั้งหมด
- `docker stop <DOCKER_NAME or DOCKER_ID>` ไว้ stop container 
- `docker start <DOCKER_NAME or DOCKER_ID>` ไว้ start container 
- `docker rm <DOCKER_NAME or DOCKER_ID>` ไว้ลบ container 
- `docker logs <DOCKER_NAME or DOCKER_ID>` ไว้ดู log ของ container  
  
- `docker image ls` ไว้ดู image ทั้งหมด 
- `docker image rm <IMAGE_NAME>` ไว้ลบ image

# เอกสารอ่านเพิ่มเติม
- [Dockerfile Reference](https://docs.docker.com/engine/reference/builder/)  
- [สร้าง Docker ของ Spring Boot กันเถอะ](https://medium.com/@phayao/%E0%B8%AA%E0%B8%A3%E0%B9%89%E0%B8%B2%E0%B8%87-docker-%E0%B8%82%E0%B8%AD%E0%B8%87-spring-boot-%E0%B8%81%E0%B8%B1%E0%B8%99%E0%B9%80%E0%B8%96%E0%B8%AD%E0%B8%B0-2a36adc7a0ba)
- [จัดการ Spring boot application ด้วย Docker](http://www.somkiat.cc/docker-with-spring-boot/)  

ขอบคุณสำหรับเอกสารและบทความครับ  
