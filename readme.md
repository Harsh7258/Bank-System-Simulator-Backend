# Banking System Simulator
**Microservices Architecture with Spring Boot, Spring Cloud & MongoDB**

## Overview
The **Banking System Simulator** is a fully distributed backend application built using **Microservices Architecture**.
It simulates real-world banking operations such as **account management, transactions, and notifications**, where each service runs independently and communicates through well-defined REST contracts.

---

## Project Objectives
- Design and implement microservices architecture using Spring Boot
- Use Spring Cloud components (Eureka Server, API Gateway, Config Server)
- Implement inter-service communication using Feign Client / RestTemplate
- Apply fault tolerance using Resilience4j Circuit Breaker
- Deploy multiple independent services
- Use MongoDB per microservice
- Implement centralized configuration and logging
- Test end-to-end transaction flows using Postman

---

## Microservices & Ports

| Service | Port |
|-------|------|
| Account Service | 8081 |
| Transaction Service | 8082 |
| Notification Service | 8083 |
| API Gateway | 8080 |
| Eureka Server | 8761 |

---

## Inter-Service Communication
- Transaction → Account Service (update balance)
- Transaction → Notification Service (send notification)
- Communication via Feign Client
- Resilience4j Circuit Breaker for fault tolerance

---

## Architecture Diagram

```
Client
  |
API Gateway (8080)
  |
Transaction Service (8082) - transaction_db (MongoDB)
  |        |
  |        └── Notification Service (8083)
  |
Account Service (8081) - accounts_db (MongoDB)
  |
All services registered with Eureka Server (8761)
```

---

## Core Features
- Independent microservices
- Dynamic service discovery
- Centralized API Gateway routing
- Fault-tolerant communication
- MongoDB database per service
- Real-time email notifications using JavaMailSender
- Centralized logging in terminal
- Postman API documentation - [API URL documentation](https://documenter.getpostman.com/view/31106866/2sBXVbJtrg)

---

## Transaction Flow
1. Client sends request via API Gateway
2. Gateway routes request to Transaction Service
3. Transaction Service updates account balance
4. Notification Service sends confirmation
5. Circuit Breaker handles failures gracefully

---

## API Testing
- Tested using Postman
- Deposit → Transfer → Notification flow verified
- Failure scenarios tested with Circuit Breaker

---

## Tech Stack
- Spring Boot
- Spring Cloud
- Eureka Server
- Spring Cloud Gateway
- MongoDB
- Resilience4j
- Maven
- Postman

---

##  Setup Instructions

### Prerequisites
- Java 17+
- Maven
- MongoDB
- IntelliJ IDEA / Eclipse
- Postman

---

## Running the Application

### Start MongoDB
```
mongod
```

### Start Eureka Server
Configure `application.properties`:
```properties
server.port=8761
```
```
http://localhost:8761
```

### Start Services
Configure `application.properties` if not:
```properties
server.port=as-per-service
spring.mongodb.uri=mongodb://localhost:27017/
spring.mongodb.database=your-db-name

eureka.client.service-url.default-zone=http://localhost:8761/eureka
spring.cloud.config.enabled=false
```
- Account Service → 8081
- Transaction Service → 8082
- Notification Service → 8083
- API Gateway → 8080

---

## Final Outcome
- Each service runs independently
- Eureka dynamically discovers services
- API Gateway routes requests correctly
- Transaction updates account balances
- Notifications triggered successfully
- System remains stable during failures

---

## SDLC Approach Used

**1. Requirement Analysis**  
Identified core banking operations such as account creation, deposits, withdrawals, transfers, and transaction history.

**2. Planning**  
Selected microservices architecture, defined layered structure, and finalized technology stack.

**3. Design**  
Designed REST APIs, MongoDB document models, validation rules, and exception handling flow.

**4. Development**  
Implemented APIs using Spring Boot, business logic in each service layer, and persistence using per MongoDB db.

**5. Testing**  
APIs tested using Postman; unit tests written using JUnit and Mockito.

**6. Deployment**  
Docker file created build file .jar in target.

**7. Maintenance**  
Not implemented (out of scope).
