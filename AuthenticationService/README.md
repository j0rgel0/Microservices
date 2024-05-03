# Authentication Service

## Description
This AuthenticationService is designed as a microservice using Spring Boot and Spring Cloud technologies. It provides JWT-based authentication and integrates with the Netflix Eureka discovery service.

## System Requirements
- Java 17
- Spring Boot 3.2.5
- Spring Cloud 2023.0.1

## Configuration
This microservice is configured with Maven and the following main dependencies:
- Spring Boot Starters (Web, Data JPA, Security, Actuator)
- Spring Cloud Netflix Eureka Client
- Auth0 Java JWT for JWT handling
- PostgreSQL JDBC Driver for database connectivity

## Features
- JWT authentication
- User management and registration
- Service registration and discovery via Eureka
- Exception handling and custom error responses
- Actuator endpoints for health monitoring

## Quick Start
To start the microservice, ensure that you have Gradle and JDK 17 installed.
## Contributing

Contributions are welcome! If you find any issues or have suggestions for improvements, please feel free to create a new issue or submit a pull request.