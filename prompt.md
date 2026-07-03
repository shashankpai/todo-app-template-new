## Project: java-maven-spring-25 Backstage Scaffolding Template

Generates greenfield Spring Boot applications from Backstage Software Templates
using the native `fetch:template` action with Nunjucks-style templating
(Backstage `${{ values.* }}` syntax).

### Tech Stack (Pinned)
- Java 25
- Spring Boot 4.1.0
- Maven (no Gradle)
- H2 in-memory database (dev/test), commented PostgreSQL section for prod
- JaCoCo code coverage plugin (CI-enforced threshold)

### Testing
- JUnit 5, Mockito, @WebMvcTest, @DataJpaTest
- @MockitoBean (NOT @MockBean) — see Spring Boot 4.x pitfalls below
- Test layers mirror code layers (controller test, service test, repository test)
- TDD required: Red → Green → Refactor for every behavior
- CI runs all tests with JaCoCo coverage on every PR

### Logging
- Spring Boot native structured logging (ECS format)
- NO external logging dependencies, NO logback-spring.xml
- logging.structured.format.console=ecs in application.yml
- Optional OTLP log export via management.logging.export.otlp.enabled

### Template Parameters
- componentName (required)
- owner (required, Backstage entity owner)
- groupId (default: com.example)
- artifactId (default: derived from componentName)
- packageName (default: equals groupId)
- description (default: provided)

### Always-Generated (No Toggles)
- Spring Security (form login baseline)
- Multi-stage Dockerfile
- GitHub Actions CI workflow
- Sample Todo CRUD module (teaching reference)
- catalog-info.yaml (Backstage registration)
- README.md with API docs and sequence diagram

### Code Conventions
- Explicit getters/setters (no Lombok, no records for entities)
- Feature-package style (e.g., `todo/` package with all layers)
- Manual DTO ↔ Entity mapping in service layer
- Comments explain WHY, not WHAT
- Add comments to all methods, functions, complex logic, etc.
- TDD required: Red → Green → Refactor for every behavior

### Spring Boot 4.x Migration Pitfalls (Critical)
1. Use `spring-boot-starter-webmvc` (NOT `spring-boot-starter-web`)
2. Test autoconfiguration is modularized — add `spring-boot-starter-webmvc-test`,
    `spring-boot-starter-data-jpa-test`, and `spring-boot-starter-jackson` (test scope)
3. Use `@MockitoBean` (NOT `@MockBean`) from
    `org.springframework.test.context.bean.override.mockito.MockitoBean`
4. `@WebMvcTest` is at `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
5. `@DataJpaTest` is at `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`
6. Jackson 3.x: `ObjectMapper` is `tools.jackson.databind.ObjectMapper`
    (annotations remain at `com.fasterxml.jackson.annotation.*`)
7. `@EnableJpaAuditing` must be on a separate `@Configuration` class,
    NOT on `@SpringBootApplication` (causes JPA metamodel error in @WebMvcTest)
8. All JPA annotation imports must be explicit, including
    `jakarta.persistence.EntityListeners`

### Not Generated
- docker-compose.yml (Dockerfile only)
- Gradle build files (Maven only)
- Lombok configuration (explicit getters/setters only)
- Database selection parameter (H2 only)