# spring-boot-webflux-example

ตัวอย่างการเขียน Java Spring-boot WebFlux ซึ่งเป็นการเขียน Spring-boot แบบ Non-Blocking I/O หรือ Asynchronous 

![Reactive Spring](reactive_spring.png)

# ปัญหา / ข้อเสนอแนะ 
หากพบปัญหา หรือ ต้องการให้ทำอะไรเพิ่ม รบกวนช่วยเปิด issue ให้ด้วยน่ะครับ  
ขอบคุณครับ :)  

# เอกสาร Spring 

- [Web on Reactive Stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)

# Pre Require 
สิ่งที่ต้องรู้ก่อนเขียน Spring-boot Webflux
- java 8+
- [Apache Maven](https://coderunnerth.co/2018/12/05/%E0%B8%A3%E0%B8%B9%E0%B9%89%E0%B8%88%E0%B8%B1%E0%B8%81%E0%B8%81%E0%B8%B1%E0%B8%9A-apache-maven/)
- Reactive Programming ลองอ่านนี่ดูได้ครับ [RxJava series - part 1 - ตอน อะไรเอ่ย ReactiveX?](https://medium.com/@nutron/what-is-reactivex-38293abb81cb)  ขอบคุณสำหรับบทความครับ    

Spring-boot ใช้ Reactor ซึ่งเป็น lib reactive ตัวนึง มีความคล้ายกันกับ ReactiveX สามารถอ่านแทนกันได้ครับ Concept เหมือนกัน  


# เริ่ม 

> Code บางตัวอาจจะไม่สามารถเอาไป Run ได้เลย เนื่องจากต้องเตรียม environment ต่าง ๆ ให้พร้อมก่อน เช่น Postgresql, Mongodb, Redis เป็นต้น ต้องมี database หรือ data source ปลายทางก่อน  

ให้เรียนรู้/ดูตัวอย่างตามลำดับต่อไปนี้    

- [spring-boot-webflux-helloworld](spring-boot-webflux-helloworld) - Hello World!
- [spring-boot-webflux-dockerfile](spring-boot-webflux-dockerfile) - การสร้าง Dockerfile การ Build Image และการ Run Container Spring-boot 
- [spring-boot-webflux-logging](spring-boot-webflux-logging) - การ Config และใช้งาน Logging  
- [spring-boot-webflux-controller](spring-boot-webflux-controller)  - การเขียน Controller 
- [spring-boot-webflux-configuration-properties](spring-boot-webflux-configuration-properties) - การอ่าน Config จาก application.properties 
- [spring-boot-webflux-thymleaf](spring-boot-webflux-thymleaf)  - การใช้ Thymleaf ทำ View (Server Site) Rendering (HTML)    
- [spring-boot-webflux-filter](spring-boot-webflux-filter) - การเขียน Filter  
- [spring-boot-webflux-error-handler](spring-boot-webflux-error-handler) - การจัดการ Exception หรือ Error
- [spring-boot-webflux-custom-error-handler](spring-boot-webflux-custom-error-handler) - การ Custom ตัวจัดการ Exception หรือ Error 
- [spring-boot-webflux-validation](spring-boot-webflux-validation) - การ Validate ข้อมูล  
- [spring-boot-webflux-custom-validator](spring-boot-webflux-custom-validator) - การ custom validator  
- [spring-boot-webflux-scheduling](spring-boot-webflux-scheduling) - การ Run Task แบบ Scheduling
- [spring-boot-webflux-unit-test](spring-boot-webflux-unit-test) - การเขียน Unit Test  
- [spring-boot-webflux-unit-test-mockito](spring-boot-webflux-unit-test-mockito) - การเขียน Unit Test + Mockito  
- [spring-boot-webflux-security](spring-boot-webflux-security) - พื้นฐานการ Config Spring Security สำหรับทำการ Login
- [spring-boot-webflux-custom-login-page](spring-boot-webflux-custom-login-page) - การ custom หน้า login 
- [spring-boot-webflux-security-authority](spring-boot-webflux-security-authority) - การกำหนดสิทธิ์การเข้าถึง  
- [spring-boot-webflux-postgresql](spring-boot-webflux-postgresql) - การเชื่อมต่อ Postgresql (Relational Database) 
- [spring-boot-webflux-jdbc-template](spring-boot-webflux-jdbc-template) - การเขียน Native Query จาก JDBC Template  
- [spring-boot-webflux-mongodb](spring-boot-webflux-mongodb) - การเชื่อมต่อ Mongodb (NoSQL Document Database)  
- [spring-boot-webflux-mongo-operations](spring-boot-webflux-mongo-operations) - การเขียน Query ผ่าน Mogo Operations 
- [spring-boot-webflux-redis](spring-boot-webflux-redis) - การเชื่อมต่อ Redis (Key/Value NoSQL) 

> ผมจะค่อย ๆ add module ต่าง ๆ เข้าไปเรื่อย ๆ น่ะครับ  อาจจะได้วันละ 2-3 modules แล้วแต่เวลาว่าง + ความยากง่ายของ module นั้น ๆ ครับ

# บทความอื่น ๆ เกี่ยวกับ WebFlux
- [Spring WebFlux Tutorial](https://howtodoinjava.com/spring-webflux/spring-webflux-tutorial/)
- [สร้าง Reactive RESTful Web Service ด้วย Spring WebFlux](https://medium.com/@phayao/%E0%B8%AA%E0%B8%A3%E0%B9%89%E0%B8%B2%E0%B8%87-reactive-restful-web-service-%E0%B8%94%E0%B9%89%E0%B8%A7%E0%B8%A2-spring-webflux-c42094a3424e)
- [สร้าง Reactive RESTful API ด้วย Spring WebFlux และ MongoDB](https://developers.ascendcorp.com/%E0%B8%AA%E0%B8%A3%E0%B9%89%E0%B8%B2%E0%B8%87-reactive-restful-api-%E0%B8%94%E0%B9%89%E0%B8%A7%E0%B8%A2-spring-webflux-%E0%B9%81%E0%B8%A5%E0%B8%B0-mongodb-868d645dd9f6)

ขอบคุณสำหรับเจ้าของบทความครับ 
