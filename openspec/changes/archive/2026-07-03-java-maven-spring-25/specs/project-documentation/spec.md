## ADDED Requirements

### Requirement: README with API documentation
The generated project SHALL include a `README.md` at the project root with API documentation for all generated REST endpoints, including HTTP method, path, request/response body examples, and status codes.

#### Scenario: README exists
- **WHEN** the generated project structure is inspected
- **THEN** `README.md` exists at the project root

#### Scenario: API endpoints documented
- **WHEN** the README is inspected
- **THEN** all Todo CRUD endpoints are documented with HTTP method, path, example request body, example response body, and expected status codes

### Requirement: README with setup instructions
The README SHALL include setup instructions for running the application locally, including prerequisites (Java 25, Maven), build commands, and run commands.

#### Scenario: Setup instructions present
- **WHEN** the README is inspected
- **THEN** sections for prerequisites, build (`mvn clean install`), run (`mvn spring-boot:run`), and test (`mvn verify`) are present

### Requirement: Sequence diagram in README
The README SHALL include a sequence diagram illustrating the request flow from controller through service to repository and back. The diagram SHALL use Mermaid syntax.

#### Scenario: Sequence diagram present
- **WHEN** the README is inspected
- **THEN** a Mermaid sequence diagram block is present showing the flow: Client → Controller → Service → Repository → Database → back

### Requirement: README with Backstage template parameters
The README SHALL document the template parameters used to generate the project, including their names, descriptions, and default values.

#### Scenario: Parameters documented
- **WHEN** the README is inspected
- **THEN** a table or list documents `componentName`, `owner`, `groupId`, `artifactId`, `packageName`, and `description` with their defaults
