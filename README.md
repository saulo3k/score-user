# Read Me First

 HTTP-based mini game back-end in Java which registers score points for different users, 
 with the capability to return the current user position and high score list. 


# Getting Started

### Prerequisites

Java 8 
Maven 
Docker
Redis 

### Choices

I choice the redis cache for optimize this project. 

Redis is an in-memory data structure project implementing a distributed, in-memory key-value database with optional durability. Redis supports different kinds of abstract data structures, such as strings, lists, maps, sets, sorted sets, HyperLogLogs, bitmaps, streams, and spatial indexes.

## Docker

Enter in root directory this project and execute this comand

```
$ docker-compose up
```

After run, this Redis is already 


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
* [Spring Data Redis (Access+Driver)](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-redis)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.3.0.RELEASE/reference/htmlsingle/#boot-features-developing-web-applications)

### Guides
The following guides illustrate how to use some features concretely:

* [Messaging with Redis](https://spring.io/guides/gs/messaging-redis/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)

## Authors

* **Saulo Silva** 


## License

This project is licensed under the MIT License 