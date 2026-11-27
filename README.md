# Lecture Practice
This lecture introduced how to use git and github commands in a practical scenario what I mostly like is that I was introduced into Directed Acyclic graph which github depends on 

# Online School Backend

A Spring Boot backend service for an online school management system designed to teach bootcamp students.

## Features

- Student and Teacher management (both inherit from User entity)
- Course and Class management
- Student registration system
- RESTful APIs for all entities
- H2 in-memory database for development
- Comprehensive API documentation with Swagger

## Technology Stack

- Java 25 (LTS)
- Spring Boot 3.5.16
- Spring Data JPA
- H2 Database
- Maven
- Swagger/OpenAPI

## Getting Started

### Prerequisites

- Java 25 or higher
- Maven 3.6 or higher

Ensure `JAVA_HOME` points to a Java 25 installation before building or running the application.

### Running the Application

1. Clone the repository
2. Navigate to the project directory
3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

The application will start on `http://localhost:8080`

### H2 Database Console

Access the H2 database console at: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

### API Documentation

Once the application is running, access the Swagger UI at:
`http://localhost:8080/swagger-ui.html`

## Project Structure

```
src/
├── main/
│   ├── java/com/bootcamp/onlineschool/
│   │   ├── OnlineSchoolApplication.java
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   └── exception/
│   └── resources/
│       └── application.properties
└── test/
    └── java/com/bootcamp/onlineschool/
        └── OnlineSchoolApplicationTests.java
```