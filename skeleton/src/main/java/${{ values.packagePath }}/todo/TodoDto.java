package ${{ values.packageName }}.todo;

import java.time.Instant;

/**
 * Data Transfer Object for Todo API requests and responses.
 *
 * WHY a separate DTO: decouples the API contract from the JPA entity.
 * This prevents accidentally exposing internal fields (like auditing
 * mechanisms) and allows the entity schema to evolve independently
 * from the API contract.
 *
 * Manual mapping between DTO and Entity is performed in TodoService
 * to keep the mapping logic transparent and visible for learning purposes.
 */
public class TodoDto {

    private Long id;
    private String title;
    private Boolean completed;
    private Instant createdAt;
    private Instant updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
