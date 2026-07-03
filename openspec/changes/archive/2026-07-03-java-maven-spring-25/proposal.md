## Why

Teams adopting Backstage need a reliable, enterprise-grade scaffolding template that generates production-aware Spring Boot 4.1.0 applications with Java 25. Existing Spring Initializr-based scaffolding lacks enforced standards (security baseline, CI, Docker, structured logging, TDD conventions) and doesn't integrate natively with Backstage's `fetch:template` action. This template owns the entire generated structure — ensuring consistency, version control of the scaffold, and compliance with Spring Boot 4.x breaking changes that are easy to get wrong.

## What Changes

- **New Backstage scaffolding template** using native `fetch:template` with `${{ values.* }}` syntax (no Cookiecutter, no `.njk` suffixes on skeleton files)
- **Pinned tech stack**: Java 25, Spring Boot 4.1.0, Maven only, H2 in-memory (dev/test) with commented PostgreSQL prod section
- **Spring Boot 4.x compliance**: `spring-boot-starter-webmvc` (not `web`), modularized test starters, `@MockitoBean` (not `@MockBean`), updated `@WebMvcTest`/`@DataJpaTest` packages, Jackson 3.x `ObjectMapper`, separate `@EnableJpaAuditing` config class
- **Sample Todo CRUD module** as a teaching reference demonstrating controller → service → repository → entity flow with explicit getters/setters (no Lombok)
- **TDD-enforced**: All generated code includes tests written first (Red → Green → Refactor); test layers mirror code layers
- **Spring Security baseline**: Form login always included (no toggle)
- **Multi-stage Dockerfile** always generated (no docker-compose.yml)
- **GitHub Actions CI** with JaCoCo coverage threshold enforcement on every PR
- **Structured logging**: ECS format via Spring Boot native config, no external logging dependencies
- **Backstage catalog-info.yaml** for service registration
- **README.md** with API documentation and sequence diagram
- **Template parameters**: `componentName` (required), `owner` (required), `groupId`, `artifactId`, `packageName`, `description` — all boolean toggles removed per locked decisions from explore-prompt.md

## Non-goals

- **No docker-compose.yml** — only a Dockerfile is generated
- **No Gradle support** — Maven only; no `buildTool` parameter
- **No Lombok** — explicit getters/setters only; no `includeLombok` parameter
- **No database selection parameter** — H2 only; PostgreSQL is a commented-out section in `application.yml`
- **No Spring Initializr bootstrap** — the template owns the full generated structure
- **No build tool toggle** — Maven is the only option
- **No boolean include toggles** — security, Dockerfile, CI, and sample module are always generated
- **No Thymeleaf/MVC views** — backend-first REST API only
- **No external logging frameworks** — Spring Boot native structured logging only

## Capabilities

### New Capabilities

- `backstage-template`: Backstage template definition (`template.yaml`), input parameters, `fetch:template` action, and skeleton directory structure with `${{ values.* }}` variable substitution
- `spring-boot-app`: Generated Spring Boot application baseline — `pom.xml` with pinned dependencies (Java 25, Spring Boot 4.1.0, JaCoCo), `Application.java`, `application.yml` (H2 + ECS structured logging), `JpaAuditingConfig` separate config class
- `todo-crud-module`: Sample Todo CRUD module as teaching reference — entity, DTO, repository, service, controller with full TDD test suite across all layers
- `security-baseline`: Spring Security form login baseline configuration always included in generated app
- `ci-pipeline`: GitHub Actions CI workflow running tests with JaCoCo coverage threshold enforcement on every PR
- `docker-build`: Multi-stage Dockerfile for containerized build and runtime
- `catalog-registration`: Backstage `catalog-info.yaml` for service entity registration
- `project-documentation`: `README.md` with API documentation, setup instructions, and sequence diagram

### Modified Capabilities

(None — this is a greenfield template with no existing specs.)

## Impact

- **New files**: `template.yaml`, `skeleton/` directory tree with all template files, `catalog-info.yaml` template
- **Dependencies**: Spring Boot 4.1.0 starters (`webmvc`, `data-jpa`, `security`, `validation`), test starters (`webmvc-test`, `data-jpa-test`, `jackson` test scope), JaCoCo Maven plugin, H2 database
- **Backstage integration**: Template registered in Backstage `catalog-info.yaml` at the template level; generated apps produce their own `catalog-info.yaml`
- **CI/CD**: GitHub Actions workflow file generated in `.github/workflows/`
- **No breaking changes** to existing systems — this is a new template
