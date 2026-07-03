## ADDED Requirements

### Requirement: Pinned Java 25 and Spring Boot 4.1.0 versions
The generated `pom.xml` SHALL target Java 25 and Spring Boot 4.1.0 explicitly. No other Java or Spring Boot version options SHALL be parameterized.

#### Scenario: Java version pinned
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `<java.version>25</java.version>` is set and `<maven.compiler.release>25</maven.compiler.release>` is configured

#### Scenario: Spring Boot version pinned
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `<parent>` references `spring-boot-starter-parent` version `4.1.0`

### Requirement: Maven build only
The generated project SHALL include only `pom.xml` for build configuration. No Gradle files (`build.gradle`, `settings.gradle`, `gradlew`) SHALL be generated.

#### Scenario: No Gradle files
- **WHEN** the generated project structure is inspected
- **THEN** no Gradle-related files exist

### Requirement: Correct Spring Boot 4.x web starter
The generated `pom.xml` SHALL include `spring-boot-starter-webmvc` as the web starter. The template SHALL NOT use `spring-boot-starter-web`.

#### Scenario: WebMVC starter present
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `spring-boot-starter-webmvc` is listed as a dependency

#### Scenario: Deprecated web starter absent
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `spring-boot-starter-web` is NOT present

### Requirement: Modularized test starters
The generated `pom.xml` SHALL include `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, and `spring-boot-starter-jackson` (test scope) as separate dependencies in addition to `spring-boot-starter-test`.

#### Scenario: Test starters present
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, and `spring-boot-starter-jackson` with `<scope>test</scope>` are all listed

### Requirement: JaCoCo coverage plugin
The generated `pom.xml` SHALL include the JaCoCo Maven plugin configured with a coverage threshold. The CI pipeline SHALL fail if coverage drops below the configured threshold.

#### Scenario: JaCoCo plugin configured
- **WHEN** the generated `pom.xml` is inspected
- **THEN** the `jacoco-maven-plugin` is present with `prepare-agent` and `report` goals bound to appropriate phases

#### Scenario: Coverage threshold enforced
- **WHEN** `mvn verify` is run on the generated project
- **THEN** the build fails if code coverage drops below the configured threshold (default: 60%)

### Requirement: H2 database with commented PostgreSQL
The generated `application.yml` SHALL configure H2 in-memory database for dev/test. A commented-out PostgreSQL datasource section SHALL be included for production guidance.

#### Scenario: H2 configured for dev
- **WHEN** the generated `application.yml` is inspected
- **THEN** the datasource URL is `jdbc:h2:mem:testdb` with H2 driver and JPA properties configured for H2 dialect

#### Scenario: PostgreSQL section commented
- **WHEN** the generated `application.yml` is inspected
- **THEN** a commented-out PostgreSQL datasource configuration block is present with `jdbc:postgresql://localhost:5432/dbname` URL template

### Requirement: ECS structured logging
The generated `application.yml` SHALL configure `logging.structured.format.console=ecs` for ECS-format structured logging. No external logging dependencies SHALL be included. No `logback-spring.xml` SHALL be generated.

#### Scenario: ECS logging enabled
- **WHEN** the generated `application.yml` is inspected
- **THEN** `logging.structured.format.console` is set to `ecs`

#### Scenario: No external logging deps
- **WHEN** the generated `pom.xml` is inspected
- **THEN** no Logback, Logstash encoder, or external structured logging libraries are present

#### Scenario: No logback-spring.xml
- **WHEN** the generated project structure is inspected
- **THEN** no `logback-spring.xml` or `logback.xml` file exists

### Requirement: Optional OTLP log export
The generated `application.yml` SHALL include a commented-out OTLP log export configuration under `management.logging.export.otlp.enabled` for optional observability integration.

#### Scenario: OTLP config available
- **WHEN** the generated `application.yml` is inspected
- **THEN** a commented-out `management.logging.export.otlp.enabled: true` property is present

### Requirement: Separate JpaAuditingConfig class
The generated project SHALL include a separate `@Configuration` class (e.g., `JpaAuditingConfig`) annotated with `@EnableJpaAuditing`. The `@SpringBootApplication` class SHALL NOT have `@EnableJpaAuditing`.

#### Scenario: JpaAuditingConfig exists
- **WHEN** the generated project is inspected
- **THEN** a `JpaAuditingConfig` class exists with `@Configuration` and `@EnableJpaAuditing` annotations

#### Scenario: No EnableJpaAuditing on Application class
- **WHEN** the generated `Application.java` is inspected
- **THEN** `@EnableJpaAuditing` is NOT present on the `@SpringBootApplication` class

### Requirement: Feature-package code layout
The generated project SHALL organize code by feature package (e.g., `todo/` containing entity, DTO, repository, service, controller). Code SHALL NOT be organized by layer (e.g., separate `controllers/`, `services/`, `repositories/` packages).

#### Scenario: Todo feature package
- **WHEN** the generated project structure is inspected
- **THEN** a `todo/` package contains `TodoEntity.java`, `TodoDto.java`, `TodoRepository.java`, `TodoService.java`, and `TodoController.java`

### Requirement: Explicit getters and setters
All generated Java classes SHALL use explicit getters and setters. No Lombok annotations (`@Getter`, `@Setter`, `@Data`) SHALL be used. No Java records SHALL be used for JPA entities.

#### Scenario: No Lombok dependency
- **WHEN** the generated `pom.xml` is inspected
- **THEN** no Lombok dependency is present

#### Scenario: Explicit getters/setters in entity
- **WHEN** the generated `TodoEntity.java` is inspected
- **THEN** explicit `getId()`, `setId()`, `getTitle()`, `setTitle()` methods are present without Lombok annotations

### Requirement: Application class with correct annotations
The generated `Application.java` SHALL be annotated with `@SpringBootApplication` and serve as the entry point. It SHALL NOT carry `@EnableJpaAuditing` or other infrastructure annotations.

#### Scenario: Application class structure
- **WHEN** the generated `Application.java` is inspected
- **THEN** it has `@SpringBootApplication`, a `main` method with `SpringApplication.run()`, and no `@EnableJpaAuditing`
