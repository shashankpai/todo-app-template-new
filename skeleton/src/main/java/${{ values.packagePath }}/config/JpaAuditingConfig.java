package ${{ values.packageName }}.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Separate configuration class for JPA auditing.
 *
 * WHY this is a separate class: Placing @EnableJpaAuditing on the
 * @SpringBootApplication class causes "JPA metamodel must not be empty"
 * errors in @WebMvcTest tests because the auditing configuration triggers
 * JPA context loading even when only the web layer is being tested.
 * This is a known Spring Boot 4.x pitfall.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
