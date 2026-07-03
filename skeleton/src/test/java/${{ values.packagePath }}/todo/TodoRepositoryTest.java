package ${{ values.packageName }}.todo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

import ${{ values.packageName }}.config.JpaAuditingConfig;

/**
 * Repository layer test using @DataJpaTest.
 *
 * WHY @Import(JpaAuditingConfig.class): @DataJpaTest only scans JPA-related
 * components by default. The auditing configuration is in a separate
 * @Configuration class, so it must be explicitly imported for @CreatedDate
 * and @LastModifiedDate to be populated during tests.
 */
@DataJpaTest
@Import(JpaAuditingConfig.class)
class TodoRepositoryTest {

    @Autowired
    private TodoRepository todoRepository;

    @Test
    void shouldSaveAndFindById() {
        // Given
        TodoEntity todo = new TodoEntity("Learn Spring Boot", false);

        // When
        TodoEntity saved = todoRepository.save(todo);
        Optional<TodoEntity> found = todoRepository.findById(saved.getId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getTitle()).isEqualTo("Learn Spring Boot");
        assertThat(found.get().getCompleted()).isFalse();
    }

    @Test
    void shouldPopulateAuditingFieldsOnSave() {
        // Given
        TodoEntity todo = new TodoEntity("Learn JPA auditing", false);

        // When
        TodoEntity saved = todoRepository.save(todo);

        // Then — auditing fields should be automatically populated
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }
}
