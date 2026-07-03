package ${{ values.packageName }}.todo;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.Instant;

/**
 * JPA entity representing a Todo item.
 *
 * Uses JPA auditing to automatically populate createdAt and updatedAt timestamps.
 * WHY auditing: eliminates manual timestamp management and ensures consistency
 * across all persist and update operations.
 *
 * No-arg constructor is required by JPA for proxy instantiation and entity
 * rehydration. It is protected to prevent direct instantiation outside JPA.
 */
@Entity
@Table(name = "todos")
@EntityListeners(AuditingEntityListener.class)
public class TodoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private Boolean completed;

    // WHY @CreatedDate: automatically set by JPA auditing on first persist
    @CreatedDate
    private Instant createdAt;

    // WHY @LastModifiedDate: automatically updated by JPA auditing on every save
    @LastModifiedDate
    private Instant updatedAt;

    /**
     * Protected no-arg constructor required by JPA for entity instantiation.
     */
    protected TodoEntity() {
    }

    /**
     * Convenience constructor for creating new todo entities.
     */
    public TodoEntity(String title, Boolean completed) {
        this.title = title;
        this.completed = completed;
    }

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
