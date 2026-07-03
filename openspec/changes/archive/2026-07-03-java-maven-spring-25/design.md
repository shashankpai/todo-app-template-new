## Context

This template generates greenfield Spring Boot 4.1.0 applications from Backstage Software Templates. The template owns the entire generated project structure (no Spring Initializr bootstrap) to enforce enterprise standards, ensure version consistency, and comply with Spring Boot 4.x breaking changes. It uses Backstage's native `fetch:template` action with `${{ values.* }}` variable substitution — no Cookiecutter, no `.njk` file suffixes.

The target developer is experienced in Java but new to Spring Boot. The generated code is teaching-oriented: explicit getters/setters (no Lombok), feature-package layout, comments explaining WHY, and a sample Todo CRUD module demonstrating controller → service → repository → entity flow.

Locked decisions from `explore-prompt.md` constrain the design space: Java 25, Spring Boot 4.1.0, Maven only, H2 only, no boolean toggles, no docker-compose, no Gradle, no Lombok.

## Goals / Non-Goals

**Goals:**
- Generate a runnable, tested Spring Boot 4.1.0 app from Backstage with zero post-scaffolding configuration
- Enforce enterprise standards (security, CI, Docker, logging, coverage) without toggles
- Produce teaching-oriented code that demonstrates layering and function flow
- Guarantee Spring Boot 4.x compliance (correct starters, annotations, packages, Jackson 3.x)
- TDD by default — every generated behavior has tests written first

**Non-Goals:**
- No database selection (H2 only, PostgreSQL commented out)
- No build tool selection (Maven only)
- No Lombok or records for entities
- No Spring Initializr integration
- No docker-compose.yml
- No Thymeleaf/MVC views (REST API only)
- No external logging frameworks or logback-spring.xml

## Decisions

### 1. Template owns full structure vs Spring Initializr bootstrap

**Decision**: The template generates the entire project structure itself.

**Rationale**: Spring Initializr produces minimal output and can't enforce enterprise standards (security baseline, CI, Docker, structured logging, JaCoCo, sample module). Owning the structure gives full control over version pinning, file layout, and Spring Boot 4.x compliance. Updates to Spring Boot versions are centralized in the template's `pom.xml` skeleton.

**Alternatives considered**: Calling `start.spring.io` via `fetch:plain` then patching files — rejected because it's fragile, requires network access during scaffolding, and can't guarantee consistent output.

### 2. Backstage native `fetch:template` with `${{ values.* }}` vs Cookiecutter

**Decision**: Use Backstage's native `fetch:template` action with `${{ values.* }}` syntax.

**Rationale**: Native `fetch:template` is the Backstage-recommended approach. It supports Nunjucks-style templating inside files with real file extensions (no `.njk` suffix). Cookiecutter requires Python, a separate action (`fetch:cookiecutter`), and adds a dependency layer. Native templating keeps everything in the Backstage ecosystem.

**Alternatives considered**: Cookiecutter — rejected for added complexity, Python dependency, and worse Backstage integration.

### 3. No `.njk` suffix on skeleton files

**Decision**: All skeleton files use their real file extensions (e.g., `pom.xml`, `Application.java`, `application.yml`).

**Rationale**: Backstage's `fetch:template` does not strip `.njk` extensions. Using real extensions ensures generated files have correct names without post-processing steps.

### 4. Explicit getters/setters (no Lombok, no records for entities)

**Decision**: Generated Java classes use explicit getters/setters. No Lombok dependency. No records for JPA entities.

**Rationale**: Target developers are experienced in Java but learning Spring Boot. Explicit getters/setters make the code transparent — you can see exactly what exists without understanding Lombok annotations. For JPA entities, records conflict with JPA's no-arg constructor requirement and proxying.

**Alternatives considered**: Lombok (rejected — hides boilerplate, adds dependency, requires annotation processing); records for non-entity DTOs (possible future enhancement, but kept simple for now).

### 5. Feature-package style

**Decision**: Code is organized by feature package (e.g., `todo/` containing entity, DTO, repository, service, controller) rather than by layer (e.g., `controllers/`, `services/`, `repositories/`).

**Rationale**: Feature packages keep related code together, making it easier to understand how a feature works end-to-end. This aligns with the teaching goal — a developer can look at one package and see the full flow.

### 6. Separate `@EnableJpaAuditing` configuration class

**Decision**: `@EnableJpaAuditing` is on a dedicated `@Configuration` class (`JpaAuditingConfig`), never on `@SpringBootApplication`.

**Rationale**: Placing `@EnableJpaAuditing` on the main application class causes "JPA metamodel must not be empty" errors in `@WebMvcTest` tests because the auditing configuration triggers JPA context loading even when only the web layer is being tested. This is a known Spring Boot 4.x pitfall.

### 7. Spring Boot 4.x starter and test dependencies

**Decision**: Use `spring-boot-starter-webmvc` (not `web`), and add modularized test starters: `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-starter-jackson` (test scope).

**Rationale**: Spring Boot 4.x modularized the test autoconfiguration. The monolithic `spring-boot-starter-test` alone is insufficient. Using the correct starters ensures `@WebMvcTest` and `@DataJpaTest` autoconfigure properly.

### 8. `@MockitoBean` instead of `@MockBean`

**Decision**: Use `@MockitoBean` from `org.springframework.test.context.bean.override.mockito.MockitoBean`.

**Rationale**: `@MockBean` was removed in Spring Boot 4.x. `@MockitoBean` is the replacement with equivalent functionality.

### 9. Jackson 3.x ObjectMapper

**Decision**: Use `tools.jackson.databind.ObjectMapper` for Jackson 3.x. Jackson annotations remain at `com.fasterxml.jackson.annotation.*`.

**Rationale**: Jackson 3.x moved the `ObjectMapper` class to a new package but kept annotations in the original package. Mixing these up causes compilation errors.

### 10. ECS structured logging via Spring Boot native config

**Decision**: Configure `logging.structured.format.console=ecs` in `application.yml`. No external logging dependencies, no `logback-spring.xml`.

**Rationale**: Spring Boot 4.x has native structured logging support. ECS format provides JSON-structured logs out of the box. Adding Logback configs or external libraries is unnecessary and adds maintenance burden. Optional OTLP export is available via `management.logging.export.otlp.enabled`.

### 11. JaCoCo coverage in pom.xml + CI enforcement

**Decision**: JaCoCo Maven plugin is included in `pom.xml` with a coverage threshold. GitHub Actions CI runs `mvn verify` and fails if coverage drops below threshold.

**Rationale**: Coverage enforcement at the build level ensures it can't be bypassed. CI runs on every PR to catch regressions early.

### 12. H2 only with commented PostgreSQL

**Decision**: `application.yml` uses H2 in-memory for dev/test. A commented-out PostgreSQL datasource section is included for production guidance.

**Rationale**: H2 provides zero-config database for development and testing. Including a commented PostgreSQL section guides production deployment without adding runtime dependencies or parameters.

## Risks / Trade-offs

- **[Spring Boot 4.1.0 is very new]** → Versions are pinned in the template; all known 4.x pitfalls are encoded in the generated code. Template updates are centralized.
- **[Template maintenance burden when versions change]** → All version pins are in `pom.xml` skeleton and `template.yaml`. Updating is a single-file change.
- **[No database flexibility]** → H2 only simplifies the template but limits production readiness. Mitigated by commented PostgreSQL section. Teams can uncomment and add the PostgreSQL driver post-scaffolding.
- **[Always-generated includes can't be opted out]** → Security, Docker, CI, and sample module are always present. This ensures consistency but may require deletion for edge cases. Acceptable trade-off for enterprise standardization.
- **[No Lombok may increase boilerplate]** → Explicit getters/setters are verbose but transparent. This is intentional for the teaching-oriented goal.
- **[Backstage `fetch:template` limitations]** → No `.njk` suffix support, limited conditional logic. Mitigated by using `${{ values.* }}` syntax and keeping conditionals simple.
- **[JaCoCo threshold too strict may block PRs]** → Threshold should be set conservatively initially (e.g., 60%) and adjusted as the codebase grows. Documented in README.
