
# Food Expiry & Donation Coordination System

A **Spring Boot Microservices-based application** designed to help organizations manage food inventory, monitor expiry dates, and coordinate food donations and pickups.

> **Project Status:** Discovery Server, Food Inventory Service, and Expiry Monitoring Service have been implemented, tested, and integrated. Donation Matching Service, Pickup Scheduling Service, and API Gateway are remaining components to be implemented and integrated.

---

# 1. Project Overview

## Problem Statement

Food providers such as supermarkets, restaurants, and cafeterias often have surplus food approaching its expiry date. Without an efficient monitoring and coordination system, large quantities of food may go to waste.

## Proposed Solution

The **Food Expiry & Donation Coordination System** aims to reduce food wastage by providing a centralized platform for:

- Managing food inventory.
- Monitoring food expiry dates.
- Identifying expired and soon-to-expire food.
- Matching available food with donation requirements.
- Coordinating food pickup schedules.

The application follows a **Microservices Architecture**, where each service handles a specific business functionality independently.

---

# 2. System Architecture

The project is divided into independent microservices.

## Current Architecture

```text
                    API CLIENTS
                 (Postman / Swagger)
                          |
                          |
              +-----------v-----------+
              |   Eureka Discovery    |
              |      Server           |
              |      Port: 8761       |
              +-----------+-----------+
                          |
             +------------+------------+
             |                         |
   +---------v----------+    +---------v----------+
   | Food Inventory     |    | Expiry Monitoring  |
   | Service            |<---| Service            |
   | Port: 8081         |REST| Port: 8082         |
   +---------+----------+    +--------------------+
             |
   +---------v----------+
   | PostgreSQL         |
   | food_inventory_db  |
   +--------------------+
```

The complete planned architecture will additionally include:

- Donation Matching Service
- Pickup Scheduling Service
- API Gateway

**Note:** These components will be added to the architecture after their implementation and integration.

---

# 3. Technology Stack

| Technology | Purpose |
|---|---|
| Java 22 | Development environment |
| Java 21 | Project compiler target |
| Spring Boot 4.1.2 | Backend development |
| Spring Cloud 2025.1.4 | Microservices infrastructure |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database operations |
| Hibernate | ORM implementation |
| PostgreSQL | Relational database |
| Netflix Eureka | Service discovery |
| Spring Cloud LoadBalancer | Load balancing |
| Spring RestClient | Inter-service communication |
| Maven | Build and dependency management |
| Swagger / OpenAPI 3.2.0 | API documentation |
| JUnit 5 | Unit testing |
| Mockito | Mocking dependencies during testing |
| Postman | API and integration testing |

---

# 4. Project Folder Structure

The project contains three independently maintained Maven projects.

```text
Food-Donation-System/
│
├── discovery-server/
│   ├── src/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── food-inventory-service/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── .env
│   ├── .env.example
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── expiry-monitoring-service/
│   ├── src/
│   │   ├── main/
│   │   └── test/
│   ├── pom.xml
│   ├── mvnw
│   └── mvnw.cmd
│
├── .gitignore
└── README.md
```

Each microservice is an independent Maven project with its own dependencies, configuration, and source code.

---

# 5. Microservices Implementation

## 5.1 Discovery Server

**Application Name:** `discovery-server`

**Port:** `8761`

The Discovery Server uses Netflix Eureka to maintain a registry of available microservices.

### Responsibilities

- Allows microservices to register themselves.
- Maintains information about registered services.
- Allows services to discover one another.
- Supports service-to-service communication without hardcoding service instance addresses.

### Eureka Dashboard

```text
http://localhost:8761/
```

---

## 5.2 Food Inventory Service

**Application Name:** `food-inventory-service`

**Service ID:** `FOOD-INVENTORY-SERVICE`

**Port:** `8081`

**Database:** `food_inventory_db`

The Food Inventory Service manages food records provided by supermarkets, restaurants, and other food providers.

### Responsibilities

- Add new food items.
- Retrieve all food records.
- Retrieve food by ID.
- Update food information.
- Delete food records.
- Search food by name.
- Filter food by category.
- Retrieve food based on expiry dates.

### Food Item Attributes

| Attribute | Description |
|---|---|
| id | Unique food item identifier |
| foodName | Name of the food |
| category | Food category |
| quantity | Available quantity |
| unit | Quantity unit |
| expiryDate | Expiry date |
| location | Food provider location |
| providerName | Name of the provider |
| providerType | Type of provider |
| createdAt | Record creation timestamp |

### Inventory REST API Endpoints

**Base URL:** `http://localhost:8081`

| HTTP Method | Endpoint | Description |
|---|---|---|
| POST | `/api/food` | Add a food item |
| GET | `/api/food` | Retrieve all food items |
| GET | `/api/food/{id}` | Retrieve food by ID |
| PUT | `/api/food/{id}` | Update food details |
| DELETE | `/api/food/{id}` | Delete a food item |
| GET | `/api/food/category/{category}` | Search food by category |
| GET | `/api/food/search?name={name}` | Search food by name |
| GET | `/api/food/expiring?date={date}` | Retrieve food expiring on or before a date |
| GET | `/api/food/expiring-between?startDate={date}&endDate={date}` | Retrieve food within a date range |

### Swagger Documentation

```text
http://localhost:8081/swagger-ui/index.html
```

### Database

The service uses PostgreSQL with Spring Data JPA and Hibernate.

Database name:

```text
food_inventory_db
```

---

## 5.3 Expiry Monitoring Service

**Application Name:** `expiry-monitoring-service`

**Service ID:** `EXPIRY-MONITORING-SERVICE`

**Port:** `8082`

The Expiry Monitoring Service retrieves food records from the Food Inventory Service and automatically calculates their expiry status.

### Responsibilities

- Retrieve food records from Food Inventory Service.
- Calculate the number of days remaining until expiry.
- Identify expired food items.
- Identify food items expiring soon.
- Identify safe food items.
- Handle communication failures when the Inventory Service is unavailable.

### Expiry Classification Logic

The system uses a warning period of **3 days**.

| Condition | Expiry Status |
|---|---|
| Expiry date is before today | `EXPIRED` |
| Expiry date is today or within the next 3 days | `EXPIRING_SOON` |
| More than 3 days remain | `SAFE` |

### Expiry Monitoring REST API Endpoints

**Base URL:** `http://localhost:8082`

| HTTP Method | Endpoint | Description |
|---|---|---|
| GET | `/api/expiry/status` | Retrieve all food items with expiry status |
| GET | `/api/expiry/expired` | Retrieve expired food items |
| GET | `/api/expiry/expiring-soon` | Retrieve food expiring within 3 days |
| GET | `/api/expiry/safe` | Retrieve safe food items |
| GET | `/api/expiry/inventory` | Retrieve inventory records from Food Inventory Service |

### Swagger Documentation

```text
http://localhost:8082/swagger-ui/index.html
```

### Inter-Service Communication

The Expiry Monitoring Service communicates with the Food Inventory Service using:

- Spring RestClient
- Eureka service discovery
- Spring Cloud LoadBalancer

The Inventory Service is accessed using its registered service name:

```text
http://FOOD-INVENTORY-SERVICE/api/food
```

The Expiry Monitoring Service does not currently maintain a separate database. It retrieves inventory data from the Food Inventory Service and applies its own expiry classification logic.

---

# 6. Prerequisites

Before running the project, ensure the following software is installed:

- JDK 22
- PostgreSQL
- pgAdmin
- Git
- VS Code or another Java IDE

The Maven wrapper is included in each Maven project, so a separate Maven installation is not required to execute the provided wrapper commands.

### PostgreSQL Databases

Create the following databases in PostgreSQL:

1. `food_inventory_db`
2. `food_inventory_test_db`

The first database is used by the Food Inventory Service.

The second database is used for PostgreSQL integration testing.

---

# 7. Environment Variables and Database Configuration

**Important:** Database credentials must not be committed to GitHub.

The Food Inventory Service uses environment variables to access PostgreSQL credentials.

## 7.1 Environment File

Create a `.env` file inside:

```text
food-inventory-service/
```

Add:

```properties
DB_URL=jdbc:postgresql://localhost:5432/food_inventory_db
TEST_DB_URL=jdbc:postgresql://localhost:5432/food_inventory_test_db
DB_USERNAME=postgres
DB_PASSWORD=your_local_postgres_password
```

Replace the password with your own local PostgreSQL password.

## 7.2 Application Properties

The Food Inventory Service uses these environment variables:

```properties
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=org.postgresql.Driver
```

The test configuration uses:

```properties
spring.datasource.url=${TEST_DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```

## 7.3 Loading Environment Variables in Windows

Spring Boot does not automatically load a `.env` file.

Open PowerShell inside the `food-inventory-service` directory and execute:

```powershell
Get-Content .env | ForEach-Object {
    if ($_ -match '^\s*([^#=]+)=(.*)$') {
        [Environment]::SetEnvironmentVariable(
            $matches[1].Trim(),
            $matches[2].Trim(),
            "Process"
        )
    }
}
```

This loads the environment variables into the current PowerShell terminal session.

Run the Maven commands from the same terminal.

## 7.4 Environment Example File

Create a file named `.env.example` inside `food-inventory-service`.

```properties
DB_URL=jdbc:postgresql://localhost:5432/food_inventory_db
TEST_DB_URL=jdbc:postgresql://localhost:5432/food_inventory_test_db
DB_USERNAME=postgres
DB_PASSWORD=replace_with_your_local_password
```

This file can be uploaded to GitHub because it contains placeholder credentials rather than an actual password.

## 7.5 Git Ignore Configuration

The root `.gitignore` should include:

```gitignore
# Environment files
.env
.env.*
**/.env
**/.env.*
!.env.example
!**/.env.example

# Maven generated files
**/target/

# IDE files
.idea/
*.iml
.vscode/

# Operating system files
.DS_Store
Thumbs.db
```

Never upload actual `.env` files or database passwords.

---

# 8. Running the Project

Each microservice is an independent Maven project.

Start each service in a separate VS Code terminal.

## Step 1: Start Discovery Server

Navigate to:

```text
discovery-server/
```

Execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Verify that the Eureka dashboard is accessible:

```text
http://localhost:8761/
```

## Step 2: Start Food Inventory Service

Navigate to:

```text
food-inventory-service/
```

First, load the environment variables using the PowerShell command mentioned in Section 7.

Then execute:

```powershell
.\mvnw.cmd spring-boot:run
```

The service starts on port `8081`.

## Step 3: Start Expiry Monitoring Service

Navigate to:

```text
expiry-monitoring-service/
```

Execute:

```powershell
.\mvnw.cmd spring-boot:run
```

The service starts on port `8082`.

## Step 4: Verify Eureka Registration

Open:

```text
http://localhost:8761/
```

Verify that the following services are registered:

- `FOOD-INVENTORY-SERVICE`
- `EXPIRY-MONITORING-SERVICE`

Both should show as `UP`.

## Step 5: Open Swagger UI

Food Inventory Service:

```text
http://localhost:8081/swagger-ui/index.html
```

Expiry Monitoring Service:

```text
http://localhost:8082/swagger-ui/index.html
```

---

# 9. Testing

The project uses JUnit 5, Mockito, Spring Boot testing support, and Postman.

## 9.1 Food Inventory Service Tests

Navigate to the `food-inventory-service` directory.

Load the environment variables and execute:

```powershell
.\mvnw.cmd clean test
```

**Test Results:**

- Total tests: 32
- Failures: 0
- Errors: 0
- Skipped: 0

**Build Status: SUCCESS**

The tests cover application startup, service logic, repository operations, controller APIs, and PostgreSQL integration.

The PostgreSQL integration tests use the separate `food_inventory_test_db` database with the `create-drop` Hibernate configuration.

Ensure that this test database exists before running the integration test.

## 9.2 Expiry Monitoring Service Tests

Navigate to the `expiry-monitoring-service` directory.

Execute:

```powershell
.\mvnw.cmd clean test
```

**Test Results:**

- Total tests: 17
- Failures: 0
- Errors: 0
- Skipped: 0

**Build Status: SUCCESS**

The tests cover:

- Expiry status calculation.
- Inventory client communication.
- Controller endpoint responses.
- Application startup.

## 9.3 Combined Test Results

| Microservice | Tests Passed |
|---|---:|
| Food Inventory Service | 32 |
| Expiry Monitoring Service | 17 |
| **Total** | **49** |

All 49 automated tests passed during development.

Rerun the test commands after making changes to verify the current code.

## 9.4 Postman Integration Testing

With all three services running, execute the following requests:

| HTTP Method | URL |
|---|---|
| GET | `http://localhost:8082/api/expiry/inventory` |
| GET | `http://localhost:8082/api/expiry/status` |
| GET | `http://localhost:8082/api/expiry/expired` |
| GET | `http://localhost:8082/api/expiry/expiring-soon` |
| GET | `http://localhost:8082/api/expiry/safe` |

The inventory endpoint retrieves records from the Food Inventory Service.

The expiry status endpoint returns additional fields:

- `expiryStatus`
- `daysRemaining`

### Testing Service Unavailability

To verify exception handling:

1. Keep Expiry Monitoring Service running.
2. Stop Food Inventory Service.
3. Send a GET request to `/api/expiry/status`.
4. Verify that the service returns HTTP `503 Service Unavailable`.
5. Restart Food Inventory Service.

The custom exception handler returns a meaningful error response when the Inventory Service is unavailable.

---

# 10. Inter-Service Communication

The Expiry Monitoring Service communicates with the Food Inventory Service through Eureka service discovery.

### Communication Flow

1. Food Inventory Service registers itself with Eureka.
2. Expiry Monitoring Service registers itself with Eureka.
3. Expiry Monitoring Service requests inventory records.
4. Spring Cloud LoadBalancer resolves the registered service name.
5. RestClient sends the REST request to Food Inventory Service.
6. Food Inventory Service retrieves records from PostgreSQL.
7. The inventory records are returned to Expiry Monitoring Service.
8. Expiry Monitoring Service calculates the expiry status.
9. The final response is returned to the API client.

### Service URL

```text
http://FOOD-INVENTORY-SERVICE/api/food
```

This approach avoids hardcoding the Inventory Service's host and port in the client URL.

---

# 11. Remaining Project Work

The following components are part of the planned complete project and remain to be implemented and integrated:

- **Donation Matching Service**
- **Pickup Scheduling Service**
- **API Gateway**
- **Complete system integration and testing**

These components will extend the existing Food Inventory and Expiry Monitoring services.

After implementation, update this README with their actual ports, API endpoints, database configurations, setup instructions, and test results.

---

# 12. Team Collaboration

The project is divided between two team members.

### Member 1

Responsible for:

- Food Inventory Service
- Expiry Monitoring Service
- Inter-service communication between the above services
- Related testing and documentation

### Member 2

Responsible for:

- Donation Matching Service
- Pickup Scheduling Service
- API Gateway
- Integration of their services with the existing components

### Collaboration Guidelines

- Maintain each microservice as an independent Maven project.
- Coordinate changes to service names, ports, and API contracts.
- Each developer must configure their own local `.env` file.
- Never commit real database passwords or API keys.
- Run tests before merging changes.
- Keep the `main` branch in a working state.

---

# 13. Troubleshooting

## Eureka Registration Failure

If a service does not register with Eureka:

- Ensure Discovery Server is running on port `8761`.
- Verify the Eureka URL in `application.properties`.
- Check the service name and port.
- Review the application startup logs.

## PostgreSQL Connection Failure

If a database connection fails:

- Ensure PostgreSQL is running.
- Verify that `food_inventory_db` or `food_inventory_test_db` exists.
- Check the database username and password.
- Verify that `DB_URL`, `TEST_DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` are loaded.
- Run Maven from the same PowerShell terminal in which the environment variables were loaded.

## Expiry Monitoring Returns HTTP 503

If Expiry Monitoring Service returns HTTP 503:

- Ensure Food Inventory Service is running.
- Check that it is registered as `FOOD-INVENTORY-SERVICE` in Eureka.
- Review the Inventory Service logs.
- Review the Expiry Monitoring Service logs.

## Swagger UI Does Not Open

- Ensure the corresponding service has started successfully.
- Verify the correct port.
- Check the Swagger URL.
- Review the application startup logs for errors.

---

# 14. Project Summary

The Food Expiry & Donation Coordination System is designed using a microservices architecture to support food inventory management, expiry monitoring, donation coordination, and pickup scheduling.

The currently implemented components demonstrate:

- Independent Spring Boot microservices.
- Eureka-based service discovery.
- REST-based inter-service communication.
- PostgreSQL database integration.
- Spring Data JPA and Hibernate.
- Automated food expiry classification.
- Custom exception handling.
- Swagger API documentation.
- JUnit and Mockito testing.
- Postman integration testing.

The current implementation consists of the Discovery Server, Food Inventory Service, and Expiry Monitoring Service. The remaining services will extend this foundation to complete the overall system.

---

**Project Name:** Food Expiry & Donation Coordination System

**Current Implementation:** Discovery Server, Food Inventory Service, and Expiry Monitoring Service.
