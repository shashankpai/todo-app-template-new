package ${{ values.packageName }}.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import ${{ values.packageName }}.config.SecurityConfig;

/**
 * Controller layer test using @WebMvcTest.
 *
 * WHY @WebMvcTest: loads only the web layer (controllers, filters, JSON
 * serialization) without starting the full application context. This makes
 * controller tests fast and focused on HTTP request/response behavior.
 *
 * WHY @MockitoBean (not @MockBean): @MockBean was removed in Spring Boot 4.x.
 * @MockitoBean from org.springframework.test.context.bean.override.mockito
 * is the replacement with equivalent functionality.
 *
 * WHY @Import(SecurityConfig.class): @WebMvcTest does not load non-controller
 * beans by default. SecurityConfig must be explicitly imported so that the
 * security filter chain is active during controller tests.
 */
@WebMvcTest(TodoController.class)
@Import(SecurityConfig.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TodoService todoService;

    @Test
    void shouldCreateTodo() throws Exception {
        // Given
        TodoDto savedDto = new TodoDto();
        savedDto.setId(1L);
        savedDto.setTitle("Learn Spring Boot");
        savedDto.setCompleted(false);
        savedDto.setCreatedAt(Instant.now());
        savedDto.setUpdatedAt(Instant.now());

        when(todoService.create(any(TodoDto.class))).thenReturn(savedDto);

        // When & Then — 201 Created because a new resource was created
        mockMvc.perform(post("/api/todos")
                        .with(csrf())
                        .with(user("testuser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Learn Spring Boot\",\"completed\":false}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldGetTodoById() throws Exception {
        // Given
        TodoDto dto = new TodoDto();
        dto.setId(1L);
        dto.setTitle("Learn Spring Boot");
        dto.setCompleted(false);
        dto.setCreatedAt(Instant.now());
        dto.setUpdatedAt(Instant.now());

        when(todoService.findById(1L)).thenReturn(dto);

        // When & Then — 200 OK because the resource was found
        mockMvc.perform(get("/api/todos/1")
                        .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Learn Spring Boot"));
    }

    @Test
    void shouldGetAllTodos() throws Exception {
        // Given
        TodoDto dto1 = new TodoDto();
        dto1.setId(1L);
        dto1.setTitle("Task 1");
        dto1.setCompleted(false);
        dto1.setCreatedAt(Instant.now());
        dto1.setUpdatedAt(Instant.now());

        TodoDto dto2 = new TodoDto();
        dto2.setId(2L);
        dto2.setTitle("Task 2");
        dto2.setCompleted(true);
        dto2.setCreatedAt(Instant.now());
        dto2.setUpdatedAt(Instant.now());

        when(todoService.findAll()).thenReturn(List.of(dto1, dto2));

        // When & Then — 200 OK with a list
        mockMvc.perform(get("/api/todos")
                        .with(user("testuser")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Task 1"))
                .andExpect(jsonPath("$[1].title").value("Task 2"));
    }

    @Test
    void shouldUpdateTodo() throws Exception {
        // Given
        TodoDto updatedDto = new TodoDto();
        updatedDto.setId(1L);
        updatedDto.setTitle("Updated title");
        updatedDto.setCompleted(true);
        updatedDto.setCreatedAt(Instant.now());
        updatedDto.setUpdatedAt(Instant.now());

        when(todoService.update(eq(1L), any(TodoDto.class))).thenReturn(updatedDto);

        // When & Then — 200 OK because the resource was updated successfully
        mockMvc.perform(put("/api/todos/1")
                        .with(csrf())
                        .with(user("testuser"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"title\":\"Updated title\",\"completed\":true}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated title"))
                .andExpect(jsonPath("$.completed").value(true));
    }

    @Test
    void shouldDeleteTodo() throws Exception {
        // Given
        doNothing().when(todoService).delete(1L);

        // When & Then — 204 No Content because the resource was deleted
        mockMvc.perform(delete("/api/todos/1")
                        .with(csrf())
                        .with(user("testuser")))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenTodoNotFound() throws Exception {
        // Given
        when(todoService.findById(999L)).thenThrow(new RuntimeException("Todo not found with id: 999"));

        // When & Then — 404 Not Found because the resource does not exist
        mockMvc.perform(get("/api/todos/999")
                        .with(user("testuser")))
                .andExpect(status().isNotFound());
    }
}
