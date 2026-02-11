# app-authenticator

Authentication gateway microservice for the Firefly OpenCore Banking Platform. This service acts as a reactive API gateway that proxies authentication-related requests to the downstream Security Center domain service, providing a unified entry point for login, logout, and user management operations.

## Overview

The `app-authenticator` service is a Spring Cloud Gateway application built on Spring WebFlux. Rather than implementing authentication logic directly, it leverages the FireflyFramework `@DomainPassthrough` mechanism to declaratively route incoming API requests to the appropriate backend domain service (`security-center`). This pattern keeps the gateway layer thin and focused on routing, security filtering, and cross-cutting concerns while the actual business logic resides in dedicated domain services.

### Key Features

- **Reactive API Gateway**: Built on Spring Cloud Gateway and Spring WebFlux for non-blocking, high-throughput request handling
- **Declarative Route Passthrough**: Uses FireflyFramework's `@DomainPassthrough` annotations to route requests to backend domain services without boilerplate code
- **Authentication Routing**: Proxies login, logout, and user management endpoints to the Security Center service
- **WebFlux Security**: Reactive security filter chain configuration with Spring Security for WebFlux
- **Centralized Configuration**: Integrates with Spring Cloud Config Server for externalized configuration management
- **Observability**: Built-in support for Prometheus metrics via Micrometer and distributed tracing via OpenTelemetry (OTLP exporter)
- **API Documentation**: OpenAPI 3.0 documentation via SpringDoc with WebFlux UI support
- **Health Monitoring**: Spring Boot Actuator endpoints for health checks and operational monitoring

## Architecture

This project follows a modular multi-module Maven architecture with clear separation of concerns.

### Modules

- **app-authenticator-core**: Business logic and service layer. Depends on FireflyFramework utilities (`fireflyframework-utils`), Spring WebFlux, MapStruct for object mapping, and Lombok for boilerplate reduction. Includes Reactor Test for reactive testing support.
- **app-authenticator-interfaces**: DTOs, API contracts, and interface definitions. Depends on the core module, includes Swagger/OpenAPI annotations, Jackson serialization support (including JSR-310 date/time types), and Spring Boot Validation.
- **app-authenticator-web**: REST controllers, gateway configuration, security setup, and the Spring Boot application entry point. Depends on the interfaces module and `fireflyframework-application` for shared application infrastructure. Contains the Spring Cloud Gateway routing configuration and all web-layer concerns.

### Technology Stack

- **Java 25**: Latest Java features including virtual threads
- **Spring Boot**: Microservice framework with reactive support
- **Spring WebFlux**: Reactive web framework for non-blocking I/O
- **Spring Cloud Gateway**: API gateway built on Spring WebFlux for reactive routing and filtering
- **Spring Cloud Config**: Externalized configuration management via a central Config Server
- **Spring Security (WebFlux)**: Reactive security filter chain
- **FireflyFramework**: Parent POM (`fireflyframework-parent`), BOM (`fireflyframework-bom` v26.01.01), application infrastructure (`fireflyframework-application`), and utilities (`fireflyframework-utils`) from the [FireflyFramework](https://github.com/fireflyframework/) ecosystem
- **SpringDoc OpenAPI**: API documentation with Swagger UI for WebFlux
- **Micrometer + Prometheus**: Metrics collection and exposition
- **OpenTelemetry (OTLP)**: Distributed tracing export
- **MapStruct**: Compile-time object mapping
- **Lombok**: Boilerplate code reduction
- **Jackson**: JSON serialization/deserialization with JSR-310 support
- **Reactor Test**: Testing utilities for reactive streams

### Project Structure

```
app-authenticator/
├── pom.xml                              # Parent POM (multi-module)
├── app-authenticator-core/
│   ├── pom.xml                          # Core module POM
│   └── src/main/java/                   # Business logic (services, domain)
├── app-authenticator-interfaces/
│   ├── pom.xml                          # Interfaces module POM
│   └── src/main/java/                   # DTOs, API contracts
└── app-authenticator-web/
    ├── pom.xml                          # Web module POM
    └── src/main/
        ├── java/com/firefly/app/orchestrator/web/
        │   ├── AppAuthenticatorApplication.java      # Spring Boot entry point
        │   ├── GatewaySecurityConfig.java            # WebFlux security config
        │   └── controllers/
        │       └── AuthenticatorController.java      # Gateway route definitions
        └── resources/
            └── application.yml                       # Application configuration
```

## Setup and Installation

### Prerequisites

- Java 25 or higher
- Maven 3.8 or higher
- Access to the FireflyFramework Maven repository (for `fireflyframework-parent`, `fireflyframework-bom`, `fireflyframework-application`, and `fireflyframework-utils` artifacts)

### Environment Variables

| Variable            | Description                              | Default                                                  |
|---------------------|------------------------------------------|----------------------------------------------------------|
| `CONFIG_SERVER_URL` | URL of the Spring Cloud Config Server    | `https://app-firefly-config-server.dev.soon.es/`         |

Additional configuration properties are resolved at runtime from the Config Server, including:

- `endpoints.domain.security-center` -- Base URL of the Security Center domain service (used by `@DomainPassthrough` route targets)

### Building the Application

```bash
mvn clean install
```

### Running the Application

```bash
mvn spring-boot:run -pl app-authenticator-web
```

The application will start as a reactive gateway service named `app-authenticator` (as defined in `application.yml`).

## Main API Endpoints

The following endpoints are defined as gateway passthroughs to the Security Center domain service:

| Method  | Path                   | Description                                | Target Service   |
|---------|------------------------|--------------------------------------------|------------------|
| `*`     | `/api/v1/auth/login`   | User authentication (login)                | Security Center  |
| `*`     | `/api/v1/auth/logout`  | User session termination (logout)          | Security Center  |
| `*`     | `/api/v1/users`        | User management operations                 | Security Center  |

All routes are defined declaratively using the `@DomainPassthroughs` / `@DomainPassthrough` annotations in `AuthenticatorController.java`. The actual HTTP methods supported depend on the downstream Security Center service implementation.

### API Documentation

When the application is running, the OpenAPI documentation is available at:

- **Swagger UI**: `http://localhost:{port}/webjars/swagger-ui/index.html`
- **OpenAPI spec**: `http://localhost:{port}/v3/api-docs`

## Security Configuration

The gateway security is configured in `GatewaySecurityConfig.java` using Spring Security for WebFlux:

- **CSRF**: Disabled (typical for stateless API gateways)
- **Authorization**: All exchanges are currently permitted (`permitAll()`) at the gateway level; authentication and authorization are expected to be enforced by the downstream Security Center service

## Development Guidelines

### Testing

- **Unit Tests**: Use JUnit 5 with Spring Boot Test starter
- **Reactive Tests**: Use `reactor-test` for testing reactive streams and WebFlux components
- Run all tests: `mvn test`

### Code Style

- Use Lombok annotations to reduce boilerplate (getters, setters, builders, etc.)
- Use MapStruct for type-safe object mapping between DTOs and domain objects
- Follow reactive programming patterns with Project Reactor (`Mono`, `Flux`)

### Branching Strategy

- `main`: Production-ready code
- `develop`: Integration branch for features
- `feature/*`: Feature branches

## Monitoring

### Actuator Endpoints

Spring Boot Actuator is included and provides operational endpoints:

- `/actuator/health` -- Application health status
- `/actuator/info` -- Build and application information
- `/actuator/prometheus` -- Prometheus-formatted metrics

### Metrics

Micrometer with Prometheus registry is configured for metrics collection. Metrics are exposed via the `/actuator/prometheus` endpoint for scraping by Prometheus.

### Distributed Tracing

OpenTelemetry with the OTLP exporter is configured for distributed tracing across microservices.

## Related Projects

- [FireflyFramework](https://github.com/fireflyframework/) -- Parent framework providing shared infrastructure, BOM, utilities, and application base
- [app-authenticator (this repo)](https://github.com/firefly-oss/app-authenticator) -- Source repository

## License

This project is licensed under the Apache License 2.0. See the [LICENSE](LICENSE) file for details.
