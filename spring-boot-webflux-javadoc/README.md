# spring-boot-webflux-javadoc
ตัวอย่างการเขียน Spring-boot WebFlux Java Document 

# 1. Config Plugin

pom.xml 
``` xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-javadoc-plugin</artifactId>
            <version>3.1.1</version>
            <configuration>
                <stylesheetfile>src/main/javadoc/stylesheet.css</stylesheetfile>
            </configuration>
        </plugin>
        ...
        ...
    </plugins>
</build>
```

# 2. custom stylesheet.css 

วางไฟล์ไว้ที่ `src/main/javadoc/stylesheet.css`  

# 3. Generate Java Doc

```shell
$ mvn javadoc:javadoc 
```

# 4. ดูผลลัพธ์ 

ไฟล์จะอยู่ที่ /target/site/apidocs/index.html 
