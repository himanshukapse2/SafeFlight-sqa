## This file is generated using github copilot and is intended to provide a comprehensive guide for setting up the development environment for the application. It includes instructions for installing , configuring the database, running the application, and troubleshooting common issues. The guide is designed to help developers quickly get started with SafeFlight and ensure a smooth development experience.
## This is just for dev environment setup and not part of submiision
# SafeFlight Development Environment Setup Guide

## Overview
SafeFlight is a Spring Boot web application for flight booking and management. This guide provides step-by-step instructions to set up the development environment.


### System Requirements
- **Java**: JDK 17 or higher
- **Maven**: 3.6+ (for dependency management)
- **Git**: For version control
- **Web Browser**: Chrome, Firefox, or Edge (latest versions)

### Verify Prerequisites
```bash
# Check Java version
java -version

# Check Maven version
mvn -version

# Check Git version
git version
```

## Project Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd SafeFlight-sqa
```

### 2. Navigate to Backend Directory
```bash
cd backend
```

### 3. Build the Application
```bash
# Clean and compile
mvn clean compile

# Or use the Maven wrapper (recommended)
./mvnw clean compile
```

## Database Configuration

### H2 Database Setup
The application uses H2 database with the following configuration:

```properties
# Database Configuration (from application.properties)
spring.datasource.url=jdbc:h2:file:./data/flightdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=update
```

### Database Features
- **File-based storage**: Data persists in `./data/flightdb.mv.db`
- **H2 Console**: Web-based database browser available at `http://localhost:8082/h2-console`
- **Auto DDL**: Database schema is automatically created/updated
- **In-memory fallback**: If file access fails, falls back to in-memory database

## Application Configuration

### Server Configuration
```properties
# Server Configuration
spring.application.name=backend
server.port=8082
```

### Development Settings
```properties
# Development Optimizations
spring.thymeleaf.cache=false  # Disable template caching for development
spring.jpa.show-sql=false     # Set to true for SQL debugging
```

## Running the Application

### Method 1: Using Maven Wrapper (Recommended)
```bash
# Run the application
./mvnw spring-boot:run

# Or on Windows
mvnw.cmd spring-boot:run
```

### Method 2: Using Maven
```bash
mvn spring-boot:run
```

### Method 3: Using IDE
1. Import the project as a Maven project
2. Run the main class: `com.safeflight.backend.BackendApplication`
3. Or use your IDE's Spring Boot run configuration

## Accessing the Application

### Main Application
- **URL**: http://localhost:8082
- **Features**:
  - Flight search and booking
  - User registration and login
  - Booking management

### H2 Database Console
- **URL**: http://localhost:8082/h2-console
- **JDBC URL**: `jdbc:h2:file:./data/flightdb`
- **Username**: `sa`
- **Password**: (leave blank)


## Development Workflow

### 1. Code Changes
- Edit files in `src/main/java/` for Java code
- Edit templates in `src/main/resources/templates/` for HTML
- Edit styles in `src/main/resources/static/` for CSS/JS

### 2. Hot Reload
The application supports hot reload for:
- Java classes (restart required)
- Thymeleaf templates (automatic reload with `spring.thymeleaf.cache=false`)
- Static resources (automatic reload)

### 3. Database Changes
- Schema changes are handled automatically with `spring.jpa.hibernate.ddl-auto=update`
- For production, consider using proper migrations (Flyway/Liquibase)

## Testing

### Run Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=BackendApplicationTests
```

### Test Database
Tests use a separate H2 in-memory database to avoid affecting development data.

## Troubleshooting

### Common Issues

#### Port Already in Use
```bash
# Find process using port 8082
lsof -i :8082

# Kill the process (replace PID)
kill -9 <PID>

# Or change port in application.properties
server.port=8083
```

#### Database Connection Issues
- Ensure write permissions in the `data/` directory
- Check if H2 console is accessible
- Verify JDBC URL in application.properties

#### Build Failures
```bash
# Clean and rebuild
./mvnw clean install

# Clear Maven cache
./mvnw dependency:purge-local-repository
```

#### Template Caching Issues
- Ensure `spring.thymeleaf.cache=false` in application.properties
- Clear browser cache
- Restart the application

### Logs and Debugging
- Application logs are displayed in the console
- Set `logging.level.com.safeflight=DEBUG` for detailed logging
- Enable SQL logging with `spring.jpa.show-sql=true`

## Project Structure
```
SafeFlight-sqa/
├── backend/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/safeflight/backend/
│   │   │   │   ├── BackendApplication.java
│   │   │   │   ├── config/
│   │   │   │   ├── controller/
│   │   │   │   ├── model/
│   │   │   │   ├── repository/
│   │   │   │   ├── service/
│   │   │   │   └── exception/
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── templates/     # Thymeleaf templates
│   │   │       └── static/        # CSS, JS, images
│   │   └── test/
│   ├── pom.xml
│   └── mvnw/mvnw.cmd
├── data/                          # H2 database files
└── README.md
```

## Environment Variables (Optional)

You can override application.properties with environment variables:

```bash
# Custom port
export SERVER_PORT=8083

# Custom database
export SPRING_DATASOURCE_URL=jdbc:h2:mem:testdb

# Run with custom profile
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

## Contributing

1. Create a feature branch
2. Make changes
3. Test thoroughly
4. Submit a pull request

## Support

For issues or questions:
- Check application logs
- Verify configuration in `application.properties`
- Test with H2 console
- Review Spring Boot documentation

---

**Last Updated**: March 12, 2026
**Application Version**: SafeFlight Backend v1.0
**Spring Boot Version**: 3.x (based on pom.xml)</content>
