package ${{ values.packageName }};

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application entry point.
 *
 * Note: @EnableJpaAuditing is intentionally NOT placed here to avoid
 * "JPA metamodel must not be empty" errors in @WebMvcTest tests.
 * See JpaAuditingConfig for the separate auditing configuration.
 */
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
