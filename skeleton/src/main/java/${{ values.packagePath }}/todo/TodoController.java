package ${{ values.packageName }}.todo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST controller for Todo CRUD operations.
 *
 * All endpoints are mapped to /api/todos following RESTful conventions.
 *
 * WHY specific HTTP status codes:
 * - 201 Created: POST returns this to indicate a new resource was created
 * - 200 OK: GET and PUT return this for successful retrieval/update
 * - 204 No Content: DELETE returns this because no body is needed
 * - 404 Not Found: returned when a requested resource does not exist
 */
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * Create a new Todo.
     * Returns 201 Created because a new resource was created.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TodoDto create(@RequestBody TodoDto dto) {
        return todoService.create(dto);
    }

    /**
     * Get a Todo by ID.
     * Returns 200 OK with the todo DTO if found.
     * Returns 404 Not Found if the todo does not exist.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TodoDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(todoService.findById(id));
    }

    /**
     * Get all Todos.
     * Returns 200 OK with a list of all todo DTOs.
     */
    @GetMapping
    public List<TodoDto> findAll() {
        return todoService.findAll();
    }

    /**
     * Update an existing Todo.
     * Returns 200 OK with the updated todo DTO.
     * Returns 404 Not Found if the todo does not exist.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TodoDto> update(@PathVariable Long id, @RequestBody TodoDto dto) {
        return ResponseEntity.ok(todoService.update(id, dto));
    }

    /**
     * Delete a Todo by ID.
     * Returns 204 No Content because the resource was deleted and no body is needed.
     * Returns 404 Not Found if the todo does not exist.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        todoService.delete(id);
    }

    /**
     * Exception handler for RuntimeException thrown by the service layer
     * when a resource is not found.
     * WHY 404: the client requested a resource that does not exist.
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleNotFound(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
