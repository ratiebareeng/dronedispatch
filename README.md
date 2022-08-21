# Spring Drones Dispatch API

This is a Java / Maven / Spring Boot (version 2.7.3)
service application that allows managing medication
delivery drones.

## How to Run
This application is packaged as a jar, and you run it using the java -jar command.

* Clone this repository
* Make sure you are using JDK 1.8 or higher
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

-----
## Basic Authentication
Basic autentiation has been configured with the following default users. Use any of the credentials to sign in.

```
admin/adminpass - ADMIN
user/userpass - USER
```

* ADMIN has READ/WRITE Permissions
* USER has READ Permissions

---

## Browser Access
http://localhost:8080/api/v1/drones-dispatch

---
## API Endpoints

[Postman Drones Dispatch API Documentation](https://documenter.getpostman.com/view/19790527/VUqptHdb)

Get paged list of all drones
````
GET http://localhost:8080/api/v1/drones-dispatch/drones
````

Get list of medication loaded onto drone
````
GET http://localhost:8080/api/v1/drones-dispatch/loadedMedication/{serialNumber}
````

Get paged list of available [IDLE] drones
````
GET http://localhost:8080/api/v1/drones-dispatch/availableDrones
````

Get drone battery level
````
GET http://localhost:8080/api/v1/drones-dispatch/batteryLevel/{serialNumber}
````

Delete drone
````
DELETE http://localhost:8080/api/v1/drones-dispatch/deleteDrone/{serialNumber}
````
Register new drone
````
POST http://localhost:8080/api/v1/registerDrone
````

Load drone with medication
````
POST http://localhost:8080/api/v1/loadDrone/{serialNumber}
````

Update existing drone
````
PUT http://localhost:8080/api/v1/updateDrone/{serialNumber}
````