# Todo CRUD Module

## Purpose

Defines the Todo CRUD module including entity, DTO, repository, service, controller, and tests.

## Requirements

### Requirement: Todo entity with JPA auditing
The generated project SHALL include a `TodoEntity` class annotated with `@Entity`, `@Table`, and JPA auditing annotations (`@CreatedDate`, `@LastModifiedDate`, `@EntityListeners(AuditingEntityListener.class)`). All JPA annotation imports SHALL be explicit, including `jakarta.persistence.EntityListeners`.

#### Scenario: Entity has auditing fields
- **WHEN** the generated `TodoEntity.java` is inspected
- **THEN** it includes `@CreatedDate` and `@LastModifiedDate` fields with `@EntityListeners(AuditingEntityListener.class)` on the class

#### Scenario: Explicit EntityListeners import
- **WHEN** the generated `TodoEntity.java` imports are inspected
- **THEN** `jakarta.persistence.EntityListeners` is explicitly imported

#### Scenario: No-arg constructor present
- **WHEN** the generated `TodoEntity.java` is inspected
- **THEN** a protected no-arg constructor exists for JPA compliance

### Requirement: Todo DTO for API layer
The generated project SHALL include a `TodoDto` class that serves as the API request/response object. The DTO SHALL be separate from the entity. Manual DTO ↔ Entity mapping SHALL be performed in the service layer.

#### Scenario: DTO has API fields
- **WHEN** the generated `TodoDto.java` is inspected
- **THEN** it contains `id`, `title`, `completed`, `createdAt`, and `updatedAt` fields with explicit getters and setters

#### Scenario: DTO is not a JPA entity
- **WHEN** the generated `TodoDto.java` is inspected
- **THEN** no `@Entity`, `@Table`, or JPA annotations are present

### Requirement: Todo repository interface
The generated project SHALL include a `TodoRepository` interface extending `JpaRepository<TodoEntity, Long>`.

#### Scenario: Repository extends JpaRepository
- **WHEN** the generated `TodoRepository.java` is inspected
- **THEN** it extends `JpaRepository<TodoEntity, Long>` with no additional custom methods beyond standard CRUD

### Requirement: Todo service with CRUD operations
The generated project SHALL include a `TodoService` class with methods for create, read (by ID and all), update, and delete operations. The service SHALL perform manual DTO ↔ Entity mapping.

#### Scenario: Create todo
- **WHEN** `TodoService.create(TodoDto)` is called with a valid DTO
- **THEN** a new `TodoEntity` is persisted and the returned DTO includes the generated ID and timestamps

#### Scenario: Get todo by ID
- **WHEN** `TodoService.findById(Long id)` is called with an existing ID
- **THEN** the corresponding `TodoDto` is returned

#### Scenario: Get todo by ID not found
- **WHEN** `TodoService.findById(Long id)` is called with a non-existent ID
- **THEN** a `RuntimeException` (or appropriate custom exception) is thrown

#### Scenario: Get all todos
- **WHEN** `TodoService.findAll()` is called
- **THEN** a list of all `TodoDto` objects is returned

#### Scenario: Update todo
- **WHEN** `TodoService.update(Long id, TodoDto)` is called with an existing ID and valid DTO
- **THEN** the entity is updated and the updated DTO with new `updatedAt` timestamp is returned

#### Scenario: Delete todo
- **WHEN** `TodoService.delete(Long id)` is called with an existing ID
- **THEN** the entity is removed from the database

### Requirement: Todo controller with REST endpoints
The generated project SHALL include a `TodoController` class with REST endpoints for CRUD operations mapped to `/api/todos`.

#### Scenario: POST creates todo
- **WHEN** a POST request is sent to `/api/todos` with a valid JSON body
- **THEN** a `201 Created` response is returned with the created todo DTO

#### Scenario: GET retrieves todo by ID
- **WHEN** a GET request is sent to `/api/todos/{id}` with an existing ID
- **THEN** a `200 OK` response is returned with the todo DTO

#### Scenario: GET retrieves all todos
- **WHEN** a GET request is sent to `/api/todos`
- **THEN** a `200 OK` response is returned with a list of todo DTOs

#### Scenario: PUT updates todo
- **WHEN** a PUT request is sent to `/api/todos/{id}` with a valid JSON body
- **THEN** a `200 OK` response is returned with the updated todo DTO

#### Scenario: DELETE removes todo
- **WHEN** a DELETE request is sent to `/api/todos/{id}` with an existing ID
- **THEN** a `204 No Content` response is returned

### Requirement: Controller test with WebMvcTest
The generated project SHALL include a `TodoControllerTest` using `@WebMvcTest` from `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`. Mock dependencies SHALL use `@MockitoBean` from `org.springframework.test.context.bean.override.mockito.MockitoBean`.

#### Scenario: WebMvcTest annotation correct
- **WHEN** the generated `TodoControllerTest.java` is inspected
- **THEN** the `@WebMvcTest` import is from `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest`

#### Scenario: MockitoBean used not MockBean
- **WHEN** the generated `TodoControllerTest.java` is inspected
- **THEN** `@MockitoBean` is used (imported from `org.springframework.test.context.bean.override.mockito.MockitoBean`) and `@MockBean` is NOT present

#### Scenario: Controller test verifies GET endpoint
- **WHEN** the controller test performs a GET to `/api/todos/{id}` with a mocked service returning a DTO
- **THEN** the test asserts a `200 OK` status with the expected JSON body

### Requirement: Service test with mocked repository
The generated project SHALL include a `TodoServiceTest` that unit-tests the service layer with a mocked `TodoRepository`.

#### Scenario: Service test verifies create
- **WHEN** the service test calls `create()` with a valid DTO and the mocked repository returns a saved entity
- **THEN** the test asserts the returned DTO has the expected fields

#### Scenario: Service test verifies not found
- **WHEN** the service test calls `findById()` with a mocked repository returning empty Optional
- **THEN** the test asserts a `RuntimeException` is thrown

### Requirement: Repository test with DataJpaTest
The generated project SHALL include a `TodoRepositoryTest` using `@DataJpaTest` from `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`.

#### Scenario: DataJpaTest annotation correct
- **WHEN** the generated `TodoRepositoryTest.java` is inspected
- **THEN** the `@DataJpaTest` import is from `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest`

#### Scenario: Repository test verifies save and find
- **WHEN** the repository test saves a `TodoEntity` and then calls `findById()`
- **THEN** the test asserts the returned entity matches the saved data

### Requirement: TDD sequencing in generated tests
All generated test classes SHALL be written before implementation (Red → Green → Refactor). The test files SHALL exist alongside their corresponding source files in the same package structure under `src/test/java/`.

#### Scenario: Test classes mirror source classes
- **WHEN** the generated project test structure is inspected
- **THEN** `TodoControllerTest`, `TodoServiceTest`, and `TodoRepositoryTest` exist under the same package as their corresponding source classes
