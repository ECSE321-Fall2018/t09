# Welcome to TEAM 09 [![Build Status](https://travis-ci.com/ECSE321-Fall2018/t09.svg?token=WDPhpGnigsQCoWp5WMJt&branch=mark)](https://travis-ci.com/ECSE321-Fall2018/t09/builds/87110237#L2557)
## Update of 2018/10/20: Just two tips

- Make sure to use descriptive Ids when modify the view
- When you want to send a request to  the backend that requires a role Anthorization, you can choose to use a request method from AsyncHttpClient that has a Header[] as argument, and put the Token as a basicHeader in the Header[]. This is a little troublesome since you have to give a Header[] for every request method, but it is the only approach I found for now that works for both AsyncHttpClient and Bearer Token. 
For example: 
```
 public void test(View view){
        Header[] headers = {new BasicHeader("Authorization","Bearer "+getsavedToken(getApplicationContext()))};
        HttpUtils.get(getApplicationContext(), "adv/get-logged-adv", headers, new RequestParams(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                log.d("Failure","");
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                log.d("Success",responseString);
            }
        });
    }
```

## Update of 2018/10/18: Start of Sprint2: Android Application for Driver and Passenger 

### Features implemented (for both Driver and Passenger) : 
#### 1. Login Activity:

- User can enter username and password to login by send a post request to the backend.
- Can switch to Signup Screen.
- User can check "remeber me" to remeber the password (Achived with sharedpreferences)
#### 2. SignUp Activity:

- User can enter username, password to signup by send a post request to the backend.
- The second password textfield will validate password input,otherwise register button is disabled.

---

## Update of 2018/10/03: Good luck on your EXAMS!!! OS and Ocaml!!!

- Concluded that security feature will not block mockit from unit testing, but security features needs integration test, which is not part of our project
- Able to junit test our api with mockit, example provided on UserController; Remind: update your pom.xml
- Solved ALL bonus problems


## WANTED: Bonus 3 solution: logging

- Logging (i.e., all API interactions should be recorded in a log file for posterity, debugging, and data mining purposes)

## Tips for naming API

- APIs are data-oriented, NOT functional requirement oriented
- API names should follow create, get, delete, update naming convension

## API Endpoints
**IMPORTANT**: A JWT authorization token is given in the response header after the user logs into the app. **All** requests that do **not** have the
Role: GLOBAL will need to provide this authorization token in the headers of the request.
Example:
- Header name: Authorization
- Header value: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJKdWxpYW5UZXN0IiwiZXhwIjoxNTM4OTY0NzkyfQ.z5EqLwlCzonLzosFGBwveOmrgU-  LZ4wEK2LEJpO3WkIXQQszs4l78uQOmkOeu5r1Ae5HQllr1V3wtA05LXxt_A
### Login

 URL | Request | Parameter Format | Parameters | Role (ROLE_) 
 ------| ------ | ------ | ------ | ------ 
 /login | POST | JSON | "username", "password" | GLOBAL 

### UserController

 URL | Request | Parameter Format | Parameters | Role (ROLE_) 
 ------| ------ | ------ | ------ | ------ 
 /user/sign-up | POST | JSON | "username", "password", and "role" (MUST BE ONE OF: {"ROLE_DRIVER","ROLE_PASSENGER","ROLE_ADMIN"}) | GLOBAL 
 /user/get-is-unique | POST | JSON | "username" | GLOBAL 
 /user/get-user-by-uname | POST | JSON | "username" | ADMIN 
 /user/get-logged-user | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /user/get-list-passenger-status | GET | void | void | ADMIN 
 /user/get-list-driver-status | GET | void | void | ADMIN 
 /user/get-list-users | GET | void | void | ADMIN 
 /user/update-status | PUT | JSON | "status" (MUST BE: {"ON_RIDE","STANDBY"}) | ADMIN, PASSENGER, DRIVER 
 /user/delete-usr | DELETE | JSON | "id" | ADMIN 
 /user/mainpg | GET | void | void | GLOBAL 
 /user/hello | GET | void | void | PASSENGER (for test) 

### AdvertisementController

 URL | Request | Parameter Format | Parameters | Role (ROLE_)
 ------| ------ | ------ | ------ | ------ 
 /adv/create-adv | POST | JSON | "id", "title", "startTime",  "startLocation", "seatAvailable",  "stops", "vehicle" | DRIVER 
 /adv/get-adv-search | POST | JSON | Essential: "stop", "startLocation", "startTimeX", "startTimeY", "sortByPrice"; Optional: "vColor", "vModel" | ADMIN, PASSENGER, DRIVER 
 /adv/get-logged-adv | GET | void | void | DRIVER 
 /adv/get-top-driver | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /adv/get-list-adv | GET | void | void | ADMIN, PASSENGER, DRIVER 
 /adv/update-adv | PUT | JSON | Essential: "id", "stops"; Optional: "title", "startTime",  "startLocation", "seatAvailable", "vehicle" | DRIVER 
 /adv/delete-adv | DELETE | JSON | "id" | DRIVER
 
 Note: "Stop" attribute in update-adv overshadowed all previous records in a list
 
 ### MapperController
 used to add passengers to an advertised trip.
 
 URL | Request | Parameter Format | Parameters | Role (ROLE_)
 ------| ------ | ------ | ------ | ------
 /map/add-map | POST | Path Parameter | "adv_id" | PASSENGER
 /map/admin/delete/{mapper_id} | DELETE | Path Variable | "mapper_id" | PASSENGER
 /map/list-top-passengers | GET | void | void | ADMIN
 
 ### VehicleController
 URL | Request | Parameter Format | Parameters | Role (ROLE_)
 ------| ------ | ------ | ------ | ------
 /vehicle/add-car | POST | JSON | "color", "licencePlate", "maxSeat", "model" | DRIVER
 /vehicle/remove-car | DELETE | JSON | "id" | DRIVER, ADMIN
 /vehicle/get-cars | GET | void | void | DRIVER, ADMIN
 /vehicle/change-cars | PUT | JSON | "id"; Optional: "color", "licencePlate", "maxSeat", "model" | DRIVER
 
 ### StopController
 URL | Request | Parameter Format | Parameters | Role (ROLE_)
 ------| ------ | ------ | ------ | ------
 /stop/add-stop | POST | JSON | "stopName", "price" | DRIVER
 /stop/change-stop | PUT | JSON | "id"; Optional: "stopName", "price" | DRIVER
 /stop/del-stop | DELETE | JSON | "id" | DRIVER
 /stop/get-stop-by-name/{name} | GET | Path Variable | "name" | DRIVER


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
