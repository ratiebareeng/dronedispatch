# Spring Drones Dispatch API

This is a Java / Maven / Spring Boot (version 2.7.3) 
service application that allows managing medication
delivery drones.

## How to Run
This application is packaged as a jar, and you run it using the java -jar command.

* Clone this repository
* Make sure you are using JDK 1.8 and Maven 3.x
* Navigate to project root directory and build the project by running:
`mvn clean package`
* Once successfully built, you can run the service by: 

```
  cd target
  java -jar drones-dispatch-0.0.1-SNAPSHOT.jar
```

Once the application runs you should see something like this:


````
2022-08-19 15:57:07.157  INFO 7724 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 8080 (http) with context path ''
2022-08-19 15:57:07.187  INFO 7724 --- [           main] c.o.d.DronesDispatchApplication          : Started DronesDispatchApplication in 8.985 seconds (JVM running for 9.713)
````

Followed by sample data import like this:
```
Hibernate:

    drop table if exists drone CASCADE
Hibernate:

    drop table if exists loaded_medication CASCADE
Hibernate:

    drop table if exists medication CASCADE
```