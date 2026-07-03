package ${{ values.packageName }}.todo;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Todo CRUD operations.
 *
 * WHY manual DTO ↔ Entity mapping: keeps the mapping logic explicit and
 * visible for developers learning Spring Boot layering. No MapStruct or
 * other mapping libraries are used to avoid hidden complexity.
 *
 * WHY RuntimeException for not-found: the service layer signals that a
 * requested resource does not exist. The controller layer is responsible
 * for translating this to an appropriate HTTP 404 response.
 */
@Service
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Create a new Todo.
     * Maps the incoming DTO to a new entity, persists it, and returns the saved DTO.
     */
    public TodoDto create(TodoDto dto) {
        TodoEntity entity = new TodoEntity(dto.getTitle(), dto.getCompleted());
        TodoEntity saved = todoRepository.save(entity);
        return toDto(saved);
    }

    /**
     * Find a Todo by ID.
     * Throws RuntimeException if the entity does not exist.
     */
    public TodoDto findById(Long id) {
        TodoEntity entity = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        return toDto(entity);
    }

    /**
     * Find all Todos.
     * Returns a list of DTOs mapped from all entities.
     */
    public List<TodoDto> findAll() {
        return todoRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    /**
     * Update an existing Todo.
     * Throws RuntimeException if the entity does not exist.
     */
    public TodoDto update(Long id, TodoDto dto) {
        TodoEntity entity = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        entity.setTitle(dto.getTitle());
        entity.setCompleted(dto.getCompleted());
        TodoEntity updated = todoRepository.save(entity);
        return toDto(updated);
    }

    /**
     * Delete a Todo by ID.
     * Throws RuntimeException if the entity does not exist.
     */
    public void delete(Long id) {
        TodoEntity entity = todoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Todo not found with id: " + id));
        todoRepository.delete(entity);
    }

    /**
     * Manual mapping from Entity to DTO.
     * WHY manual: explicit field-by-field mapping is transparent and
     * easy to understand for developers learning the service layer pattern.
     */
    private TodoDto toDto(TodoEntity entity) {
        TodoDto dto = new TodoDto();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setCompleted(entity.getCompleted());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}
