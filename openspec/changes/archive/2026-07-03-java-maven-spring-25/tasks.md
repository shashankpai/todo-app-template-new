## 1. Backstage Template Scaffolding

- [x] 1.1 Create `template.yaml` with `fetch:template` action, input parameters (`componentName`, `owner`, `groupId`, `artifactId`, `packageName`, `description`), and output links
- [x] 1.2 Create skeleton directory structure with package path folders using `${{ values.packagePath }}` substitution
- [x] 1.3 Create template-level `catalog-info.yaml` registering the template as a Backstage `Template` entity
- [x] 1.4 Verify all skeleton files use real file extensions (no `.njk` suffixes)

## 2. Spring Boot App Baseline — pom.xml

- [x] 2.1 Create `skeleton/pom.xml` with `spring-boot-starter-parent` 4.1.0, Java 25, `${{ values.groupId }}`/`${{ values.artifactId }}` substitution
- [x] 2.2 Add dependencies: `spring-boot-starter-webmvc`, `spring-boot-starter-data-jpa`, `spring-boot-starter-security`, `spring-boot-starter-validation`, H2
- [x] 2.3 Add test dependencies: `spring-boot-starter-test`, `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-starter-jackson` (test scope)
- [x] 2.4 Add JaCoCo Maven plugin with `prepare-agent` and `report` goals, coverage threshold set to 60%
- [x] 2.5 Verify no `spring-boot-starter-web` (deprecated), no Lombok, no Gradle files in skeleton

## 3. Spring Boot App Baseline — Application & Config

- [x] 3.1 Create `skeleton/Application.java` with `@SpringBootApplication` and `main` method (no `@EnableJpaAuditing`)
- [x] 3.2 Create `JpaAuditingConfig.java` as separate `@Configuration` class with `@EnableJpaAuditing`
- [x] 3.3 Create `application.yml` with H2 datasource, JPA properties, `logging.structured.format.console=ecs`, commented PostgreSQL section, commented OTLP export config
- [x] 3.4 Verify no `logback-spring.xml` is generated

## 4. Todo CRUD Module — Entity & Repository (TDD)

- [x] 4.1 Write failing `TodoRepositoryTest` using `@DataJpaTest` (from `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`) — test save and findById
- [x] 4.2 Implement `TodoEntity` with `@Entity`, `@Table`, `@EntityListeners(AuditingEntityListener.class)`, `@CreatedDate`, `@LastModifiedDate`, explicit getters/setters, no-arg constructor
- [x] 4.3 Implement `TodoRepository` interface extending `JpaRepository<TodoEntity, Long>`
- [x] 4.4 Run test — verify Green (test passes)
- [x] 4.5 Refactor: add comments explaining WHY auditing fields exist, verify explicit `jakarta.persistence.EntityListeners` import

## 5. Todo CRUD Module — DTO & Service (TDD)

- [x] 5.1 Write failing `TodoServiceTest` with mocked `TodoRepository` using `@MockitoBean` — test create, findById, findById-not-found, findAll, update, delete
- [x] 5.2 Implement `TodoDto` with `id`, `title`, `completed`, `createdAt`, `updatedAt` fields and explicit getters/setters
- [x] 5.3 Implement `TodoService` with CRUD methods and manual DTO ↔ Entity mapping
- [x] 5.4 Run test — verify Green (all service tests pass)
- [x] 5.5 Refactor: add comments to service methods explaining WHY mapping is manual and WHY exceptions are thrown for not-found

## 6. Todo CRUD Module — Controller (TDD)

- [x] 6.1 Write failing `TodoControllerTest` using `@WebMvcTest` (from `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`) with `@MockitoBean` for service — test POST, GET by ID, GET all, PUT, DELETE
- [x] 6.2 Implement `TodoController` with REST endpoints mapped to `/api/todos` for all CRUD operations
- [x] 6.3 Run test — verify Green (all controller tests pass)
- [x] 6.4 Refactor: add comments to controller methods explaining WHY specific HTTP status codes are returned

## 7. Security Baseline (TDD)

- [x] 7.1 Write failing `SecurityConfigTest` — verify unauthenticated request to protected endpoint is redirected/Unauthorized
- [x] 7.2 Implement `SecurityConfig` with `@EnableWebSecurity`, form login, CSRF enabled
- [x] 7.3 Run test — verify Green (security test passes)
- [x] 7.4 Refactor: add comments explaining WHY form login is the baseline and WHY CSRF is enabled

## 8. Docker Build

- [x] 8.1 Create `skeleton/Dockerfile` with multi-stage build (Maven build stage + JRE runtime stage)
- [x] 8.2 Verify no `docker-compose.yml` is generated

## 9. CI Pipeline

- [x] 9.1 Create `skeleton/.github/workflows/ci.yml` with triggers on `pull_request` and `push` to `main`
- [x] 9.2 Add CI step: setup Java 25 using `actions/setup-java`
- [x] 9.3 Add CI step: run `mvn verify` (with Maven wrapper) to execute tests and JaCoCo coverage check
- [x] 9.4 Verify JaCoCo coverage threshold enforcement causes CI failure when coverage drops below 60%

## 10. Catalog Registration

- [x] 10.1 Create `skeleton/catalog-info.yaml` with `kind: Component`, `metadata.name` from `${{ values.componentName }}`, `spec.owner` from `${{ values.owner }}`, `spec.type: service`

## 11. Project Documentation

- [x] 11.1 Create `skeleton/README.md` with project description, prerequisites (Java 25, Maven), build/run/test commands
- [x] 11.2 Add API documentation section with all Todo CRUD endpoints (method, path, request/response examples, status codes)
- [x] 11.3 Add Mermaid sequence diagram showing Client → Controller → Service → Repository → Database flow
- [x] 11.4 Add template parameters table documenting `componentName`, `owner`, `groupId`, `artifactId`, `packageName`, `description` with defaults

## 12. Final Verification

- [x] 12.1 Scaffold a test project from the template and verify all files are generated with correct variable substitution
- [x] 12.2 Run `mvn clean verify` on the generated project — all tests pass, JaCoCo coverage meets threshold
- [x] 12.3 Verify generated project has no Gradle files, no Lombok, no docker-compose.yml, no logback-spring.xml
- [x] 12.4 Verify all Spring Boot 4.x imports are correct (`@WebMvcTest`, `@DataJpaTest`, `@MockitoBean`, `ObjectMapper` from `tools.jackson.databind`)
