# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.kafka-transfer.demo' is invalid and this project uses 'com.example.kafka_transfer.demo' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.12/maven-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.12/maven-plugin/build-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.12/reference/web/servlet.html)
* [Spring for Apache Kafka](https://docs.spring.io/spring-boot/3.4.12/reference/messaging/kafka.html)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.

### start steps
#### 1. start zookeeper
```sh
.\bin\windows\zookeeper-server-start.bat .\config\zookeeper-test.properties
```
#### 2. start broker
```sh
.\bin\windows\kafka-server-start.bat .\config\server-test.properties
```


#### 3. start Springboot application
```sh
mvn spring-boot:run
```

### test messages
``` linux
Bash
# 成功的请求
curl -X POST http://localhost:8080/api/v1/transfer/submit \
     -H "Content-Type: application/json" \
     -d '{
         "sourceAccount": "10001",
         "targetAccount": "20002",
         "amount": 500.00
        }'

# 预期失败的请求 (模拟余额不足)
curl -X POST http://localhost:8080/api/v1/transfer/submit \
     -H "Content-Type: application/json" \
     -d '{
         "sourceAccount": "10001",
         "targetAccount": "20002",
         "amount": 15000.00
        }' 
```


