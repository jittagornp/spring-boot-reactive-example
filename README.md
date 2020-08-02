# spring-boot-reactive-example

> ตัวอย่างการเขียน Java Spring-boot Reactive (WebFlux) ซึ่งเป็นการเขียน Spring-boot แบบ Non-Blocking I/O หรือ Asynchronous 

![Reactive Spring](spring-reactive.png)

# ปัญหา / ข้อเสนอแนะ 
หากพบปัญหา หรือ ต้องการให้ทำอะไรเพิ่ม รบกวนช่วยเปิด issue ให้ด้วยน่ะครับ  
ขอบคุณครับ :)  

# เอกสาร Spring 

- [Web on Reactive Stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)

# ข้อแตกต่าง 

- [Servlet vs Reactive (WebFlux)](difference.md)

# WebFlux Performance
- [SpringBoot 2 performance — servlet stack vs WebFlux reactive stack](https://medium.com/@the.raj.saxena/springboot-2-performance-servlet-stack-vs-webflux-reactive-stack-528ad5e9dadc)
- [Spring Boot performance battle: blocking vs non-blocking vs reactive](https://medium.com/@filia.aleks/microservice-performance-battle-spring-mvc-vs-webflux-80d39fd81bf0)
- [https://blog.ippon.tech/spring-5-webflux-performance-tests/](https://blog.ippon.tech/spring-5-webflux-performance-tests/)

# Prerequisites
สิ่งที่ต้องรู้ก่อนเขียน Spring-boot Reactive 
- java 11+ ตอนนี้ผมใช้ OpenJdk 11
- Apache Maven
  - [รู้จักกับ Apache Maven](https://www.jittagornp.me/blog/what-is-apache-maven/)    
  - [พื้นฐานการใช้ Maven Command Line](https://www.jittagornp.me/blog/basic-maven-command-line/)
- Reactive Programming ลองอ่านนี่ดูได้ครับ [RxJava series - part 1 - ตอน อะไรเอ่ย ReactiveX?](https://medium.com/@nutron/what-is-reactivex-38293abb81cb)  ขอบคุณสำหรับบทความครับ 
  - Spring-boot ใช้ Reactor ซึ่งเป็น lib reactive ตัวนึง มีความคล้ายกันกับ ReactiveX สามารถอ่านทฤษฎีแทนกันได้ Concept เหมือนกัน  
- Event Loop
  - [ทำความเข้าใจ Node.js กันอีกรอบ ก่อนย้ายบ้านไป Golang](https://medium.com/@goangle/%E0%B8%97%E0%B8%B3%E0%B8%84%E0%B8%A7%E0%B8%B2%E0%B8%A1%E0%B9%80%E0%B8%82%E0%B9%89%E0%B8%B2%E0%B9%83%E0%B8%88-event-loop-%E0%B9%83%E0%B8%99-node-js-%E0%B8%81%E0%B8%B1%E0%B8%99%E0%B8%AD%E0%B8%B5%E0%B8%81%E0%B8%A3%E0%B8%AD%E0%B8%9A-d80930ef081d)
  - [รู้ลึกการทำงานแบบ Asynchronous กับ Event Loop](https://www.babelcoder.com/blog/posts/asynchronous-javascript-and-event-loop) 

# ตัวอย่างการเขียน Reactor 
- [Reactor Example](./spring-boot-webflux-reactor-example)

# เริ่ม 

> Code บางตัวอาจจะไม่สามารถเอาไป Run ได้เลย เนื่องจากต้องเตรียม environment ต่าง ๆ ให้พร้อมก่อน เช่น Postgresql, Mongodb, Redis เป็นต้น ต้องมี database หรือ data source ปลายทางก่อน  

ให้เรียนรู้/ดูตัวอย่างตามลำดับต่อไปนี้    

### ปรับ Code เป็น Java 11 แล้ว
- [spring-boot-reactive-helloworld](spring-boot-reactive-helloworld) - Hello World!
- [spring-boot-reactive-dockerfile](spring-boot-reactive-dockerfile) - การเขียน Dockerfile, การ Build Docker Image และการ Run Container
- [spring-boot-reactive-logging](spring-boot-reactive-logging) - การ Config และใช้งาน Logging  
- [spring-boot-reactive-controller](spring-boot-reactive-controller) - การเขียน Controller 
- [spring-boot-reactive-form-data](spring-boot-reactive-form-data) - การรับค่าจาก Html Form
- [spring-boot-reactive-download-file](spring-boot-reactive-download-file) - การ Download File 
- [spring-boot-reactive-upload-file](spring-boot-reactive-upload-file) - การ Upload File  
- [spring-boot-reactive-session](spring-boot-reactive-session) - พื้นฐานการใช้ Session 
- [spring-boot-reactive-cookie](spring-boot-reactive-cookie) - พื้นฐานการใช้ Cookie 

### รอการปรับ Code เป็น Java 11

- [spring-boot-webflux-configuration-properties](spring-boot-webflux-configuration-properties) - การอ่าน Config จาก application.properties 
- [spring-boot-webflux-thymleaf](spring-boot-webflux-thymleaf)  - การใช้ Thymleaf ทำ View (Server Site) Rendering (HTML)    
- [spring-boot-webflux-filter](spring-boot-webflux-filter) - การเขียน Filter  
- [spring-boot-webflux-error-handler](spring-boot-webflux-error-handler) - การจัดการ Exception หรือ Error
- [spring-boot-webflux-custom-error-handler](spring-boot-webflux-custom-error-handler) - การ Custom ตัวจัดการ Exception หรือ Error 
- [spring-boot-webflux-validation](spring-boot-webflux-validation) - การ Validate ข้อมูล
- [spring-boot-webflux-manual-validation](spring-boot-webflux-manual-validation) - การ Validate ข้อมูลแบบ Manual  
- [spring-boot-webflux-custom-validator](spring-boot-webflux-custom-validator) - การ custom validator  
- [spring-boot-webflux-scheduling](spring-boot-webflux-scheduling) - การ Run Task แบบ Scheduling
- [spring-boot-webflux-unit-test](spring-boot-webflux-unit-test) - การเขียน Unit Test  
- [spring-boot-webflux-unit-test-mockito](spring-boot-webflux-unit-test-mockito) - การเขียน Unit Test + Mockito  
- [spring-boot-webflux-test-coverage](spring-boot-webflux-test-coverage) - Test Coverage ด้วย JaCoCo 
- [spring-boot-webflux-security](spring-boot-webflux-security) - พื้นฐานการ Config Spring Security สำหรับทำการ Login
- [spring-boot-webflux-custom-login-page](spring-boot-webflux-custom-login-page) - การ custom หน้า login 
- [spring-boot-webflux-security-authority](spring-boot-webflux-security-authority) - การกำหนดสิทธิ์การเข้าถึง  
- [spring-boot-webflux-postgresql](spring-boot-webflux-postgresql) - การเชื่อมต่อ Postgresql (Relational Database) 
- [spring-boot-webflux-pagination](spring-boot-webflux-pagination) - การทำ Pagination 
- [spring-boot-webflux-jdbc-template](spring-boot-webflux-jdbc-template) - การเขียน Native Query จาก JDBC Template  
- [spring-boot-webflux-entity](spring-boot-webflux-entity) - การเขียน Entity Class  
- [spring-boot-webflux-simple-service](spring-boot-webflux-simple-service) - พื้นฐานการเขียน Service 
- [spring-boot-webflux-mongodb](spring-boot-webflux-mongodb) - การเชื่อมต่อ Mongodb (NoSQL Document Database)  
- [spring-boot-webflux-mongo-operations](spring-boot-webflux-mongo-operations) - การเขียน Query ผ่าน Mogo Operations 
- [spring-boot-webflux-redis](spring-boot-webflux-redis) - การเชื่อมต่อ Redis (Key/Value NoSQL) 
- [spring-boot-webflux-nuxtjs](https://github.com/jittagornp/spring-boot-webflux-nuxtjs) - การเขียน Spring-boot Reactive + Nuxt.js 
- [spring-boot-webflux-javadoc](spring-boot-webflux-javadoc) - การ Generate Java Document API 
