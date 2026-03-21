# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

```bash
# Build all modules
mvn clean package

# Build with specific profile (local|test|prd)
mvn clean package -Pprd -DskipTests

# Run tests
mvn test

# Run a single test class
mvn test -Dtest=TodoServiceTest

# Run a single test method
mvn test -Dtest=TodoServiceTest#createTodo_Success

# Run the application locally (requires MySQL and Redis)
mvn spring-boot:run -pl todo-app

# Run with specific profile
mvn spring-boot:run -pl todo-app -Dspring-boot.run.profiles=local
```

## Architecture

This is a multi-module Spring Boot application following DDD (Domain-Driven Design) principles:

```
todo-app (Application Layer)
    └── todo-domain (Domain Layer)
            ├── todo-facade (API DTOs)
            └── todo-infrastructure (Infrastructure Layer)
                        └── todo-common (Utilities & Constants)
```

### Module Responsibilities

- **todo-app**: Entry point, controllers, application services, security config, exception handling
- **todo-domain**: Domain models, repository interfaces, domain services (business logic)
- **todo-facade**: Request/Response DTOs, no business logic
- **todo-infrastructure**: Entity definitions, MyBatis mappers, repository implementations, Redis/JWT utilities
- **todo-common**: Enums, exceptions, constants, user context

### Key Patterns

1. **Repository Pattern**: Interface in `todo-domain/repository`, implementation in `todo-domain/repository/impl` using MyBatis-Plus mappers
2. **DTO Pattern**: Controllers receive Request DTOs from `todo-facade/request`, return Response DTOs from `todo-facade/response`
3. **Domain Service**: Business logic resides in `todo-domain/service/*DomainService.java`
4. **Application Service**: Orchestrates domain services and converts domain models to DTOs in `todo-app/service/*Service.java`

## Database

- MySQL with MyBatis-Plus ORM
- Tables use `t_` prefix: `t_user`, `t_todo`, `t_user_preference`
- Entities in `todo-infrastructure/entity/*Entity.java`
- Schema: `init-sql/01-schema.sql`

## Authentication

- JWT-based authentication via Spring Security
- `JwtAuthenticationFilter` extracts user info from tokens
- `UserContext` (ThreadLocal) stores current user info per request
- Access current user: `UserContext.getCurrentUserId()`
- Auth endpoints (`/auth/**`) are public; all others require authentication

## API Conventions

- All responses wrapped in `ApiResponse<T>`
- Controllers use POST for data operations (e.g., `/todos/getList`, `/todos/create`)
- Swagger UI: `/doc.html`

## Environments

Three Maven profiles: `local` (default), `test`, `prd`
- Config files: `todo-app/src/main/resources/application-{profile}.yml`
- Tests use H2 in-memory database via `application-test.yml`

## Todo ID Format

Todos use string IDs: `{timestamp}_{random}` (e.g., `1709800000000_1234`)