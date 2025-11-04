# 🛰️ Cosmo Trails - Backend REST API

![Image](https://github.com/user-attachments/assets/635b0eb5-52f2-484c-8c2b-34a330f01ad3)

## 🎯 Project Overview

RESTful API backend managing space missions, astronauts, destinations, and user budgets. Built to showcase enterprise Java development skills with Spring ecosystem, security best practices, and cloud integration.
Cosmo Trails transforms space exploration into an engaging strategic experience where users must carefully balance crew composition with mission requirements. Players create accounts to access a universe of space destinations and missions, each with unique challenges and rewards.
The core gameplay mechanic revolves around intelligent crew assembly: users must analyze mission requirements—including crew size and specialized skills—then recruit astronauts whose expertise matches those needs. This risk-reward system creates tension: perfectly matched crews increase success probability and budget rewards, while incomplete or mismatched teams risk mission failure and financial losses.
The dynamic budget system adds consequence to every decision, rewarding successful missions with increased funding while penalizing failures. Upon mission completion, the PDF mission report generator provides players with professional documentation of their achievements, complete with mission statistics, crew performance, and final outcomes.

## 📚 API Documentation
Interactive documentation available via Swagger UI when deployed:

Swagger UI: [/swagger-ui.html](http://localhost:8080/api/swagger-ui/index.html)

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



## 🗄️ Database Design

Entity Relationship Diagram

![Image](https://github.com/user-attachments/assets/62d958ca-8ada-49cb-8240-e026856c2824)


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


## 📝 PDF Report Generation

Apache PDFBox integration for professional mission reports:

Report Contents

Mission header

Destination details and distance

Crew roster with specializations

Success/failure status

Budget impact


## 🔗 Related Projects

Frontend Client - Space Exploration web application

API Deployed on AWS EC2 - Production environment


Built with ❤️ using Spring Boot & AWS
