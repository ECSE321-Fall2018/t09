# TEAM 09

## Clearify User Stories

### Admin

- As an admin, I would like to check the **status** of all **active** drivers and passengers in the network, such that I can gain an overview of the ride sharing network. 
- As an admin, I would like to listing top drivers and passengers based on **historical data**, such that I can gain an overview of the ride sharing network.

probably we need a state for users:
```java
 enum{ON_RIDE, STANDBY} 
```

### Passenger

- As an passenger, I would like to search ads for drivers who are willing to **start** at my location and **stop** at my destination, such that I can select an appropriate journey to join from the list of results based on start and stop location. 
- As an passenger, I would like to sort results by relevant criteria (e.g., **car type**, **price**), such that I can select an appropriate journey to join.

### Driver

- As an driver, I would like to advertise journeys to passengers with 
> 1. information about the **vehicle**, 
> 2. the **available seating**, 
> 3. the locations that the driver is willing to **stop**, 
> 4. the **cost** of travel for passengers going to *each stop*
such that I can deliver accurate information to potential passengers. 

## Added login, authorization, and security feature

Pull my branch, and change application.yml: 

username, password: they must be the granted with access to local database "carpool"

if there is no "carpool", 

```mysql
mysql> CREATE DATABASE carpool; 

mysql> USE carpool; 

mysql> GRANT ALL PRIVILEGES ON carpool TO 'YOUR_DATABASE_GRANTED_USERNAME'@'localhost'; 

```
use local host if you are not remotely accessing database

Then, set up application.yml

```
server:
  port: 8080
spring:
  application:
    name: auth-service
  datasource:
    url : jdbc:mysql://localhost:3306/carpool
    username : YOUR_DATABASE_GRANTED_USERNAME
    password : PASSWORD_FOR_YOUR_DATABASE_GRANTED_USERNAME
    driverClassName : com.mysql.jdbc.Driver
  jpa:
    database : MYSQL
    show-sql : true
    schema: classpath:schema.sql
    data: data.sql
    hibernate:
      ddl-auto : update
      naming-strategy : org.hibernate.cfg.ImprovedNamingStrategy
    properties:
      hibernate:
        dialect : org.hibernate.dialect.MySQL5Dialect
logging:
  level:
    root: INFO
    org.hibernate: INFO
    org.hibernate.type.descriptor.sql.BasicBinder: TRACE
    org.hibernate.type.descriptor.sql.BasicExtractor: TRACE
    com.itmuch.youran.persistence: ERROR
```

Then, you are good to go! Enjoy using Chrome or Postman! 
Pay attention to format of JSON. 
