# spring-boot-webflux-example

ตัวอย่างการเขียน Java Spring-boot WebFlux ซึ่งเป็นการเขียน Spring-boot แบบ Non-Blocking I/O  

# เอกสาร Spring 

- [Web on Reactive Stack](https://docs.spring.io/spring/docs/current/spring-framework-reference/web-reactive.html)

# Pre Require 
สิ่งที่ต้องรู้ก่อนเขียน Spring-boot Webflux
- java 8+
- [Apache Maven](https://coderunnerth.co/2018/12/05/%E0%B8%A3%E0%B8%B9%E0%B9%89%E0%B8%88%E0%B8%B1%E0%B8%81%E0%B8%81%E0%B8%B1%E0%B8%9A-apache-maven/)
- Reactive Programming ลองอ่านนี่ดูได้ครับ [RxJava series - part 1 - ตอน อะไรเอ่ย ReactiveX?](https://medium.com/@nutron/what-is-reactivex-38293abb81cb)  ขอบคุณสำหรับบทความครับ    

Spring-boot ใช้ Reactor ซึ่งเป็น lib reactive ตัวนึง มีความคล้ายกันกับ ReactiveX สามารถอ่านแทนกันได้ครับ Concept เหมือนกัน  


# เริ่ม 
ให้เรียนรู้/ดูตัวอย่างตามลำดับต่อไปนี้    

> Code บางตัวอาจจะไม่สามารถเอาไป Run ได้เลย เนื่องจากต้องเตรียม environment ต่าง ๆ ให้พร้อมก่อน เช่น Postgresql, Mongodb, Redis เป็นต้น ต้องมี database หรือ data source ปลายทางก่อน  

- [spring-boot-webflux-helloworld](spring-boot-webflux-helloworld)
- [spring-boot-webflux-controller](spring-boot-webflux-controller) 
- [spring-boot-webflux-thymleaf](spring-boot-webflux-thymleaf) 
- [spring-boot-webflux-filter](spring-boot-webflux-filter)
- [spring-boot-webflux-error-handler](spring-boot-webflux-error-handler) 
- [spring-boot-webflux-scheduling](spring-boot-webflux-scheduling)
- [spring-boot-webflux-postgresql](spring-boot-webflux-postgresql)
- [spring-boot-webflux-mongodb](spring-boot-webflux-mongodb)
- [spring-boot-webflux-redis](spring-boot-webflux-redis) 
