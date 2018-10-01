# Welcome to TEAM 09
## WANTED: Bonus 3 solution: logging

- Logging (i.e., all API interactions should be recorded in a log file for posterity, debugging, and data mining purposes)

## Tips for naming API

- APIs are data-oriented, NOT functional requirement oriented
- API names should follow create, get, delete, update naming convension

## API Endpoints

### Login

 URL | Request | Parameter Format | Parameters | Role (ROLE_) 
 ------| ------ | ------ | ------ | ------ 
 /login | POST | JSON | "username", "password" | GLOBAL 

### UserController

 URL | Request | Parameter Format | Parameters | Role (ROLE_) 
 ------| ------ | ------ | ------ | ------ 
 /user/sign-up | POST | JSON | "username", "password", and "role" | GLOBAL 
 /user/get-is-unique | POST | JSON | "username" | GLOBAL 
 /user/get-user-by-uname | POST | JSON | "username" | ADMIN 
 /user/get-logged-user | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /user/get-list-passenger-status | GET | void | void | ADMIN 
 /user/get-list-driver-status | GET | void | void | ADMIN 
 /user/get-list-users | GET | void | void | ADMIN 
 /user/update-status | PUT | JSON | "status" | ADMIN, PASSENGER, DRIVER 
 /user/delete-usr | DELETE | JSON | "id" or "username" | ADMIN 
 /user/mainpg | GET | void | void | GLOBAL 
 /user/hello | GET | void | void | PASSENGER (for test) 

### AdvertisementController

 URL | Request | Parameter Format | Parameters | Role (ROLE_)
 ------| ------ | ------ | ------ | ------ 
 /adv/create-adv | POST | JSON | "id", "title", "startTime",  "startLocation", "seatAvailable",  "stops", "vehicle" | DRIVER 
 /adv/get-adv-by-price | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-time | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-price-by-color | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vColor" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-time-by-color | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vColor" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-time-by-model | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vModel" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-price-by-model | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vModel" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-time-by-model-color | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vColor", "vModel" | ADMIN, PASSENGER, DRIVER 
 /adv/get-adv-by-price-by-model-color | POST | JSON | "stop", "startLocation", "startTimeX", "startTimeY", "vColor", "vModel" | ADMIN, PASSENGER, DRIVER 
 /adv/get-logged-adv | GET | void | void | DRIVER 
 /adv/get-top-driver | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /adv/get-list-adv | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /adv/update-adv | PUT | JSON | "id", "title", "startTime",  "startLocation", "seatAvailable",  "stops", "vehicle" | DRIVER 
 /adv/delete-adv | DELETE | JSON | "id" | DRIVER 


## We need @2018/10/01

1. Unit Test
2. Heroku SQL API key/uname/pswd

## Things done on 2018/09/30

- Distributed tasks
- Completed Advertisement Controller and Repository, including documentation
- Mapper, Stop, Vehicle are assigned and to be completed within 2 days
- The team gets a overview of project architecture

## Meeting for 2018/09/30

### Merge Branch

- Merge mark to master, or swap
- But, keep original master branch content in another one

### Development Cycle 1: Controllers: Approximately 1 day - Due Sunday

- Every member is responsible for 1 controller
- If you need some function in any entity manager, add a not implemented function with TODO in that class in your repository. 

Note, a "not implemented function" is 
```java
/**
* This function does xxx
* Core API Endpoint: (if you have one)
* @param: arg
* @return: ret
*/
public ReturnT myVirtualFunc(ParamT arg) {
    // TODO: this is a virtual function that does xxx
    return new ReturnT(); 
}
```

### Development Milestone 1: End of Sunday

- After everyone finish all the above, we merge our "not implemented functions" by discussion. 
- We come up with a list of "not implemented functions" for each class, and determine Interface of each function. 
- Also, unify naming of "not implemented functions" for each author in their classes for further integration

### Development Cycle 2: Repository: Approximately 1 day - Due Monday

- Every member is responsible for 1 repository
- Implement functions required in cycle 1
- Verify that integration works

### Development Milestone 2: End of Monday

- Merge our code, and test on local/Heroku. 
- Congratulations! We have completed programming cycle! 

### Development Cycle 3: Test, Deploy, and Evolution - Tuesday to Thursday

- Content To be determined on Milestone 2

## We can work together on it Since 2018/09/29!!!

### Follow instructions in on controllers, and distribute tasks! 

- TODO tells what we should be working on
- "Wanted tasks" calls for our insight, innovation, and intelligence! 

### Read JavaDoc to get more information and reinforcement on Core API endpoints! 

- Java Doc tells which method address to which functional requirements
- Core API endpoint in each Java Doc verifies that we can get all points!
- Please help me to verify the above. I am not a careful person lol. 

## User Stories

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
Pay attention to format of JSON.  Good
