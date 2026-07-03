## ADDED Requirements

### Requirement: Multi-stage Dockerfile
The generated project SHALL include a `Dockerfile` using multi-stage builds. The first stage SHALL build the application using Maven, and the second stage SHALL run the application using a JRE base image.

#### Scenario: Dockerfile exists
- **WHEN** the generated project structure is inspected
- **THEN** a `Dockerfile` exists at the project root

#### Scenario: Build stage uses Maven
- **WHEN** the Dockerfile is inspected
- **THEN** the first stage uses a Maven image to compile and package the application

#### Scenario: Runtime stage uses JRE
- **WHEN** the Dockerfile is inspected
- **THEN** the final stage uses a JRE image and runs the built JAR with `java -jar`

### Requirement: No docker-compose.yml
The generated project SHALL NOT include a `docker-compose.yml` file.

#### Scenario: No docker-compose file
- **WHEN** the generated project structure is inspected
- **THEN** no `docker-compose.yml` or `docker-compose.yaml` file exists

### Requirement: Dockerfile always included
The Dockerfile SHALL always be generated. No `includeDockerfile` parameter or toggle SHALL exist in the template.

#### Scenario: No Dockerfile toggle parameter
- **WHEN** the template parameters are inspected
- **THEN** no `includeDockerfile` parameter exists
