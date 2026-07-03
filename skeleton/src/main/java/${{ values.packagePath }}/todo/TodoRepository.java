package ${{ values.packageName }}.todo;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for TodoEntity.
 *
 * Extends JpaRepository to inherit standard CRUD operations without
 * writing any custom query methods. All basic operations (save, findById,
 * findAll, deleteById, etc.) are provided by Spring Data automatically.
 */
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
}
