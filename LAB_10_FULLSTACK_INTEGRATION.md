# Lab 10: Full Stack Integration - Complete Application

## Overview

Lab 10 is the capstone project that brings together all previous labs into a complete, production-ready full-stack application. This lab focuses on integrating the React frontend with the Spring Boot backend, implementing authentication and authorization, configuring CORS, optimizing performance, ensuring security, and preparing the application for deployment. Students will learn how to build, test, and deploy a complete web application.

## Learning Objectives

By completing this lab, you will understand:

- **Frontend-Backend Integration:** How to connect React frontend with Spring Boot backend
- **CORS Configuration:** How to handle cross-origin requests properly
- **Authentication:** How to implement user authentication and session management
- **Authorization:** How to implement role-based access control
- **Error Handling:** How to handle errors across the full stack
- **Performance Optimization:** How to optimize both frontend and backend performance
- **Security Best Practices:** How to secure the application
- **Testing:** How to test the complete application
- **Deployment:** How to deploy to production
- **Monitoring:** How to monitor application health and performance
- **Documentation:** How to document the complete application

## Technology Stack

### Backend
- **Java 21 LTS:** Latest Java version
- **Spring Boot 3.x:** Web framework
- **Spring Data JPA:** ORM and database access
- **Spring Security:** Authentication and authorization
- **H2/PostgreSQL:** Database
- **Maven:** Build tool
- **JUnit 5:** Testing framework
- **Mockito:** Mocking framework

### Frontend
- **React 18:** UI library
- **React Router 6:** Routing
- **Material-UI:** Component library
- **Axios:** HTTP client
- **Vite:** Build tool
- **Jest:** Testing framework

### DevOps & Deployment
- **Docker:** Containerization
- **Docker Compose:** Multi-container orchestration
- **GitHub Actions:** CI/CD
- **Nginx:** Reverse proxy
- **AWS/Heroku:** Cloud deployment

## Project Structure

```
SchoolOnline/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/bootcamp/onlineschool/
│   │   │   │   ├── config/
│   │   │   │   │   ├── SecurityConfig.java
│   │   │   │   │   ├── CorsConfig.java
│   │   │   │   │   └── WebConfig.java
│   │   │   │   ├── controller/
│   │   │   │   ├── service/
│   │   │   │   ├── repository/
│   │   │   │   ├── entity/
│   │   │   │   ├── dto/
│   │   │   │   ├── exception/
│   │   │   │   └── OnlineSchoolApplication.java
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties
│   │   │       ├── application-prod.properties
│   │   │       └── data.sql
│   │   └── test/
│   ├── pom.xml
│   ├── Dockerfile
│   └── docker-compose.yml
├── frontend/
│   ├── src/
│   ├── public/
│   ├── package.json
│   ├── vite.config.js
│   ├── Dockerfile
│   └── .env.production
├── docker-compose.yml
├── nginx.conf
├── .github/
│   └── workflows/
│       ├── backend-tests.yml
│       ├── frontend-tests.yml
│       └── deploy.yml
└── README.md
```

## Key Features

### 1. Authentication & Authorization

```java
// SecurityConfig.java
@Configuration
@EnableWebSecurity
public class SecurityConfig {
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .cors().and()
            .authorizeRequests()
                .antMatchers("/api/auth/**").permitAll()
                .antMatchers("/api/students/**").authenticated()
                .antMatchers("/api/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
            .and()
            .httpBasic();
        return http.build();
    }
}
```

### 2. CORS Configuration

```java
// CorsConfig.java
@Configuration
public class CorsConfig {
    
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                    .allowedOrigins("http://localhost:3000", "https://yourdomain.com")
                    .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                    .allowedHeaders("*")
                    .allowCredentials(true)
                    .maxAge(3600);
            }
        };
    }
}
```

### 3. Frontend API Integration

```javascript
// src/services/api.js
import axios from 'axios';

const API_BASE_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_BASE_URL,
  withCredentials: true
});

// Add auth token to requests
api.interceptors.request.use(config => {
  const token = localStorage.getItem('authToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Handle auth errors
api.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      localStorage.removeItem('authToken');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

export default api;
```

### 4. Docker Configuration

```dockerfile
# Backend Dockerfile
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY target/online-school-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

```dockerfile
# Frontend Dockerfile
FROM node:18-alpine as build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### 5. Docker Compose

```yaml
version: '3.8'

services:
  backend:
    build: ./
    ports:
      - "8080:8080"
    environment:
      - SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb
      - SPRING_JPA_DATABASE_PLATFORM=org.hibernate.dialect.H2Dialect
    depends_on:
      - db

  frontend:
    build: ./online-school-frontend
    ports:
      - "3000:80"
    environment:
      - REACT_APP_API_URL=http://localhost:8080/api
    depends_on:
      - backend

  db:
    image: postgres:15
    environment:
      - POSTGRES_DB=onlineschool
      - POSTGRES_USER=admin
      - POSTGRES_PASSWORD=password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

volumes:
  postgres_data:
```

## Getting Started

### Prerequisites

- Java 21 LTS
- Node.js 16+
- Docker & Docker Compose
- Git
- Maven

### Local Development Setup

1. **Clone the repository:**
```bash
git clone https://github.com/smilar/SchoolOnline.git
cd SchoolOnline
```

2. **Start backend:**
```bash
mvn clean install
mvn spring-boot:run
```

3. **Start frontend (in new terminal):**
```bash
cd online-school-frontend
npm install
npm run dev
```

4. **Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- API Docs: http://localhost:8080/swagger-ui.html

### Docker Setup

1. **Build and run with Docker Compose:**
```bash
docker-compose up --build
```

2. **Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api

## Testing

### Backend Tests

```bash
# Run all tests
mvn test

# Run specific test class
mvn test -Dtest=StudentControllerTest

# Run with coverage
mvn test jacoco:report
```

### Frontend Tests

```bash
# Run all tests
npm test

# Run with coverage
npm test -- --coverage

# Run in watch mode
npm test -- --watch
```

### Integration Tests

```bash
# Run integration tests
mvn test -Dgroups=integration

# Run end-to-end tests
npm run test:e2e
```

## Deployment

### AWS Deployment

1. **Create EC2 instance:**
```bash
# SSH into instance
ssh -i key.pem ec2-user@your-instance-ip

# Install Docker
sudo yum update -y
sudo yum install docker -y
sudo systemctl start docker
```

2. **Deploy application:**
```bash
# Clone repository
git clone https://github.com/smilar/SchoolOnline.git
cd SchoolOnline

# Build and run
docker-compose up -d
```

### Heroku Deployment

```bash
# Login to Heroku
heroku login

# Create app
heroku create your-app-name

# Deploy
git push heroku main
```

## Performance Optimization

### Backend Optimization

- Use database indexing
- Implement caching (Redis)
- Use pagination for large datasets
- Optimize queries with JPA projections
- Use connection pooling

### Frontend Optimization

- Code splitting with React.lazy
- Image optimization
- Minification and compression
- Lazy loading components
- Service workers for offline support

## Security Best Practices

1. **Authentication:**
   - Use JWT tokens
   - Implement refresh tokens
   - Secure password hashing (bcrypt)

2. **Authorization:**
   - Role-based access control (RBAC)
   - Method-level security
   - Resource-level security

3. **Data Protection:**
   - HTTPS/TLS encryption
   - Input validation
   - SQL injection prevention
   - XSS protection

4. **API Security:**
   - Rate limiting
   - CORS configuration
   - API key management
   - Request validation

## Monitoring & Logging

### Backend Monitoring

```java
// Logging configuration
@Slf4j
@RestController
@RequestMapping("/api/students")
public class StudentController {
    
    @GetMapping
    public ResponseEntity<List<StudentDTO>> getAllStudents() {
        log.info("Fetching all students");
        List<StudentDTO> students = studentService.getAllStudents();
        log.debug("Found {} students", students.size());
        return ResponseEntity.ok(students);
    }
}
```

### Frontend Monitoring

```javascript
// Error tracking
import * as Sentry from "@sentry/react";

Sentry.init({
  dsn: "your-sentry-dsn",
  environment: process.env.NODE_ENV,
  tracesSampleRate: 1.0,
});
```

## CI/CD Pipeline

### GitHub Actions Workflow

```yaml
name: CI/CD Pipeline

on:
  push:
    branches: [ main, develop ]
  pull_request:
    branches: [ main ]

jobs:
  backend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
        with:
          java-version: '21'
      - name: Run tests
        run: mvn test

  frontend-tests:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up Node.js
        uses: actions/setup-node@v2
        with:
          node-version: '18'
      - name: Install dependencies
        run: npm install --prefix online-school-frontend
      - name: Run tests
        run: npm test --prefix online-school-frontend

  deploy:
    needs: [backend-tests, frontend-tests]
    runs-on: ubuntu-latest
    if: github.ref == 'refs/heads/main'
    steps:
      - uses: actions/checkout@v2
      - name: Deploy to production
        run: |
          # Deployment script
          echo "Deploying to production..."
```

## Lab Progression

This is **Lab 10** of the bootcamp curriculum:

- **Lab 1:** Java Fundamentals ✅
- **Lab 2:** JUnit Testing ✅
- **Lab 3:** Spring Boot Basics ✅
- **Lab 4:** Database & SQL ✅
- **Lab 5:** ORM & JPA ✅
- **Lab 6:** Backend API ✅
- **Lab 7:** Maven & Build Tools ✅
- **Lab 8:** Frontend HTML & CSS ✅
- **Lab 9:** ReactJS ✅
- **Lab 10:** Full Stack Integration (current)

## Next Steps

1. Integrate frontend and backend
2. Implement authentication
3. Configure CORS
4. Set up Docker
5. Write integration tests
6. Optimize performance
7. Implement monitoring
8. Set up CI/CD
9. Deploy to production
10. Monitor and maintain

## Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev)
- [Docker Documentation](https://docs.docker.com)
- [GitHub Actions](https://github.com/features/actions)
- [AWS Documentation](https://docs.aws.amazon.com)

---

**Last Updated:** November 26, 2025  
**Lab:** 10 - Full Stack Integration  
**Status:** Ready for Development
