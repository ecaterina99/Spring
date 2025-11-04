# 🛰️ Cosmo Trails - Backend REST API

![Image](https://github.com/user-attachments/assets/635b0eb5-52f2-484c-8c2b-34a330f01ad3)

Enterprise-grade Spring Boot REST API for space mission management, demonstrating modern Java development practices and cloud integration.

## 🎯 Project Overview

RESTful API backend managing space missions, astronauts, destinations, and user budgets. Built to showcase enterprise Java development skills with Spring ecosystem, security best practices, and cloud integration.


## 🛠️ Technology Stack

***Core Framework***

**Spring Boot 3.x** - Main application framework

**Spring Data JPA** - Database operations with Hibernate ORM

**Spring Security** - JWT-based authentication and role-based authorization

**MySQL 8.0** - Relational database



***Cloud & External Services***

**AWS S3** - Scalable image storage for astronaut/destination photos

**AWS EC2** - Production deployment

**Apache PDFBox** - Mission report generation



***Development Tools***

**Maven** - Dependency management and build automation

**Lombok** - Code generation for entities/DTOs

**ModelMapper** - DTO-Entity mapping

**Swagger/Springdoc** - Interactive API documentation

**SLF4J logging** - debugging and monitoring


## 🏗️ Architecture & Design Patterns

**Layered Architecture**

   REST Controllers (@RestController) │  ← API Endpoints

   DTOs (Data Transfer Objects)       │  ← Request/Response models

   Service Layer (@Service)           │  ← Business Logic

   Repository Layer (@Repository)     │  ← Data Access (JPA)

   JPA Entities                       │  ← Domain Models

   MySQL Database                     │  ← Persistence


## Key Design Patterns Implemented

**-DTO Pattern**

Clear separation between database entities and API contracts

ModelMapper for automatic object conversion

Prevents over-fetching and data exposure



**-Repository Pattern**

Spring Data JPA repositories with custom queries

Method name derivation for common operations

@Query annotations for complex queries



**-Dependency Injection**

Constructor-based injection throughout



**-Global Exception Handling**

@ControllerAdvice for centralized error handling

Consistent error response format

Custom exceptions for business logic errors



## 🗄️ Database Design

Entity Relationship Diagram

![Image](https://github.com/user-attachments/assets/62d958ca-8ada-49cb-8240-e026856c2824)


## Core Entities

**-User - System users with authentication**

Relationships: One-to-One with Budget, One-to-Many with Mission

Security: Password encryption with BCrypt

Roles: USER, ADMIN


**-Mission - Space exploration missions**

Complex business logic for success probability calculation

Budget impact management


**-Astronaut - Crew members with specializations**

Many-to-Many with Specializations

Many-to-Many with Missions through MissionParticipant

Image storage via AWS S3


**-Destination - Space locations**

Crew size requirements

Distance metrics

Associated missions


**-MissionParticipant - Junction entity for Mission-Astronaut relationship**

Tracks specific role in mission

Additional metadata for participation


## 🔐 Security Implementation

JWT Authentication Flow

1.User logs in with credentials

2.Server validates and generates JWT token with user details

3.Token returned to client with expiration time

4.Client includes token in Authorization header: Bearer <token>

5.Server validates token on each request via JWT filter

6.User identity extracted from token for authorization


## Security Features

Password encryption with BCrypt (cost factor 12)

JWT tokens with configurable expiration

Method-level security with @PreAuthorize

CORS configuration for frontend integration

SQL injection prevention via JPA


## ☁️ AWS Integration

S3 Image Storage

EC2 deployment


## 📊 Business Logic Examples

Mission Success Probability Calculation

Algorithm considers:

-Crew size vs required crew size

-Specialization matching (each required skill fulfilled)

-Astronaut status 

-Mission difficulty



**Budget Management**

Successful missions increase budget

Failed missions decrease budget

Budget tracking per user

Transaction history maintained


## 📝 PDF Report Generation

Apache PDFBox integration for professional mission reports:

Report Contents

Mission header

Destination details and distance

Crew roster with specializations

Success/failure status

Budget impact



## 📚 API Documentation
Interactive documentation available via Swagger UI when deployed:

Swagger UI: [/swagger-ui.html](http://localhost:8080/api/swagger-ui/index.html)


## 🎓 Skills Demonstrated

-Spring Boot Mastery: Configuration, auto-configuration, profiles

-Spring Data JPA: Repository pattern, custom queries, relationships

-Spring Security: JWT implementation, method security, RBAC

-RESTful Design: HTTP methods, status codes, resource naming

-Cloud Integration: AWS S3, EC2 deployment

-Database Design: Normalization, indexing, complex relationships

-Clean Architecture: Layered design, separation of concerns

-Error Handling: Global exception handling, custom exceptions

-Documentation: Swagger/OpenAPI integration

-Build Tools: Maven multi-module configuration


## 🔗 Related Projects

Frontend Client - Space Exploration web application

API Deployed on AWS EC2 - Production environment


Built with ❤️ using Spring Boot & AWS
