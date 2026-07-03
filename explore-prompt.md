I want to design a reusable Backstage scaffolding template for greenfield Spring Boot applications.

Context:
- The template will be used from Backstage Software Templates.
- Scaffolding may use Cookiecutter or Nunjucks templating.
- The goal is not just to generate a runnable app, but also to generate a codebase that helps developers understand:
  - how functions/methods are created,
  - how they are called across layers,
  - where DTOs, services, controllers, repositories, and models fit,
  - whether using getters/setters is helpful for readability and maintainability in the generated code.
- The template should be in one language (Java).
- The template should have standard json logging.
- TDD is REQUIRED (non-negotiable):
  - Follow Red–Green–Refactor.
  - For every behavior in the specs, write tests FIRST (failing), then implement.
  - The task list must explicitly sequence: tests → implementation → refactor.

What I want you to explore:
1. What should the default generated Spring Boot project structure look like for a simple greenfield app template?
2. What dependencies should be included by default in Spring Initializr for a beginner-friendly but production-aware starter?
3. Should the template prefer:
   - REST API only,
   - MVC with Thymeleaf,
   - or a layered backend-first starter with example endpoints?
4. How should the generated sample code demonstrate method/function flow clearly from:
   controller -> service -> repository -> entity/model -> response
5. Should the generated Java classes use explicit getters/setters, Lombok, records, or a mix? Compare these options from a learning and maintainability perspective.
6. What sample domain should be scaffolded by default so that it is simple but realistic? For example:
   - Todo
   - Task tracker
   - Employee CRUD
   - Notes app
7. What files should the Backstage template generate beyond code? For example:
   - README
   - architecture notes
   - sample API usage
   - sequence diagram / flow diagram
   - catalog-info.yaml
   - CI/CD starter files
8. Compare Cookiecutter vs Nunjucks for this use case in Backstage scaffolding:
   - ease of maintenance,
   - templating readability,
   - conditional logic support,
   - developer experience,
   - compatibility with Backstage native scaffolder patterns.
9. What parts should be parameterized in the template? For example:
   - groupId
   - artifactId
   - package name
   - Java version
   - build tool
   - database choice
   - include security
   - include Dockerfile
   - include sample CRUD module
10. What are the tradeoffs between generating “clean minimal code” versus “teaching-oriented code with more explicit structure and comments”?
11. What conventions should be enforced so the scaffold works as a standard enterprise starter from Backstage?
12. What are the risks of generating too much starter code, and how can we avoid a template becoming overly opinionated?
13. Should this Backstage template start from a Spring Initializr-generated project from https://start.spring.io, or should the template generate the full project structure itself? Compare both approaches for:
   - maintainability,
   - consistency,
   - version control of the scaffold,
   - ability to enforce enterprise standards,
   - ease of updating Spring Boot versions and dependencies,
   - suitability for Backstage-native scaffolding workflows.

Please think in explore mode only.
Do not implement code yet.
Help me identify:
- the best scaffold shape,
- the best default sample module,
- the best approach for demonstrating function flow,
- whether explicit getters/setters should be kept for learning value,
- whether Cookiecutter or Nunjucks is the better fit for Backstage-native scaffolding,
- and whether the template should bootstrap from Spring Initializr or own the whole generated structure itself.

Please use comparison tables where useful and give me a recommended direction at the end.
After the exploration is complete and a direction is confirmed, recommend which stable decisions should be saved into openspec/config.yaml as reusable context for future.
Draft:
- a proposed context block,
- any reusable standards or conventions,
- and any items that should remain outside config.yaml because they are task-specific.
Do not implement or write config changes yet unless explicitly instructed.

## Spring Boot 4.x Migration Pitfalls (Critical for Code Generation)

When generating Spring Boot 4.1.0 code, the following breaking changes MUST be
accounted for. These were discovered through actual build/test failures:

1. **`spring-boot-starter-web` deprecated** → use `spring-boot-starter-webmvc`
2. **Test autoconfiguration modularized** → `spring-boot-starter-test` alone
   is insufficient. Must also add `spring-boot-starter-webmvc-test`,
   `spring-boot-starter-data-jpa-test`, and `spring-boot-starter-jackson`
   (test scope) as separate dependencies.
3. **`@MockBean` removed** → use `@MockitoBean` from
   `org.springframework.test.context.bean.override.mockito.MockitoBean`
4. **`@WebMvcTest` package changed** →
   `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`
5. **`@DataJpaTest` package changed** →
   `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`
6. **Jackson 3.x package change** → `ObjectMapper` is now
   `tools.jackson.databind.ObjectMapper` (NOT `com.fasterxml.jackson.databind`).
   Jackson annotations remain at `com.fasterxml.jackson.annotation.*`.
7. **`@EnableJpaAuditing` must NOT be on `@SpringBootApplication`** → it
   causes "JPA metamodel must not be empty" in `@WebMvcTest` tests. Create a
   separate `@Configuration` class (e.g. `JpaAuditingConfig`) with
   `@EnableJpaAuditing`.
8. **All JPA annotation imports must be explicit** → including
   `jakarta.persistence.EntityListeners` when using
   `@EntityListeners(AuditingEntityListener.class)`.

These pitfalls should be encoded in the config.yaml context block so that
future spec-driven code generation automatically complies.

## Exploration Decisions and Directions

The following decisions were made during exploration to constrain the
design space and supersede any conflicting options raised above:

1. **No docker-compose.yml** — The template SHALL NOT generate a
   `docker-compose.yml`. Only a `Dockerfile` is generated (mandatory).

2. **Explicit Java 25 and Spring Boot 4.1.0** — The template SHALL target
   Java 25 and Spring Boot 4.1.0 explicitly. No other Java or Spring Boot
   version options SHALL be parameterized.

3. **Maven only — no Gradle** — The template SHALL generate only `pom.xml`.
   The `buildTool` parameter and Gradle support SHALL be removed. All
   build-related templates and conditional logic for Gradle are dropped.

4. **JaCoCo code coverage** — The generated `pom.xml` SHALL include the
   JaCoCo Maven plugin for code coverage reporting. The CI pipeline SHALL
   run tests with coverage and fail if coverage drops below a configured
   threshold.

5. **Getter/setter vs Lombok — single decision based on developer profile** —
   Since the target developer is experienced in Java but new to Spring Boot,
   the template SHALL use **explicit getters/setters** (not Lombok) to keep
   the generated code transparent and readable for learning Spring Boot
   layering. The `includeLombok` parameter SHALL be removed. No other option
   (e.g. records for entities) SHALL be offered for the entity layer.

6. **No `.njk` suffix on skeleton files** — Backstage's `fetch:template`
   action with Nunjucks does not support `.njk` file extensions in the
   skeleton directory. All template files SHALL use their real file
   extensions (e.g. `pom.xml`, `Application.java`, `application.yml`).
   Nunjucks templating syntax inside the files is still used for variable
   substitution and conditional blocks.

7. **Backstage-native variable syntax** — All Nunjucks-style `{{ var }}`
   expressions SHALL be replaced with Backstage's `${{ values.<var> }}`
   syntax. The mapping is:

   | Old (Nunjucks)           | New (Backstage)                    |
   | ------------------------ | ---------------------------------- |
   | `{{ componentName }}`    | `${{ values.componentName }}`      |
   | `{{ owner }}`            | `${{ values.owner }}`              |
   | `{{ groupId }}`          | `${{ values.groupId }}`            |
   | `{{ packageName }}`      | `${{ values.packageName }}`        |
   | `{{ packagePath }}`      | `${{ values.packagePath }}`        |
   | `{{ database }}`         | `${{ values.database }}`           |

8. **H2 in-memory database — no external database option** — Since the
   template generates an H2 in-memory database for dev/test by default, the
   `database` parameter (postgresql/mysql choice) SHALL be removed. The
   generated `application.yml` SHALL use H2 for the dev profile. A
   commented-out PostgreSQL datasource section SHALL be included for
   production deployment guidance, but no parameterized database selection
   is offered.

9. **Mandatory includes — no boolean toggles** — The following parameters
   SHALL be made mandatory (always included, no toggle):
   - `includeSecurity` — Spring Security with basic form login is always
     generated as a best-practice baseline.
   - `includeDockerfile` — A multi-stage Dockerfile is always generated.
   - `includeCICD` — A GitHub Actions CI workflow is always generated.
   - `includeSampleModule` — The sample Todo CRUD module is always
     generated as a teaching reference.

   These boolean parameters SHALL be removed from the template inputs.
   The only remaining parameters are: `componentName` (required), `owner`
   (required), `groupId` (default: `com.example`), `artifactId` (default:
   derived from componentName), `packageName` (default: equals groupId),
   and `description` (default: provided).