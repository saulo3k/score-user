# Read Me First

 HTTP-based mini game back-end in Java which registers score points for different users, 
 with the capability to return the current user position and high score list. 

# Getting Started

### Prerequisites

Java 8 
Maven 

### Choices

I redid the test without using a Redis cache, instead of a cache, I used the map.

ConcurrentMap introduced in Java in version 1.5, it is a high-performance, thread-safety map.

With this decision I can get: thread-safety with high throughput under high concurrency

Knowing that my performance tests are merely superficial and do not serve as an advanced basis for stress testing,
I would like to share the results obtained
When performing 1 insertion via API where a for has inserted 21k players on the map, I obtained response time
300ms, when saving only 1 item it was 9ms the response time

Bearing in mind that the biggest difficulty of performance will be to seek both highscore and getPosition
I obtained values of 150 ms for 20k high score saved, fetched via API GET

and 39 ms to search for the player's position, on a map with 21,000 records.

For this solution I used DDD (Domain Driven Design) 
and developed based on TDD

### Installing

#### Build, Testing and Run

```
mvn clean package && java -jar target/score-user-0.0.1-SNAPSHOT.jar
```

## Running without tests

```
 mvn clean package -Dmaven.test.skip=true
```

## API Documentation 

#### Swagger
http://localhost:8080/swagger-ui.html#/
#### For import in Postman
http://localhost:8080/v2/api-docs

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/maven-plugin/reference/html/#build-image)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#using-boot-devtools)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

## Authors

* **Saulo Silva** 


## License

This project is licensed under the MIT License 