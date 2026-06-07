# LMF API

A RESTful API built with Spring Boot for managing and serving data with a focus on clean architecture and maintainability. This project provides a comprehensive payment system solution for local football leagues.

## 📋 Table of Contents

- [Description](#description)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [API Endpoints](#api-endpoints)
- [Examples](#examples)
- [Author](#author)

## 📝 Description

LMF API is a modern RESTful web service developed using Spring Boot and Java 21. It provides a scalable backend solution specifically designed for managing payments in local football leagues. The system handles court management, match scheduling, and payment processing with data persistence using MySQL and implements best practices in API design.

### Key Features

- **Spring Boot 4.0.6** - Latest Spring Boot framework for rapid development
- **Java 21** - Modern Java version with latest language features
- **Spring Data JPA** - Simplified database access layer
- **MySQL Integration** - Robust relational database support
- **RESTful Architecture** - Clean and intuitive API design
- **Lombok** - Automatic code generation for getters, setters, and constructors
- **Payment System** - Complete payment processing for football league operations
- **Court Management** - Manage sports facilities and their details
- **Match Scheduling** - Organize and track matches within divisions

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

### Court Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/court/all` | Retrieve all courts |
| GET | `/api/court/{name}` | Retrieve a specific court by name |
| POST | `/api/court/add` | Create a new court |
| PUT | `/api/court/update/{name}` | Update an existing court by name |

### Match Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/match/{id}` | Retrieve a specific match by ID |
| GET | `/api/match/courtName/{courtName}` | Retrieve matches by court name |
| GET | `/api/match/date/{date}` | Retrieve matches by date (format: YYYY-MM-DD HH:mm) |
| POST | `/api/match/add` | Create a new match |
| PUT | `/api/match/update/{id}` | Update an existing match by ID |

### Payment Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/payment/{courtName}` | Retrieve payments by court name |
| POST | `/api/payment/add` | Create a new payment |

## 📚 Examples

### Example 1: Create a Court

```bash
curl -X POST http://localhost:8080/api/court/add \
  -H "Content-Type: application/json" \
  -d '{
    "name": "José Alberto Valle",
    "address": "Av. Polonia 1335",
    "ownerTeamName": "Club Atlético Kimberley"
  }'
```

**Response:**
```json
{
  "name": "José Alberto Valle",
  "address": "Av. Polonia 1335",
  "ownerTeamName": "Club Atlético Kimberley"
}
```

### Example 2: Get All Courts

```bash
curl -X GET http://localhost:8080/api/court/all \
  -H "Content-Type: application/json"
```

**Response:**
```json
[
  {
    "name": "José Alberto Valle",
    "address": "Av. Polonia 1335",
    "ownerTeamName": "Club Atlético Kimberley"
  },
  {
    "name": "Predio Kraglievich",
    "address": "Calle Principal 456",
    "ownerTeamName": "Club Atlético"
  }
]
```

### Example 3: Create a Match

```bash
curl -X POST http://localhost:8080/api/match/add \
  -H "Content-Type: application/json" \
  -d '{
    "date": "2026-02-02 15:00",
    "divisionName": "1er División",
    "homeTeamName": "Club Atlético Kimberley",
    "visitTeamName": "Club Atlético"
  }'
```

**Response:**
```json
{
  "id": 1,
  "date": "2026-02-02 15:00",
  "divisionName": "1er División",
  "homeTeamName": "Club Atlético Kimberley",
  "visitTeamName": "Club Atlético"
}
```

### Example 4: Get Matches by Court

```bash
curl -X GET http://localhost:8080/api/match/courtName/jose%20alberto%20valle \
  -H "Content-Type: application/json"
```

**Response:**
```json
[
  {
    "id": 1,
    "date": "2026-02-02 15:00",
    "divisionName": "1er División",
    "homeTeamName": "Club Atlético Kimberley",
    "visitTeamName": "Club Atlético"
  }
]
```

### Example 5: Create a Payment

```bash
curl -X POST http://localhost:8080/api/payment/add \
  -H "Content-Type: application/json" \
  -d '{
    "amount": 1000,
    "matchId": 1
  }'
```

**Response:**
```json
{
  "id": 1,
  "amount": 1000,
  "matchId": 1,
  "status": "completed"
}
```

## 👨‍💻 Author

**Lopez Enzo A**

- GitHub: [@lopezenzoa](https://github.com/lopezenzoa)
- Repository: [lmf-api](https://github.com/lopezenzoa/lmf-api)

## 📄 License

This project is open source and available under the MIT License.

---

**Last Updated:** June 7, 2026
