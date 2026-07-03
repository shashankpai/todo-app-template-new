## ADDED Requirements

### Requirement: GitHub Actions CI workflow
The generated project SHALL include a GitHub Actions CI workflow file at `.github/workflows/ci.yml`. The workflow SHALL trigger on pull requests and pushes to the main branch.

#### Scenario: CI workflow file exists
- **WHEN** the generated project structure is inspected
- **THEN** `.github/workflows/ci.yml` exists

#### Scenario: CI triggers on PR
- **WHEN** the CI workflow file is inspected
- **THEN** the `on` trigger includes `pull_request` events

#### Scenario: CI triggers on push to main
- **WHEN** the CI workflow file is inspected
- **THEN** the `on` trigger includes `push` to the `main` branch

### Requirement: CI runs tests with JaCoCo
The CI workflow SHALL run `mvn verify` to execute all tests and generate JaCoCo coverage reports. The workflow SHALL fail if the JaCoCo coverage threshold is not met.

#### Scenario: CI runs mvn verify
- **WHEN** the CI workflow steps are inspected
- **THEN** a step runs `mvn verify` (or `./mvnw verify` using the Maven wrapper)

#### Scenario: CI fails on low coverage
- **WHEN** the CI pipeline runs and code coverage is below the configured threshold
- **THEN** the workflow step fails and the PR is blocked

### Requirement: CI uses Java 25
The CI workflow SHALL set up Java 25 using the appropriate action (e.g., `actions/setup-java` with `java-version: 25`).

#### Scenario: Java 25 setup
- **WHEN** the CI workflow file is inspected
- **THEN** a setup step configures Java 25

### Requirement: CI always included
The CI workflow SHALL always be generated. No `includeCICD` parameter or toggle SHALL exist in the template.

#### Scenario: No CI toggle parameter
- **WHEN** the template parameters are inspected
- **THEN** no `includeCICD` parameter exists
