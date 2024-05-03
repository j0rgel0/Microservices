# Microservices Architecture README

This document provides an overview of the microservices architecture used, including service discovery, gateway management, authentication, and the Docker containers that support the infrastructure.

## Services Description

### DiscoverService

**Purpose**: Manages service registration and discovery using Netflix Eureka.

**Port**: 8761

**Key Dependencies**:
- `spring-cloud-starter-netflix-eureka-server`: Eureka server starter.
- `spring-boot-starter-actuator`: For monitoring and management capabilities.
- `micrometer-registry-prometheus`: For Prometheus integration.

**Configuration Highlights**:
- Eureka server does not register itself as a client.
- Customized instance ID for unique identification.
- Self-preservation mode is disabled to allow for quicker node eviction.

### GatewayService

**Purpose**: Routes and filters requests to various microservices.

**Port**: 8762

**Key Dependencies**:
- `spring-cloud-starter-netflix-eureka-client`: Eureka client for service discovery.
- `spring-cloud-starter-gateway`: Gateway starter for routing.
- `spring-cloud-starter-loadbalancer`: For client-side load balancing.

**Configuration Highlights**:
- Integrated with Eureka for service discovery.
- Custom route definitions and filters, including a retry mechanism for failed requests.

### AuthenticationService

**Purpose**: Handles authentication and token generation.

**Random Port** (configured to 0 for dynamic assignment).

**Key Dependencies**:
- `spring-boot-starter-security`: For security features.
- `java-jwt`: For JWT handling.
- `spring-boot-starter-data-jpa`: For JPA integration.

**Configuration Highlights**:
- Secures endpoints using Spring Security.
- Utilizes JWT for stateless authentication.
- Connects to PostgreSQL database.

### UserService

**Purpose**: Manages user data and provides endpoints for user operations such as create, read, update, and delete (CRUD).

**Random Port** (configured to 0 for dynamic assignment).

**Key Dependencies**:
- `spring-boot-starter-web`: For building web applications.
- `spring-boot-starter-data-jpa`: For database integration using JPA.
- `spring-boot-starter-security`: For securing the application.

**Configuration Highlights**:
- Utilizes Spring Data JPA for database interactions.
- Secured endpoints with role-based access control.
- Integrated with Eureka for service discovery.
- Provides comprehensive API endpoints for managing user profiles and data, with support for pagination and HATEOAS.

**Database**: Connects to a PostgreSQL database for storage.

## Docker Containers

### microservice-database

**Purpose**: Provides a PostgreSQL database for microservices.

**Port**: 5432

**Build Context**:
- Dockerfile located in `./docker/database`.

**Environment Variables**:
- POSTGRES_DB
- POSTGRES_USER
- POSTGRES_PASSWORD

### prometheus

**Purpose**: Monitoring system and time series database.

**Port**: 9090

**Image**: `prom/prometheus:v2.30.0`

**Configuration**:
- Prometheus configuration file is located at `/etc/prometheus/prometheus.yml`.

### grafana

**Purpose**: Analytical and visualization tool.

**Port**: 3000

**Image**: `grafana/grafana:8.1.2`

**Dependencies**:
- Depends on the `prometheus` service.

**Configuration**:
- Grafana provisioning configurations are located in `/etc/grafana/provisioning/`.
- Uses environment variables from `./docker/grafana/config.monitoring`.

## Running the Containers

Use Docker Compose to manage and run the multi-container setup. The configuration file `docker-compose.yml` defines how these services are built and interact. Ensure Docker and Docker Compose are installed on your machine and use the following command in the directory containing your `docker-compose.yml`:

```bash
docker-compose up
