# spring-boot-webflux-unit-test-mockito
ตัวอย่างการเขียน Spring-boot WebFlux Unit Test + Mockito  

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

    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.12</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.assertj</groupId>
        <artifactId>assertj-core</artifactId>
        <version>3.12.2</version>
        <scope>test</scope>
        <type>jar</type>
    </dependency>

    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-all</artifactId>
        <version>1.10.19</version>
        <scope>test</scope>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <type>jar</type>
    </dependency>
</dependencies>

...
```
- junit เป็น dependency สำหรับเขียน test ภาษา java  
- assertj เป็น dependency สำหรับทำ assert (support junit ซึ่งจริง ๆ ใช้แค่ junit ก็ได้)
- mockito เป็น dependency สำหรับทำ mock (จำลองพฤติกรรม) test 
- spring-boot-starter-test เป็น dependency สำหรับ test อะไรต่างๆ ที่ผูกกับ spring-boot   

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

# 3. เขียน Logic 
``` java
public interface HttpClientIPAddressResolver {

    String resolve(ServerWebExchange exchange);

}
```

```java
@Slf4j
@Component
public class DefaultHttpClientIPAddressResolver implements HttpClientIPAddressResolver {

    private static final String CACHED_KEY = HttpClientIPAddressResolver.class.getName() + ".IP_ADDRESS";

    private static final String[] IP_ADDRESS_HEADERS = {
        "X-Forwarded-For",
        "Proxy-Client-IP",
        "WL-Proxy-Client-IP",
        "HTTP_X_FORWARDED_FOR",
        "HTTP_X_FORWARDED",
        "HTTP_X_CLUSTER_CLIENT_IP",
        "HTTP_CLIENT_IP",
        "HTTP_FORWARDED_FOR",
        "HTTP_FORWARDED",
        "HTTP_VIA",
        "REMOTE_ADDR"
    };

    private static boolean has(String ip) {
        if (!hasText(ip)) {
            return false;
        }

        return !"UNKNOWN".equalsIgnoreCase(ip);
    }

    @Override
    public String resolve(ServerWebExchange exchange) {
        Assert.notNull(exchange, "require exchange.");
        String cached = (String) exchange.getAttribute(CACHED_KEY);
        if (has(cached)) {
            return cached;
        }

        ServerHttpRequest httpReq = exchange.getRequest();
        HttpHeaders headers = httpReq.getHeaders();
        for (String header : IP_ADDRESS_HEADERS) {
            String ip = headers.getFirst(header);
            if (has(ip)) {
                return cached(ip, exchange);
            }
        }

        return cached(getRemoteAddress(httpReq), exchange);
    }

    private String getRemoteAddress(ServerHttpRequest httpReq) {
        InetSocketAddress removeAddress = httpReq.getRemoteAddress();
        if (removeAddress == null) {
            return null;
        }
        return removeAddress.toString();
    }

    private String cached(String ip, ServerWebExchange exchange) {
        Map<String, Object> attributes = exchange.getAttributes();
        if (hasText(ip)) {
            attributes.put(CACHED_KEY, ip);
        } else {
            attributes.remove(CACHED_KEY, ip);
        }
        return ip;
    }
}
```

# 4. เขียน Unit Test + Mockito 
``` java 
public class HttpClientIPAddressResolverTest {

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    private HttpClientIPAddressResolver resolver;

    @Before
    public void before() {
        resolver = new DefaultHttpClientIPAddressResolver();
    }

    @Test
    public void shouldBeThrowIllegalArgumentException_whenInputIsNull() {
        exception.expect(IllegalArgumentException.class);
        exception.expectMessage("require exchange.");

        resolver.resolve(null);
    }

    @Test
    public void shouldBeNull_whenAnyHeadersIsNull() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/"));
        String output = resolver.resolve(exchange);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBeNull_whenXForwardedForHeaderIsUnknown() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").header("X-Forwarded-For", "UNKNOWN"));
        String output = resolver.resolve(exchange);
        String expected = null;
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenXForwardedForHeaderIs127_0_0_1() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").header("X-Forwarded-For", "127.0.0.1"));
        String output = resolver.resolve(exchange);
        String expected = "127.0.0.1";
        assertThat(output).isEqualTo(expected);
    }

    @Test
    public void shouldBe127_0_0_1_whenRemoteAddrIs127_0_0_1() {
        ServerWebExchange exchange = MockServerWebExchange.from(MockServerHttpRequest.get("/").remoteAddress(InetSocketAddress.createUnresolved("127.0.0.1", 80)));
        String output = resolver.resolve(exchange);
        String expected = "127.0.0.1:80";

        assertThat(output).isEqualTo(expected);
    }

}
```
# 5. Build
cd ไปที่ root ของ project จากนั้น  
``` shell 
$ mvn clean install
```

# 6. ดูผลลัพธ์ที่ Console 

# อ่านเพิ่มเติม 

- [https://junit.org](https://junit.org)  
- [https://joel-costigliola.github.io/assertj/](https://joel-costigliola.github.io/assertj/)  
- [https://site.mockito.org/](https://site.mockito.org/)   
- [รูปแบบการเขียน Java Unit Test ของผม](https://medium.com/@jittagornp/%E0%B8%A3%E0%B8%B9%E0%B8%9B%E0%B9%81%E0%B8%9A%E0%B8%9A%E0%B8%81%E0%B8%B2%E0%B8%A3%E0%B9%80%E0%B8%82%E0%B8%B5%E0%B8%A2%E0%B8%99-java-unit-test-%E0%B8%82%E0%B8%AD%E0%B8%87%E0%B8%9C%E0%B8%A1-8408b6b27a7b)
