package ${{ values.packageName }}.todo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for TodoService with a mocked TodoRepository.
 *
 * WHY mock the repository: isolates the service layer test from the
 * database, making tests fast and focused on business logic (DTO ↔ Entity
 * mapping, exception handling) rather than persistence concerns.
 */
@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private TodoService todoService;

    @Test
    void shouldCreateTodo() {
        // Given
        TodoDto inputDto = new TodoDto();
        inputDto.setTitle("Learn Spring Boot");
        inputDto.setCompleted(false);

        TodoEntity savedEntity = new TodoEntity("Learn Spring Boot", false);
        savedEntity.setId(1L);
        savedEntity.setCreatedAt(Instant.now());
        savedEntity.setUpdatedAt(Instant.now());

        when(todoRepository.save(any(TodoEntity.class))).thenReturn(savedEntity);

        // When
        TodoDto result = todoService.create(inputDto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Learn Spring Boot");
        assertThat(result.getCompleted()).isFalse();
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getUpdatedAt()).isNotNull();
    }

    @Test
    void shouldFindById() {
        // Given
        TodoEntity entity = new TodoEntity("Learn Spring Boot", false);
        entity.setId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        when(todoRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        TodoDto result = todoService.findById(1L);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Learn Spring Boot");
    }

    @Test
    void shouldThrowWhenFindByIdNotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then — WHY RuntimeException: signals to the caller that the
        // resource does not exist; the controller translates this to 404
        assertThatThrownBy(() -> todoService.findById(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    @Test
    void shouldFindAll() {
        // Given
        TodoEntity entity1 = new TodoEntity("Task 1", false);
        entity1.setId(1L);
        entity1.setCreatedAt(Instant.now());
        entity1.setUpdatedAt(Instant.now());

        TodoEntity entity2 = new TodoEntity("Task 2", true);
        entity2.setId(2L);
        entity2.setCreatedAt(Instant.now());
        entity2.setUpdatedAt(Instant.now());

        when(todoRepository.findAll()).thenReturn(List.of(entity1, entity2));

        // When
        List<TodoDto> results = todoService.findAll();

        // Then
        assertThat(results).hasSize(2);
        assertThat(results.get(0).getTitle()).isEqualTo("Task 1");
        assertThat(results.get(1).getTitle()).isEqualTo("Task 2");
    }

    @Test
    void shouldUpdateTodo() {
        // Given
        TodoEntity existing = new TodoEntity("Old title", false);
        existing.setId(1L);
        existing.setCreatedAt(Instant.now());
        existing.setUpdatedAt(Instant.now());

        TodoDto updateDto = new TodoDto();
        updateDto.setTitle("New title");
        updateDto.setCompleted(true);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(any(TodoEntity.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When
        TodoDto result = todoService.update(1L, updateDto);

        // Then
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("New title");
        assertThat(result.getCompleted()).isTrue();
    }

    @Test
    void shouldThrowWhenUpdateNotFound() {
        // Given
        TodoDto updateDto = new TodoDto();
        updateDto.setTitle("New title");
        updateDto.setCompleted(true);

        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.update(999L, updateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }

    @Test
    void shouldDeleteTodo() {
        // Given
        TodoEntity entity = new TodoEntity("Task to delete", false);
        entity.setId(1L);

        when(todoRepository.findById(1L)).thenReturn(Optional.of(entity));

        // When
        todoService.delete(1L);

        // Then
        verify(todoRepository).delete(entity);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        // Given
        when(todoRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> todoService.delete(999L))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("999");
    }
}
