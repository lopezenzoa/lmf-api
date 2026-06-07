# LMF API

A RESTful API built with Spring Boot for managing and serving data with a focus on clean architecture and maintainability.

## 📋 Table of Contents

- [Description](#description)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Examples](#examples)
- [Author](#author)

## 📝 Description

LMF API is a modern RESTful web service developed using Spring Boot and Java 21. It provides a scalable backend solution with data persistence using MySQL and implements best practices in API design. The project leverages Spring Data JPA for database operations and Lombok for reducing boilerplate code.

### Key Features

- **Spring Boot 4.0.6** - Latest Spring Boot framework for rapid development
- **Java 21** - Modern Java version with latest language features
- **Spring Data JPA** - Simplified database access layer
- **MySQL Integration** - Robust relational database support
- **RESTful Architecture** - Clean and intuitive API design
- **Lombok** - Automatic code generation for getters, setters, and constructors

## 📦 Prerequisites

Before you begin, ensure you have the following installed on your system:

- **Java 21 or higher**
- **Maven 3.6+** (or use the included Maven wrapper)
- **MySQL 8.0+**

## 🚀 Installation

### 1. Clone the Repository

```bash
git clone https://github.com/lopezenzoa/lmf-api.git
cd lmf-api
```

### 2. Configure the Database

Create a MySQL database and update the `application.properties` or `application.yml` file with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/lmf_db
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
```

### 3. Build the Project

Using Maven wrapper (recommended):

```bash
./mvnw clean install
```

Or using Maven directly:

```bash
mvn clean install
```

### 4. Run the Application

Using Maven wrapper:

```bash
./mvnw spring-boot:run
```

Or using Maven directly:

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080` by default.

## 💻 Usage

### Starting the API

Once the application is running, you can access it at:

```
http://localhost:8080
```

### Configuration

You can customize the application behavior by modifying the configuration files:

- `src/main/resources/application.properties` - Main configuration file
- `src/main/resources/application-dev.properties` - Development profile
- `src/main/resources/application-prod.properties` - Production profile

To run with a specific profile:

```bash
./mvnw spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=dev"
```

## 🔌 API Endpoints

### Base URL

```
http://localhost:8080/api
```

### Available Endpoints

#### Example Health Check

```
GET /api/health
```

**Description:** Check if the API is running and healthy.

**Response:**
```json
{
  "status": "UP",
  "timestamp": "2026-06-07T14:01:25Z"
}
```

---

**Note:** The specific business endpoints depend on your implementation. Below are common patterns used in REST APIs:

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/{resource}` | Retrieve all resources |
| GET | `/api/{resource}/{id}` | Retrieve a specific resource by ID |
| POST | `/api/{resource}` | Create a new resource |
| PUT | `/api/{resource}/{id}` | Update an existing resource |
| DELETE | `/api/{resource}/{id}` | Delete a resource |

## 📚 Examples

### Example 1: GET All Resources

```bash
curl -X GET http://localhost:8080/api/resources \
  -H "Content-Type: application/json"
```

**Response:**
```json
[
  {
    "id": 1,
    "name": "Resource 1",
    "description": "First resource"
  },
  {
    "id": 2,
    "name": "Resource 2",
    "description": "Second resource"
  }
]
```

### Example 2: GET Single Resource

```bash
curl -X GET http://localhost:8080/api/resources/1 \
  -H "Content-Type: application/json"
```

**Response:**
```json
{
  "id": 1,
  "name": "Resource 1",
  "description": "First resource"
}
```

### Example 3: POST Create New Resource

```bash
curl -X POST http://localhost:8080/api/resources \
  -H "Content-Type: application/json" \
  -d '{
    "name": "New Resource",
    "description": "A brand new resource"
  }'
```

**Response:**
```json
{
  "id": 3,
  "name": "New Resource",
  "description": "A brand new resource"
}
```

### Example 4: PUT Update Resource

```bash
curl -X PUT http://localhost:8080/api/resources/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Updated Resource",
    "description": "Updated description"
  }'
```

**Response:**
```json
{
  "id": 1,
  "name": "Updated Resource",
  "description": "Updated description"
}
```

### Example 5: DELETE Resource

```bash
curl -X DELETE http://localhost:8080/api/resources/1 \
  -H "Content-Type: application/json"
```

**Response:** HTTP 204 No Content

## 👨‍💻 Author

**Lopez Enzo A**

- GitHub: [@lopezenzoa](https://github.com/lopezenzoa)
- Repository: [lmf-api](https://github.com/lopezenzoa/lmf-api)

## 📄 License

This project is open source and available under the MIT License.

---

**Last Updated:** June 7, 2026
