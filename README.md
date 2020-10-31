# spring-boot-reactive-example

> ตัวอย่างการเขียน Java Spring-boot Reactive (WebFlux) ซึ่งเป็นการเขียน Spring-boot แบบ Non-Blocking I/O หรือ Asynchronous 

![Reactive Spring](spring-reactive.png)

# Spring

Spring เป็น Framework ฝั่งภาษา Java สำหรับใช้เขียน Web และ Back-end Application ที่ได้รับความนิยมอย่างมากในปัจจุบัน     
มีระบบนิเวศ หรือ Ecosystem ต่าง ๆ มากมาย ทำให้สามารถนำ Spring ไปใช้สร้างสรรค์ผลงานต่าง ๆ ได้อย่างรวดเร็ว   
  
Spring-boot เป็น Subset ของ Spring Framework เป็นการเอา Ecosystem ต่าง ๆ ของ Spring ที่มีอยู่ มาประกอบร่างรวมกัน เพื่อให้ Spring นั้นใช้งานได้ง่ายขึ้นไปอีก โดยมีจุดเด่นดังนี้
1. สามารถทำ Stand Alone หรือ Micro Service Application ได้
2. มี Embeded Application Server ภายในตัว สามารถ Start ตัวเองได้ โดยไม่ต้องพึ่งพา Application Server จากภายนอก 
3. มีระบบ Auto Configuration 
4. Config ง่าย 
5. ใช้งานง่าย 
6. Deploy ง่าย
7. อื่น ๆ

Spring-boot มี 2 แบบ คือ

1. Servlet Stack 
2. Reactive Stack 

### Servlet Stack 

- เป็นการเขียน Code แบบ Blocking I/O 
- ใช้ Web MVC เป็น Base 
- เขียนง่าย ตรงไปตรงมา 
- Application Server จะใช้เทคนิคในการจัดการ Request/Response ด้วย Thread Pool Connection 

### Reactive Stack 

- เป็นการเขียน Code แบบ Non-Blocking I/O
- เขียน Code แบบ Reactive (Functional Programming รูปแบบหนึ่ง)
- ใช้ WebFlux เป็น Base 
- เขียน/เข้าใจ ยากกว่าแบบ Servlet Stack 
- Application Server จะใช้เทคนิคในการจัดการ Request/Response ด้วย Event Loop 
- กิน Resources (CPU, RAM) น้อยกว่าแบบ Servlet Stack 
- รองรับ Concurrent ได้มากกว่าแบบ Servlet Stack 

# เชิญชวน
อยากเชิญชวน คนที่มีความรู้ด้านใดด้านหนึ่งตามที่ตัวเองถนัด ทำตัวอย่างอะไรทำนองนี้ไว้  
ตัวผมเองก็ใช้อันนี้สอนน้อง ๆ สอนเด็กฝึกงานที่บริษัท อีกทั้งเอาไว้ทบทวนตัวเองด้วย   
กับเห็นว่ามันมีประโยชน์สำหรับทุกคน เลยแชร์ไว้ครับ   

# ปัญหา / ข้อเสนอแนะ 
หากพบปัญหา หรือ ต้องการให้ทำอะไรเพิ่ม รบกวนช่วยเปิด issue หรือ fork/pull request ให้ด้วยน่ะครับ  
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
- [spring-boot-reactive-jenkinsfile](spring-boot-reactive-jenkinsfile) - การเขียน Jenkinsfile 
- [spring-boot-reactive-logging](spring-boot-reactive-logging) - การ Config และใช้งาน Logging  
- [spring-boot-reactive-default-timezone](spring-boot-reactive-default-timezone) - การตั้งค่า Default Time Zone 
- [spring-boot-reactive-controller](spring-boot-reactive-controller) - การเขียน Controller 
- [spring-boot-reactive-cors](spring-boot-reactive-cors) - การ config Cors (Cross-origin resource sharing)
- [spring-boot-reactive-form-data](spring-boot-reactive-form-data) - การรับค่าจาก Html Form
- [spring-boot-reactive-download-file](spring-boot-reactive-download-file) - การ Download File 
- [spring-boot-reactive-upload-file](spring-boot-reactive-upload-file) - การ Upload File  
- [spring-boot-reactive-session](spring-boot-reactive-session) - พื้นฐานการใช้ Session 
- [spring-boot-reactive-cookie](spring-boot-reactive-cookie) - พื้นฐานการใช้ Cookie 
- [spring-boot-reactive-configuration-properties](spring-boot-reactive-configuration-properties) - การอ่าน Config จาก application.properties 
- [spring-boot-reactive-thymleaf](spring-boot-reactive-thymleaf)  - การใช้ Thymleaf ทำ View (Server Site) Rendering (HTML)  
- [spring-boot-reactive-web-filter](spring-boot-reactive-web-filter) - การเขียน Web Filter  
- [spring-boot-reactive-error-handler](spring-boot-reactive-error-handler) - การจัดการ Exception หรือ Error
- [spring-boot-reactive-custom-error-handler](spring-boot-reactive-custom-error-handler) - การ Custom ตัวจัดการ Exception หรือ Error 
- [spring-boot-reactive-validation](spring-boot-reactive-validation) - การ Validate ข้อมูล 
- [spring-boot-reactive-manual-validation](spring-boot-reactive-manual-validation) - การ Validate ข้อมูลแบบ Manual  
- [spring-boot-reactive-custom-validator](spring-boot-reactive-custom-validator) - การ custom validator  
- [spring-boot-reactive-scheduling](spring-boot-reactive-scheduling) - การ Run Job/Task แบบ Scheduling
- [spring-boot-reactive-unit-test-junit5](spring-boot-reactive-unit-test-junit5) - การเขียน Unit Test ด้วย JUnit 5 
- [spring-boot-reactive-security](spring-boot-reactive-security) - พื้นฐานการ Config Spring Security สำหรับทำการ Login
- [spring-boot-reactive-security-custom-login](spring-boot-reactive-security-custom-login) - การ Custom หน้า Login
- [spring-boot-reactive-security-authority](spring-boot-reactive-security-authority) - การกำหนดสิทธิ์การเข้าถึง   
- [spring-boot-reactive-swagger](spring-boot-reactive-swagger) - การทำ API Document ด้วย Swagger  
- [spring-boot-reactive-pagination](spring-boot-reactive-pagination) - การทำ Pagination 
- [spring-boot-reactive-r2dbc-postgresql](spring-boot-reactive-r2dbc-postgresql) - การเขียน Code เชื่อมต่อ Database PosgreSQL ด้วย R2DBC 
- [spring-boot-reactive-r2dbc-pool-postgresql](spring-boot-reactive-r2dbc-pool-postgresql) - การทำ Connection Pool สำหรับ R2DBC PosgreSQL 
- [spring-boot-reactive-gmail](spring-boot-reactive-gmail) - การส่ง email ด้วย Gmail  
- [spring-boot-reactive-oauth-client](spring-boot-reactive-oauth-client) - การเขียน Code เพื่อ Login ผ่าน OAuth ของ Google, Facebook 
- [spring-boot-reactive-google-cloud-storage](spring-boot-reactive-google-cloud-storage) - การเขียน Code เพื่อเชื่อมต่อกับ Google Cloud Storage  
- [spring-boot-reactive-digitalocean-spaces](spring-boot-reactive-digitalocean-spaces) - การเขียน Code เพื่อเชื่อมต่อกับ DigitalOcean Spaces
- [spring-boot-reactive-neo4j](https://github.com/jittagornp/spring-boot-reactive-neo4j-example) - การเขียน Code เชื่อมต่อ Graph Database Neo4j 
- [spring-boot-reactive-redis-pubsub](https://github.com/jittagornp/spring-boot-reactive-redis-pubsub-example) - การเขียน Code เชื่อมต่อ Redis แบบ Pub/Sub 

### รอการปรับ Code เป็น Java 11
  
- [spring-boot-webflux-unit-test-mockito](spring-boot-webflux-unit-test-mockito) - การเขียน Unit Test + Mockito  
- [spring-boot-webflux-test-coverage](spring-boot-webflux-test-coverage) - Test Coverage ด้วย JaCoCo 
- [spring-boot-webflux-postgresql](spring-boot-webflux-postgresql) - การเชื่อมต่อ Postgresql (Relational Database) 
- [spring-boot-webflux-jdbc-template](spring-boot-webflux-jdbc-template) - การเขียน Native Query จาก JDBC Template  
- [spring-boot-webflux-entity](spring-boot-webflux-entity) - การเขียน Entity Class  
- [spring-boot-webflux-simple-service](spring-boot-webflux-simple-service) - พื้นฐานการเขียน Service 
- [spring-boot-webflux-mongodb](spring-boot-webflux-mongodb) - การเชื่อมต่อ Mongodb (NoSQL Document Database)  
- [spring-boot-webflux-mongo-operations](spring-boot-webflux-mongo-operations) - การเขียน Query ผ่าน Mogo Operations 
- [spring-boot-webflux-redis](spring-boot-webflux-redis) - การเชื่อมต่อ Redis (Key/Value NoSQL) 
- [spring-boot-webflux-nuxtjs](https://github.com/jittagornp/spring-boot-webflux-nuxtjs) - การเขียน Spring-boot Reactive + Nuxt.js 
- [spring-boot-webflux-javadoc](spring-boot-webflux-javadoc) - การ Generate Java Document API 
