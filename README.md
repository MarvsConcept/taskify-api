
# Taskify – Personal Task Manager API

Taskify is a clean and modern RESTful backend built with Spring Boot 3, featuring JWT authentication, role-based access control, and user-owned tasks with validation and business rules.
It demonstrates professional backend patterns such as DTO mapping, global exception handling, layered architecture, and stateless security.

## Features
## Authentication & Security

- User registration & login

- JWT-based stateless authentication (Bearer token)

- Implements AuthenticationManager, UserDetailsService, and custom JWT filter

- Role-based access (USER, ADMIN)

- ADMIN can access all tasks

- USER can only access their own tasks

## Task Management

#### CRUD operations for tasks

#### Task properties:

- Title

- Description

- Status (TODO, IN_PROGRESS, DONE)

- Due date

- Tags (Set<String>)

- createdAt, updatedAt, completedAt

#### Business rules:

- dueDate cannot be in the past

- Automatically sets completedAt when status becomes DONE
- Ownership enforced in all operations

## User Management

- /api/v1/users/me → returns profile of logged-in user

- /api/v1/admin/users → admin-only endpoint listing all users

## Error Handling
Global exception handler returns consistent JSON for:

- Validation errors

- Unauthorized access

- Resource not found

- Bad input

- Server errors

## API Documentation

- Swagger UI (/swagger-ui/index.html)

- OpenAPI 3 schema

- Grouped API docs (auth & tasks)

# Architecture Overview

```sh
┌─────────────────────────────────────────────────────────┐
│                       Taskify API                       │
└─────────────────────────────────────────────────────────┘

                    Layered Architecture
┌─────────────────────────────────────────────────────────┐
│                     Presentation Layer                   │
│   - Controllers (Auth, Tasks, Users)                     │
│   - Request/Response DTOs                                │
│   - Swagger UI (API Docs)                                │
└─────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────┐
│                     Service Layer                        │
│   - Business logic                                        │
│   - Ownership checks                                      │
│   - Status transitions (TODO → DONE → completedAt)        │
│   - Validation enforcement                                │
│   - Uses CurrentUserService                               │
└─────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────┐
│                     Security Layer                       │
│   - JWT generation/validation                             │
│   - AuthenticationManager                                 │
│   - UserDetailsService & CustomUserDetails                │
│   - JwtAuthenticationFilter                               │
│   - Role-based authorization (USER vs ADMIN)             │
└─────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────┐
│                     Persistence Layer                    │
│   - Entities (User, Task)                                │
│   - JPA Repositories                                     │
│   - @ElementCollection for Task.tags                     │
│   - UUID primary keys                                    │
└─────────────────────────────────────────────────────────┘
            │
            ▼
┌─────────────────────────────────────────────────────────┐
│                       Database                           │
│   - PostgreSQL / MySQL                                   │
│   - Tables: users, tasks, task_tags                     │
└─────────────────────────────────────────────────────────┘
```
# API Endpoints
### Auth Endpoints

| Method | Endpoint | Description
|---|:---:|---:|
POST | /api/v1/auth/register | Register a new user
POST | /api/v1/auth/login | Login and retrieve JWT

### Task Endpoints
Method | Endpoint | Description
|---|:---:|---:|
POST | /api/v1/tasks | Create a new task
GET	| /api/v1/tasks	| List tasks (optional status filter)
GET	| /api/v1/tasks/{id} | Retrieve a task by ID
PUT	| /api/v1/tasks/{id} | Update a task
DELETE | /api/v1/tasks/{id} | Delete a task

### User Endpoints
Method | Endpoint | Description
|---|:---:|---:|
GET | /api/v1/users/me | Get current authenticated user
GET	| /api/v1/admin/users	| Admin-only: list all users



## Sample API Usage
1. Register

```sh
POST /api/v1/auth/register
{
  "username": "marv",
  "email": "marv@example.com",
  "password": "Password123"
}

```

2. Login

Returns:

```sh
{
  "token": "jwt-token-here",
  "id": "uuid",
  "username": "marv",
  "email": "marv@example.com",
  "role": "USER"
}

```

Send this token on every request:


```sh
Authorization: Bearer <token>
```
3. Create a Task

   
```sh
POST /api/v1/tasks
{
  "title": "Finish backend",
  "description": "Complete Taskify features",
  "status": "IN_PROGRESS",
  "tags": ["spring", "java"],
  "dueDate": "2025-01-25T12:00:00"
}

```

## Tech Stack

- Java 17+

- Spring Boot 3

- Spring Security + JWT

- Spring Data JPA

- MapStruct

- PostgreSQL / MySQL

- Lombok
  
- Docker

Swagger (springdoc-openapi)


## Project Structure


```sh
src/main/java/com/taskify
│
├── auth
│   ├── controller
│   ├── dto
│   ├── service
│   ├── security
│
├── domain
│   ├── entities
│   ├── dtos
│   ├── repositories
│   ├── mappers
│
├── tasks
│   ├── controller
│   ├── service
│
├── users
│   ├── controller
│   ├── service
│
└── common
    ├── exceptions
    ├── ApiErrorResponse

```

## Running the Project
1. Clone the repo

```sh
git clone https://github.com/YOUR_USERNAME/taskify-api.git
cd taskify-api
```

3. Update your DB config in application.yml
   
```sh
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/taskify
    username: postgres
    password: password
```

5. Run the app
   
```sh
mvn spring-boot:run
```

7. Open Swagger UI
   
```sh
http://localhost:8080/swagger-ui/index.html
```

## Author
### Marvade
- Learning Spring Boot by building real-world backend systems.
