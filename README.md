# Artech API

REST API for managing an electronics repair service business, built with Spring Boot.

---

## 🚧 Status

In development

---

## ✨ Features (Planned)
- Role-based access control
- Email notifications
- Repair status tracking

## 📌 Overview

Artech API is designed to manage the core operations of a technical service business, including customer management, repair orders, and device tracking.

## 🎯 Purpose
This project was created as part of a backend development portfolio to demonstrate skills in building scalable REST APIs using Spring Boot.

---

## ⚙️ Tech Stack

* Java
* Spring Boot
* Spring Security (JWT)
* Maven
* MySQL (or your DB)

---

## 🧩 Core Features

* Customer management (CRUD)
* Repair order management
* Device tracking
* Authentication and authorization (JWT)
* Validation and error handling

---

## 📂 Project Structure

```
src/main/java/com/artech/
│
├── config         # Data initializer
├── controller     # REST controllers
├── service        # Business logic
├── repository     # Data access layer
├── dto            # Data transfer objects
├── models         # Database entities
├── infra          # Infrastructure
│   ├── errors         # Custom error handler
│   ├── mail           # Mail configuration
│   ├── security       # JWT & auth config
│   └── springdoc      # Swagger API docs
└── validations    # Data validations
```

---

## 🔐 Authentication

This API uses JWT (JSON Web Tokens) for securing endpoints.

---

## 🚀 Getting Started

### Prerequisites

* Java 17+
* Maven
* MySQL (or configured database)

### Installation

```bash
git clone https://github.com/arcan16/artech-service-management.git
cd artech-api
mvn clean install
```

### Run the application

```bash
mvn spring-boot:run
```

---

## 📡 API Endpoints

| Method | Endpoint      | Description              |
| ------ | ------------  | -------------------      |
| POST   | /persons      | Create person register   |
| GET    | /persons      | Get all persons          |
| GET    | /persons/{id} | Get repair orders        |
| PUT    | /persons      | Update person register   |
| DELETE | /persons/{id} | Delete person register   |

---

## 🧪 Testing
Testing is currently in progress. Unit and integration tests will be added using JUnit and Mockito.

---

## 📄 License

This project is licensed under the MIT License.

---

## 👨‍💻 Author

Omar Paredes
https://github.com/arcan16
