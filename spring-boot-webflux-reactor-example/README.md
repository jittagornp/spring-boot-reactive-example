# ตัวอย่างการเขียน Reactor 

> [https://projectreactor.io](https://projectreactor.io)  

Reactor เป็น library สำหรับเขียน Reactive เหมือนกันกับ Rx (Reactive Extension) ที่ทาง Microsoft สร้างขึ้นมา [http://reactivex.io/](http://reactivex.io/)  
ซึ่ง implements  ตาม Spec ของ Reactive Streams [https://www.reactive-streams.org/](https://www.reactive-streams.org/)   
ใช้เป็น Core ในการเขียน Spring-boot WebFlux ซึ่งเป็นการเขียน Spring-boot แบบ Non-Blocking I/O   
การใช้ Reactor จะมี 2 ส่วนหลัก ๆ ที่ต้องทำความเข้าใจ คือ 
- `Mono` เป็น Publisher สำหรับปล่อย (publish) ข้อมูลตั้งแต่ 0 ถึง 1 element และ 
- `Flux` เป็น Publisher สำหรับปล่อย (publish) ข้อมูลตั้งแต่ 0 ถึง N elements 
  
พื้นฐานการใช้ Reactor จะเหมือนกันกับ RxJava ฉะนั้นสามารถอ่าน Concept ต่าง ๆ แทนกันได้ โดยอ่านได้จากบทความนี้ [RxJava series - part 1 - ตอน อะไรเอ่ย ReactiveX?](https://medium.com/@nutron/what-is-reactivex-38293abb81cb) ขอบคุณเจ้าของบทความครับ  

# บทความอื่น ๆ
- [https://projectreactor.io/learn](https://projectreactor.io/learn)
- [Reactive systems using Reactor](https://musigma.blog/2016/11/21/reactor.html)
- [Reactor by Example](https://www.infoq.com/articles/reactor-by-example/) 

# Flow

Reactor `Mono` และ `Flux` จะมี Flow การทำงานตามลำดับเป็นดังนี้ 

### Mono Flow

```java
@Slf4j
public class MonoFlowExample {

    public static void main(String[] args) {
        Mono.just("A")
                .doFirst(() -> log.debug("doFirst..."))
                .doOnRequest(value -> log.debug("doOnRequest... {}", value))
                .doOnEach(signal -> log.debug("doOnEach... {} : value => {}", signal.getType().name(), signal.get()))
                .doOnNext(value -> log.debug("doOnNext... {}", value))
                .doOnCancel(() -> log.debug("doOnCacel..."))
                .doOnError(e -> log.debug("doOnError... {}", e.getMessage()))
                .doOnSuccess(value -> log.debug("doOnSuccess... {}", value))
                .doOnSuccessOrError((value, e) -> log.debug("doOnSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                .doAfterSuccessOrError((value, e) -> log.debug("doAfterSuccessOrError... {} or {}", value, (e == null ? null : e.getMessage())))
                .doAfterTerminate(() -> log.debug("doAfterTerminate..."))
                .doOnTerminate(() -> log.debug("doOnTerminate..."))
                .doOnSubscribe(subscription -> {
                    long id = 1234567890;
                    subscription.request(id);
                    log.debug("doOnSubscribe... {}", id);
                })
                .doFinally(signalType -> log.debug("doFinally... {}", signalType.toString()))
                .doOnDiscard(Object.class, value -> log.debug("doOnDiscard... {}", value))
                .subscribe();
    }

}
```
output  
```
- doFirst...  
- doOnRequest... 1234567890  
- doOnEach... ON_NEXT : value => A  
- doOnEach... ON_COMPLETE : value => null  
- doOnNext... A  
- doOnSuccess... A  
- doOnSuccessOrError... A or null  
- doAfterTerminate...  
- doAfterSuccessOrError... A or null  
- doOnTerminate...  
- doFinally... onComplete  
- doOnSubscribe... 1234567890  
- doOnCacel...  
```

### Flux Flow

```java
@Slf4j
public class FluxFlowExample {

    public static void main(String[] args) {
        Flux.just("A", "B", "C")
                .doFirst(() -> log.debug("doFirst..."))
                .doOnRequest(value -> log.debug("doOnRequest... {}", value))
                .doOnEach(signal -> log.debug("doOnEach... {} : value => {}", signal.getType().name(), signal.get()))
                .doOnNext(value -> log.debug("doOnNext... {}", value))
                .doOnCancel(() -> log.debug("doOnCacel..."))
                .doOnError(e -> log.debug("doOnError... {}", e.getMessage()))
                .doOnComplete(() -> log.debug("doOnComplete..."))
                .doAfterTerminate(() -> log.debug("doAfterTerminate..."))
                .doOnTerminate(() -> log.debug("doOnTerminate..."))
                .doOnSubscribe(subscription -> {
                    long id = 123456890;
                    subscription.request(id);
                    log.debug("doOnSubscribe... {}", id);
                })
                .doFinally(signalType -> log.debug("doFinally... {}", signalType.toString()))
                .doOnDiscard(Object.class, value -> log.debug("doOnDiscard... {}", value))
                .subscribe();
    }

}
```
output
```
- doFirst...  
- doOnRequest... 123456890  
- doOnNext... A  
- doOnEach... ON_NEXT : value => A  
- doOnNext... B  
- doOnEach... ON_NEXT : value => B  
- doOnNext... C  
- doOnEach... ON_NEXT : value => C  
- doOnEach... ON_COMPLETE : value => null  
- doOnComplete...  
- doOnTerminate...  
- doFinally... onComplete  
- doAfterTerminate...  
- doOnSubscribe... 123456890    
- doOnCacel...  
```

# Table of Content 
- [Mono](#mono)
  - [Mono.empty](#monoempty)
  - [Mono.just](#monojust)
  - [Mono.log](#monolog)
  - [Mono.justOrEmpty](#monojustorempty)
  - [Mono.switchIfEmpty](#monoswitchifempty)
  - [Mono.error](#monoerror)
  - [Mono.map](#monomap)
  - [Mono.filter](#monofilter)
  - [Mono.defer](#monodefer)
  - [Mono.create](#monocreate)
  - [Mono.flatMap](#monoflatmap)
  - [Mono.zip](#monozip)
  - [Mono.block](#monoblock)
  - [Mono.cache](#monocache)
  - [Mono.flux](#monoflux)
  - [Mono.then](#monothen)
  - [Mono.concatWith](#monoconcatwith)
  - [Mono.timeout](#monotimeout)
  - [Mono.filterWhen](#monofilterwhen)
- [Flux](#flux)
  - [Flux.just](#fluxjust)
  - [Flux.fromIterable](#fluxfromiterable)
  - [Flux.fromStream](#fluxfromstream)
  - [Flux.range](#fluxrange)
  - [Flux.concat](#fluxconcat)
  - [Flux.create](#fluxcreate)
  - [Flux.count](#fluxcount)
  - [Flux.repeat](#fluxrepeat)
  - [Flux.collectList](#fluxcollectlist)
  - [Flux.skip](#fluxskip)
  - [Flux.take](#fluxtake)
  - [Flux.all](#fluxall)
  - [Flux.any](#fluxany)
  - [Flux.filter](#fluxfilter)
  - [Flux.map](#fluxmap)
  - [Flux.buffer](#fluxbuffer)
  - [Flux.sample](#fluxsample)
  - [Flux.distinct](#fluxdistinct)
  - [Flux.sort](#fluxsort)
  - [Flux.zip](#fluxzip)

# Mono
ตัวอย่างการใช้ Mono 

### Mono.empty
เป็นการสร้าง empty mono ซึ่งจะไม่มีข้อมูลปล่อยออกมา 
```java 
@Slf4j
public class MonoEmptyExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.just 
การสร้าง Mono จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)
- ข้อมูลห้ามเป็น `null` ****  เพราะจะเกิด `java.lang.NullPointerException: value`

```java
@Slf4j
public class MonoJustExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.log
การ log ข้อมูลแต่ละ step ออกมาดู
```java
@Slf4j
public class MonoLogExample {

    public static void main(String[] args) {
        Mono.just("Hello at " + LocalDateTime.now())
                .log()
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- | onSubscribe([Synchronous Fuseable] Operators.ScalarSubscription)  
- | request(unbounded)  
- | onNext(Hello at 2019-07-22T21:12:06.912)  
- message => Hello at 2019-07-22T21:12:06.912  
- | onComplete()  
```
[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.justOrEmpty
การสร้าง Mono จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)
- ข้อมูลเป็น `null` ได้

```java
@Slf4j
public class MonoJustOrEmptyExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.switchIfEmpty
ทำการเปลี่ยน (switch) Mono ถ้าไม่มีข้อมูลถูกปล่อยออกมาจาก source 
```java 
@Slf4j
public class MonoSwithIfEmptyExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.error
สำหรับปล่อยข้อมูล error หรือ Exception ออกมา 
```java
@Slf4j
public class MonoErrorExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.map
ทำการแปลง (Transform) ข้อมูลก่อนส่งออกมา  
```java
@Slf4j
public class MonoMapExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.filter
ทำการกรอง (filter) ข้อมูลตามเงื่อนไขที่กำหนด  
- example 1   
```java
@Slf4j
public class MonoFilterExample1 {

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
public class MonoFilterExample1 {

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

[กลับไปข้างบน &#x2191;](#table-of-content)
 
### Mono.defer
การสร้าง Mono แบบ Lazy Load 

```java 
@Slf4j
public class MonoDeferExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.create
การสร้าง Mono แบบ Asynchronous
```java
@Slf4j
public class MonoCreateExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.flatMap
คล้าย ๆ map คือ ทำการแปลง (Transform) ข้อมูลก่อนส่งออกมา แต่เป็นแบบ Asyncronous
```java
@Slf4j
public class MonoFlatMapExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.zip
เป็นการรวม response จาก Mono ต่าง ๆ
- Sequencial 
```java
@Slf4j
public class MonoZipExample1 {

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
public class MonoZipExample2 {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.block
การทำงานแบบ Blocking I/O หรือ Synchronous
```java
@Slf4j
public class MonoBlockExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.cache
สำหรับ Cache ข้อมูล ตามเวลาที่กำหนด  
- กรณีไม่ cache 
```java
@Slf4j
public class MonoCacheExample1 {

    public static void main(String[] args) {
        Mono<String> defer = Mono.defer(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just("Hello at " + LocalDateTime.now());
        });
        
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
    }

}
```
output
```
- message => Hello at 2019-07-22T21:06:21.169  
- message => Hello at 2019-07-22T21:06:22.186  
- message => Hello at 2019-07-22T21:06:23.186  
```
สังเกตว่า ผลลัพธ์ (เวลาของแต่ละ message) ไม่เหมือนกัน  

- กรณี cache 
```java
@Slf4j
public class MonoCacheExample2 {

    public static void main(String[] args) {
        Mono<String> defer = Mono.defer(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just("Hello at " + LocalDateTime.now());
        }).cache(Duration.ofMinutes(5));
        
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
        log.debug("message => {}", defer.block());
    }

}
```
output
```
- message => Hello at 2019-07-22T21:08:31.852  
- message => Hello at 2019-07-22T21:08:31.852  
- message => Hello at 2019-07-22T21:08:31.852  
```
ข้อมูล message ที่ 2 และ 3 เหมือน message ที่ 1 เนื่องจากมีการ cache result ไว้ 5 นาที  

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.flux
การแปลงจาก Mono -> Flux 
```java 
@Slf4j
public class MonoFluxExample {

    public static void main(String[] args) {
        Flux<String> flux = Mono.just("Hello at " + LocalDateTime.now())
                .flux();

        flux.doOnNext(message -> {
            log.debug("message => {}", message);
        })
                .subscribe();
    }

}
```
output
```
- message => Hello at 2019-07-22T21:17:33.880  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.then
การทำงานตามลำดับด้วย `then`
```java
@Slf4j
public class MonoThenExample {

    private static Mono<String> task(final String message, int delay) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just(message + " " + LocalDateTime.now());
        });
    }

    public static void main(String[] args) {

        Mono<String> task1 = task("Hello 1 at", 3);
        Mono<String> task2 = task("Hello 2 at", 1);
        Mono<String> task3 = task("Hello 3 at", 2);
        
        task1
                .doOnNext(message -> {
                    log.debug("message 1 => {}", message);
                })
                .then(task2)
                .doOnNext(message -> {
                    log.debug("message 2 => {}", message);
                })
                .then(task3)
                .doOnNext(message -> {
                    log.debug("message 3 => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message 1 => Hello 1 at 2019-07-22T21:28:56.144  
- message 2 => Hello 2 at 2019-07-22T21:28:57.146  
- message 3 => Hello 3 at 2019-07-22T21:28:59.147  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.concatWith
เป็นการเชื่อม Mono 2 อันเข้าด้วยกัน กลายเป็น Flux 
```java
@Slf4j
public class MonoConcatWithExample {

    private static Mono<String> task(final String message, int delay) {
        return Mono.defer(() -> {
            try {
                Thread.sleep(delay * 1000);
            } catch (InterruptedException ex) {

            }
            return Mono.just(message + " " + LocalDateTime.now());
        });
    }

    public static void main(String[] args) {

        Mono<String> task1 = task("Hello 1 at", 3);
        Mono<String> task2 = task("Hello 2 at", 1);

        Flux<String> flux = task1.concatWith(task2);

        flux
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => Hello 1 at 2019-07-22T21:41:58.132  
- message => Hello 2 at 2019-07-22T21:41:59.161  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.timeout
ใช้สำหรับจำกัดเวลาในการตอบสนอง เช่น ถ้าไม่ตอบสนองภายใน 3 วินาที จะเกิด `java.util.concurrent.TimeoutException`  
```java
@Slf4j
public class MonoTimeoutExample {

    public static void main(String[] args) {
        Mono.create(callback -> {
            try {
                log.debug("wait 5 seconds... at " + LocalDateTime.now());
                Thread.sleep(5000L);
            } catch (InterruptedException ex) {
                //
            }
            callback.success("Hello at " + LocalDateTime.now());
        })
                .timeout(Duration.ofSeconds(3))
                .onErrorResume(TimeoutException.class, e -> Mono.just("Hello from timeout"))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- wait 5 seconds... at 2019-07-23T14:53:27.468  
- message => Hello from timeout  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Mono.filterWhen
ทำการกรอง (filter) ข้อมูลตามเงื่อนไขที่กำหนด เหมือน `Flux.filter` แต่ทำงานแบบ Asynchronous 
```java
@Slf4j
public class MonoFilterWhenExample {

    public static void main(String[] args) {
        int randomNumber = (int) (Math.random() * 100); //0 to 100
        log.debug("random number => {}", randomNumber);
        Mono.just(randomNumber)
                .filterWhen(number -> {
                    return Mono.create(callback -> {
                        try {
                            log.debug("wait 3 seconds... at " + LocalDateTime.now());
                            Thread.sleep(3000L);
                        } catch (InterruptedException ex) {
                            //
                        }
                        callback.success(number % 2 == 0);
                    });
                })
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .doOnSuccess(message -> {
                    log.debug("success at " + LocalDateTime.now());
                })
                .subscribe();
    }

}
```
output
- result 1 
```
- random number => 65  
- wait 3 seconds... at 2019-07-23T15:34:27.101  
- success at 2019-07-23T15:34:30.101  
```
- result 2 
```
- random number => 46  
- wait 3 seconds... at 2019-07-23T15:35:41.460  
- message => 46  
- success at 2019-07-23T15:35:44.462  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

# Flux
ตัวอย่างการใช้ Flux
### Flux.just 
การสร้าง Flux จากข้อมูลที่มีอยู่แล้ว (ข้อมูลต้องพร้อมแล้ว)
- ข้อมูลห้ามเป็น `null` เพราะจะเกิด `java.lang.NullPointerException`  
```java
@Slf4j
public class FluxJustExample {

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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.fromIterable
การสร้าง Flux จาก Java Collections (Iterable)  
- ข้อมูลห้ามเป็น `null` เพราะจะเกิด `java.lang.NullPointerException: iterable` 
```java
@Slf4j
public class FluxFromIterableExample {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("1", "2", "3", "4", "5");
        Flux.fromIterable(list)
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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.fromStream
การสร้าง Flux จาก Java 8 Stream 
- ข้อมูลห้ามเป็น `null` เพราะจะเกิด `java.lang.NullPointerException: Stream s must be provided` 
```java
@Slf4j
public class FluxFromStreamExample {

    public static void main(String[] args) {
        Stream<String> stream = Stream.of("1", "2", "3", "4", "5");
        Flux.fromStream(stream)
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

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.range 
การสร้าง Flux จากช่วงที่กำหนด (start จาก 3 ไป 5 จำนวน)   
```java
@Slf4j
public class FluxRangeExample {

    public static void main(String[] args) {
        int start = 3;
        int count = 5;
        Flux.range(start, count)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => 3  
- message => 4  
- message => 5  
- message => 6  
- message => 7 
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.concat
สำหรับ concat หรือ ต่อ Publisher ต่าง ๆ ให้เป็น Flux เดียว 
- example 1
```java
@Slf4j
public class FluxConcatExample1 {

    public static void main(String[] args) {
        Flux.concat(
                Mono.just("task 1"),
                Mono.just("task 2"),
                Mono.just("task 3"),
                Mono.just("task 4"),
                Mono.just("task 5")
        )
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => task 1  
- message => task 2  
- message => task 3  
- message => task 4  
- message => task 5    
```
- example 2
```java
@Slf4j
public class FluxConcatExample2 {

    public static void main(String[] args) {
        Flux.concat(
                Mono.just("1"),
                Flux.just("2", "3"),
                Mono.just("4"),
                Flux.just("5", "6", "7"),
                Mono.just("8")
        )
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
- message => 6  
- message => 7  
- message => 8    
``` 

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.create
การสร้าง Flux แบบ Asynchronous เหมือน `Mono.create`  
```java
@Slf4j
public class FluxCreateExample {

    public static void main(String[] args) {
        Flux.create(callback -> {
            for (int i = 1; i <= 5; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                   
                }
                callback.next("task " + i + " at " + LocalDateTime.now());
            }
            callback.complete();
            
        })
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
- หากหยุดปล่อยข้อมูลแล้ว อย่าลืม call `.complete()` ให้เป็นนิสัย เพื่อไม่ให้เกิด `Memory Leak`    
output
```
- message => task 1 at 2019-07-22T23:47:54.078  
- message => task 2 at 2019-07-22T23:47:55.079  
- message => task 3 at 2019-07-22T23:47:56.093  
- message => task 4 at 2019-07-22T23:47:57.106  
- message => task 5 at 2019-07-22T23:47:58.106  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.count
การนับจำนวน elements 
```java 
@Slf4j
public class FluxCountExample {

    public static void main(String[] args) {
        Flux.just("1", "2", "3", "4", "5")
                .count()
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => 5  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.repeat 
การทำซ้ำ
```java
@Slf4j
public class FluxRepeatExample {

    public static void main(String[] args) {
        Flux.just("1", "2", "3")
                .repeat(1)
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
- message => 1  
- message => 2  
- message => 3  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.collectList 
การแปลงจาก `Flux<?>` ไปเป็น `Mono<List<?>>`   
```java 
@Slf4j
public class FluxCollectListExample {

    public static void main(String[] args) {
        Mono<List<String>> list = Flux.just("1", "2", "3", "4", "5")
                .collectList();
        
                list.doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => [1, 2, 3, 4, 5]  
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.skip
สำหรับกระโดดข้ามข้อมูลตามจำนวนที่กำหนด    
```java
@Slf4j
public class FluxSkipExample {

    public static void main(String[] args) {
        Flux.just("A", "B", "C", "D", "E", "F", "G", "H")
                .skip(2)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => C  
- message => D  
- message => E   
- message => F   
- message => G   
- message => H   
```
[กลับไปข้างบน &#x2191;](#table-of-content)  

### Flux.take
สำหรับเลือกข้อมูลตามจำนวนที่กำหนด   
```java 
@Slf4j
public class FluxTakeExample {

    public static void main(String[] args) {
        Flux.just("A", "B", "C", "D", "E", "F", "G", "H")
                .skip(2)
                .take(3)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => C  
- message => D  
- message => E    
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.all
สำหรับเช็คว่าข้อมูล `ทั้งหมด` match กับเงื่อนไขที่ตั้งไว้หรือไม่ 
```java
@Slf4j
public class FluxAllExample {

    public static void main(String[] args) {
        Flux.just(
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        )
                .all(day -> day.endsWith("day"))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => true
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.any
สำหรับเช็คว่าข้อมูล `บางตัว` match กับเงื่อนไขที่ตั้งไว้หรือไม่ 
```java
@Slf4j
public class FluxAnyExample {

    public static void main(String[] args) {
        Flux.just(
                "Sunday",
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday"
        )
                .any(day -> day.startsWith("Mon"))
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- message => true
```

[กลับไปข้างบน &#x2191;](#table-of-content)

### Flux.filter
ทำการกรอง (filter) ข้อมูลตามเงื่อนไขที่กำหนด เหมือน `Mono.filter` 

```java
@Slf4j
public class FluxFilterExample {
    
    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5)
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
- message => 4  
```

[กลับไปข้างบน &#x2191;](#table-of-content)  

### Flux.map 
ทำการแปลง (Transform) ข้อมูลก่อนส่งออกมา เหมือน `Mono.map`   
```java
@Slf4j
public class FluxMapExample {
    
    public static void main(String[] args) {
        Flux.just(1, 2, 3, 4, 5)
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
- message => 2  
- message => 4 
- message => 6  
- message => 8 
- message => 10 
```

[กลับไปข้างบน &#x2191;](#table-of-content)    

### Flux.buffer
เก็บข้อมูลลง buffer ตามจำนวนที่กำหนด แล้วค่อยปล่อยออกมาเป็นชุด  
```java
@Slf4j
public class FluxBufferExample {

    public static void main(String[] args) {
        Flux.create(callback -> {
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {

                }
                callback.next(i);
            }
            callback.complete();

        })
                .buffer(5)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }

}
```
- การใช้ `Flux.create` อย่าลืม call `.complete()` ด้วยเสมอ เพื่อป้องกัน `Memory Leak`  
output
```
- message => [0, 1, 2, 3, 4]  
- message => [5, 6, 7, 8, 9]  
- message => [10, 11, 12, 13, 14]  
```

[กลับไปข้างบน &#x2191;](#table-of-content)    

### Flux.sample
สำหรับชะลอการรับข้อมูลตามเวลาที่กำหนด (บางครั้งข้อมูลปล่อยออกมาเร็วเกินไป)
- เช่น การพิมพ์ keyboard 
```java
@Slf4j
public class FluxSampleExample {

    public static void main(String[] args) {
        Flux.create(callback -> {
            for (int i = 0; i < 15; i++) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException ex) {

                }
                callback.next(i);
            }
            callback.complete();

        })
                .sample(Duration.ofMillis(300))
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
- message => 4  
- message => 7  
- message => 10  
- message => 12  
- message => 14  
```
- การใช้ `Flux.create` อย่าลืม call `.complete()` ด้วยเสมอ เพื่อป้องกัน `Memory Leak`    

[กลับไปข้างบน &#x2191;](#table-of-content)  

### Flux.distinct
การจำแนกข้อมูลที่แตกต่างกัน

```java
@Slf4j
public class FluxDistinctExample {
    
    public static void main(String[] args) {
        Flux.just("A", "B", "C", "A", "A", "B", "D")
                .distinct()
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
```
output
```
- message => A  
- message => B  
- message => C  
- message => D  
```

[กลับไปข้างบน &#x2191;](#table-of-content)  
 
### Flux.sort 
สำหรับจัดเรียงข้อมูล
```java
@Slf4j
public class FluxTodoExample {

    public static void main(String[] args) {
        Flux.just(2, 3, 5, 4, 1, 9, 7, 6, 8, 0)
                .doOnNext(message -> {
                    log.debug("before sort => {}", message);
                })
                .sort((numb1, numb2) -> numb1 - numb2)
                .doOnNext(message -> {
                    log.debug("sorted => {}", message);
                })
                .subscribe();
    }

}
```
output
```
- before sort => 2  
- before sort => 3  
- before sort => 5  
- before sort => 4  
- before sort => 1  
- before sort => 9  
- before sort => 7  
- before sort => 6  
- before sort => 8  
- before sort => 0  
- sorted => 0  
- sorted => 1  
- sorted => 2  
- sorted => 3  
- sorted => 4  
- sorted => 5  
- sorted => 6  
- sorted => 7   
- sorted => 8   
- sorted => 9  
```
[กลับไปข้างบน &#x2191;](#table-of-content)  
 
### Flux.zip  
เป็นการผสาน/รวม ข้อมูลแต่ละกล่มหรือคู่ flux เข้าด้วยกัน  
- example 1
```java
@Slf4j
public class FluxZipExample1 {
    
    public static void main(String[] args) {
        
        Flux<String> flux1 = Flux.just("1", "2", "3", "4", "5");
        Flux<String> flux2 = Flux.just("6", "7", "8");
        
        Flux.zip(flux1, flux2)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
```
output
```
- message => [1,6]  
- message => [2,7]  
- message => [3,8]  
```
- exxample 2
```java
@Slf4j
public class FluxZipExample2 {
    
    public static void main(String[] args) {
        
        Flux<String> flux1 = Flux.just("1", "2", "3", "4", "5");
        Flux<String> flux2 = Flux.just("6", "7", "8");
        Flux<String> flux3 = Flux.just("9", "10", "11", "12");
        
        Flux.zip(flux1, flux2, flux3)
                .doOnNext(message -> {
                    log.debug("message => {}", message);
                })
                .subscribe();
    }
    
}
```
output
```
- message => [1,6,9]  
- message => [2,7,10]  
- message => [3,8,11]  
```

[กลับไปข้างบน &#x2191;](#table-of-content)    


