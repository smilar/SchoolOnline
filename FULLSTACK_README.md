# Online School - Full Stack Application

A complete full-stack web application for managing an online school system. Built with Spring Boot backend and React frontend.

## 🚀 Quick Start

### Prerequisites
- Java 21 LTS
- Node.js 18+
- Docker & Docker Compose
- Git

### Local Development

1. **Clone the repository:**
```bash
git clone https://github.com/smilar/SchoolOnline.git
cd SchoolOnline
```

2. **Start with Docker Compose:**
```bash
docker-compose up --build
```

3. **Access the application:**
- Frontend: http://localhost:3000
- Backend API: http://localhost:8080/api
- API Documentation: http://localhost:8080/swagger-ui.html

### Manual Setup

**Backend:**
```bash
mvn clean install
mvn spring-boot:run
```

**Frontend (new terminal):**
```bash
cd online-school-frontend
npm install
npm run dev
```

## 📁 Project Structure

```
SchoolOnline/
├── src/                          # Backend source code
│   ├── main/java/com/bootcamp/onlineschool/
│   │   ├── config/              # Configuration classes
│   │   ├── controller/          # REST controllers
│   │   ├── service/             # Business logic
│   │   ├── repository/          # Data access
│   │   ├── entity/              # JPA entities
│   │   ├── dto/                 # Data transfer objects
│   │   └── exception/           # Exception handlers
│   └── test/                    # Backend tests
├── online-school-frontend/      # React frontend
│   ├── src/
│   │   ├── components/          # React components
│   │   ├── hooks/               # Custom hooks
│   │   ├── services/            # API services
│   │   └── pages/               # Page components
│   └── public/                  # Static assets
├── pom.xml                      # Maven configuration
├── Dockerfile                   # Backend Docker image
├── docker-compose.yml           # Multi-container setup
├── nginx.conf                   # Nginx configuration
└── .github/workflows/           # CI/CD pipelines
```

## 🔧 Configuration

### Environment Variables

Create a `.env` file in the root directory:

```env
# Database
DB_NAME=onlineschool
DB_USER=admin
DB_PASSWORD=password

# Spring Profile
SPRING_PROFILE=dev

# Frontend API
REACT_APP_API_URL=http://localhost:8080/api
```

### Application Properties

Backend configuration in `src/main/resources/application.properties`:

```properties
spring.application.name=online-school
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

## 🧪 Testing

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
npm test --prefix online-school-frontend

# Run with coverage
npm test --prefix online-school-frontend -- --coverage

# Run in watch mode
npm test --prefix online-school-frontend -- --watch
```

## 🐳 Docker

### Build Images
```bash
# Build all images
docker-compose build

# Build specific service
docker-compose build backend
docker-compose build frontend
```

### Run Containers
```bash
# Start all services
docker-compose up

# Start in background
docker-compose up -d

# View logs
docker-compose logs -f

# Stop services
docker-compose down
```

## 📊 API Endpoints

### Students
- `GET /api/students` - Get all students
- `GET /api/students/{id}` - Get student by ID
- `POST /api/students` - Create new student
- `PUT /api/students/{id}` - Update student
- `DELETE /api/students/{id}` - Delete student

### Courses
- `GET /api/courses` - Get all courses
- `GET /api/courses/{id}` - Get course by ID
- `POST /api/courses` - Create new course
- `PUT /api/courses/{id}` - Update course
- `DELETE /api/courses/{id}` - Delete course

### Classes
- `GET /api/classes` - Get all classes
- `GET /api/classes/{id}` - Get class by ID
- `POST /api/classes` - Create new class
- `PUT /api/classes/{id}` - Update class
- `DELETE /api/classes/{id}` - Delete class

## 🔐 Security

### Features
- CORS configuration for frontend-backend communication
- Spring Security with method-level authorization
- Password encryption with BCrypt
- Role-based access control (RBAC)
- Input validation and sanitization

### Configuration
Security is configured in `src/main/java/com/bootcamp/onlineschool/config/SecurityConfig.java`

## 📈 Performance

### Backend Optimization
- Database connection pooling
- Query optimization with JPA
- Caching strategies
- Pagination for large datasets

### Frontend Optimization
- Code splitting with React.lazy
- Image optimization
- CSS/JS minification
- Service workers for offline support

## 🚀 Deployment

### Docker Compose Deployment
```bash
docker-compose up -d
```

### AWS Deployment
1. Create EC2 instance
2. Install Docker
3. Clone repository
4. Run `docker-compose up -d`

### Heroku Deployment
```bash
heroku create your-app-name
git push heroku main
```

## 📝 CI/CD Pipeline

GitHub Actions workflows are configured in `.github/workflows/`:

- **backend-tests.yml** - Runs backend tests on push
- **frontend-tests.yml** - Runs frontend tests on push
- **deploy.yml** - Deploys to production on main branch push

## 📚 Documentation

- [Backend Documentation](./LAB_10_FULLSTACK_INTEGRATION.md)
- [Frontend Documentation](./online-school-frontend/README.md)
- [API Documentation](http://localhost:8080/swagger-ui.html) (when running)

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -am 'Add your feature'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## 📄 License

This project is part of the Online School Management System bootcamp curriculum.

## 🆘 Troubleshooting

### Port Already in Use
```bash
# Find process using port 8080
lsof -i :8080

# Find process using port 3000
lsof -i :3000

# Kill process
kill -9 <PID>
```

### Docker Issues
```bash
# Remove all containers
docker-compose down -v

# Rebuild images
docker-compose build --no-cache

# View logs
docker-compose logs -f
```

### Database Connection Issues
- Ensure PostgreSQL is running
- Check database credentials in `.env`
- Verify database exists: `createdb onlineschool`

## 📞 Support

For issues and questions, please open an issue on GitHub.

---

**Last Updated:** November 26, 2025  
**Version:** 1.0.0  
**Status:** Production Ready
