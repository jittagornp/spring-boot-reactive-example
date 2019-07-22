# ตัวอย่างการเขียน Reactor 

# Mono
ตัวอย่างการใช้ Mono 

### Mono.empty
เป็นการสร้าง empty mono ซึ่งจะไม่มีข้อมูลปล่อยออกมา 
```java 
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.empty()
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
- ไม่มีข้อมูล

### Mono.just 
การสร้าง Mono จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)
- ข้อมูลห้ามเป็น `null` **** 

```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.just("Hello at " + LocalDateTime.now())
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => Hello at 2019-07-22T16:07:03.309
```

### Mono.justOrEmpty
การสร้าง Mono จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)
- ข้อมูลเป็น `null` ได้

```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.justOrEmpty(null)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
- จะไม่มีการเรียก `doOnNext` เนื่องจากไม่มีข้อมูลปล่อยออกมา  

### Mono.switchIfEmpty
ทำการเปลี่ยน (switch) Mono ถ้าไม่มีข้อมูลถูกปล่อยออกมาจาก source 
```java 
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.justOrEmpty(null)
                .switchIfEmpty(Mono.just("Hello at " + LocalDateTime.now()))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => Hello at 2019-07-22T16:35:16.656 
```

### Mono.error
สำหรับปล่อยข้อมูล error หรือ Exception ออกมา 
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.justOrEmpty(null)
                .switchIfEmpty(Mono.error(new RuntimeException("Not found data")))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .doOnError(e -> {
                    log.debug("errror => {}", e.getMessage());
                })
                .subscribe();
    }

}
```
output
```
- errror => Not found data
```

### Mono.map
ทำการแปลง (Transform) ข้อมูลก่อนส่งออกมา  
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.just(1000)
                .map(number -> number * 2)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => 2000  
```

### Mono.filter
ทำการกรอง (filter) ข้อมูลตามเงื่อนไขที่กำหนด  
- example 1   
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.just(2)
                .filter(number -> (number % 2 == 0))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => 2
```
- example 2   
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.just(3)
                .filter(number -> (number % 2 == 0))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
- ไม่มีข้อมูล
 
### Mono.defer
การสร้าง Mono แบบ Lazy Load 

```java 
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.defer(() -> Mono.just("Hello at " + LocalDateTime.now()))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => Hello at 2019-07-22T16:09:45.886
```
### Mono.create
การสร้าง Mono แบบ Asynchronous
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.create(callback -> {
            try {
                log.debug("wait 3 seconds... at " + LocalDateTime.now());
                Thread.sleep(3000L);
            } catch (InterruptedException ex) {
                //
            }
            callback.success("Hello at " + LocalDateTime.now());
        })
        .doOnNext(message -> {
            log.debug("message => {}", message);
        })
        .subscribe();
    }

}
```
output
```
- wait 3 seconds... at 2019-07-22T16:16:55.602  
- message => Hello at 2019-07-22T16:16:58.603
```
### Mono.flatMap
คล้าย ๆ map คือ ทำการแปลง (Transform) ข้อมูลก่อนส่งออกมา แต่เป็นแบบ Asyncronous
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Mono.just(100)
                .flatMap(number -> {
                    return Mono.create(callback -> {
                        try {
                            log.debug("wait 3 seconds... at " + LocalDateTime.now());
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            
                        }
                        callback.success(number * 5);
                    });
                })
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- wait 3 seconds... at 2019-07-22T16:54:23.923
- message => 500
```

### Mono.zip
เป็นการรวม response จาก Mono ต่าง ๆ
- Sequencial 
```java
@Slf4j
public class ReactorExample {

    private static void delay(String name, int seconds) {
        try {
            log.debug("{} wait {} seconds", name, seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ex) {

        }
    }

    public static void main(String[] args) {
        
        Mono<String> task1 = Mono.create(callback -> {
            delay("task 1", 3);
            callback.success("Hello from Task 1");
        });
        
        Mono<String> task2 = Mono.create(callback -> {
            delay("task 2", 1);
            callback.success("Hello from Task 2");
        });
 
        log.debug("start at {}", LocalDateTime.now());
        
        Mono.zip(task1, task2)
                .doOnNext(response -> {
                    log.debug("task 1-> {}", response.getT1());
                    log.debug("task 2-> {}", response.getT2());
                })
                .doOnSuccess(response -> {
                    log.debug("end at {}", LocalDateTime.now());
                })
                .subscribe();
    }

}
```
output (ใช้เวลาทำงาน 3 + 1 = 4 วินาที)  
```
- start at 2019-07-22T18:42:30.950
- task 1 wait 3 seconds  
- task 2 wait 1 second  
- task 1-> Hello from Task 1  
- task 2-> Hello from Task 2   
- end at 2019-07-22T18:42:35.045
```

- Parallel (ใช้ `.subscribeOn(Schedulers.newElastic(name, ttlSeconds))`)
```java
@Slf4j
public class ReactorExample {

    private static void delay(String name, int seconds) {
        try {
            log.debug("{} wait {} seconds", name, seconds);
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException ex) {

        }
    }

    public static void main(String[] args) {
        
        Mono<String> task1 = Mono.create((MonoSink<String> callback) -> {
            delay("task 1", 3);
            callback.success("Hello from Task 1");
        }).subscribeOn(Schedulers.newElastic("scheduler 1", 1));
        
        Mono<String> task2 = Mono.create((MonoSink<String> callback) -> {
            delay("task 2", 1);
            callback.success("Hello from Task 2");
        }).subscribeOn(Schedulers.newElastic("scheduler 2", 1));
        
        Mono<String> task3 = Mono.create((MonoSink<String> callback) -> {
            delay("task 3", 5);
            callback.success("Hello from Task 3");
        }).subscribeOn(Schedulers.newElastic("scheduler 3", 1));
        
        log.debug("start at {}", LocalDateTime.now());
        
        Mono.zip(task1, task2, task3)
                .doOnNext(response -> {
                    log.debug("task 1-> {}", response.getT1());
                    log.debug("task 2-> {}", response.getT2());
                    log.debug("task 3-> {}", response.getT3());
                })
                .doOnSuccess(response -> {
                    log.debug("end at {}", LocalDateTime.now());
                })
                .subscribe();
    }

}
```
output (ใช้เวลาทำงานมากที่สุดคือ 5 วินาที)    
```
- start at 2019-07-22T18:38:45.892
- task 1 wait 3 seconds  
- task 2 wait 1 seconds  
- task 3 wait 5 seconds  
- task 1-> Hello from Task 1  
- task 2-> Hello from Task 2  
- task 3-> Hello from Task 3  
- end at 2019-07-22T18:38:50.920
```
### Mono.block
การทำงานแบบ Blocking I/O หรือ Synchronous
```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        String message = Mono.just("Hello World").block();
        log.debug("message => {}", message);
    }

}
```
output
```
- message => Hello World
```

# Flux
ตัวอย่างการใช้ Flux
### Flux.just 
การสร้าง Flux จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)

```java
@Slf4j
public class ReactorExample {

    public static void main(String[] args) {
        Flux.just("1", "2", "3", "4", "5")
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => 1  
- message => 2  
- message => 3  
- message => 4  
- message => 5 
```

