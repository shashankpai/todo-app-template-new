package ${{ values.packageName }}.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Spring Security baseline configuration.
 *
 * WHY form login: form-based authentication is the simplest baseline that
 * works out of the box with a login page. It serves as a starting point
 * that teams can replace with OAuth2, JWT, or other mechanisms as needed.
 *
 * WHY CSRF enabled: CSRF protection prevents cross-site request forgery
 * attacks on state-changing operations (POST, PUT, DELETE). It is enabled
 * by default in Spring Security and should remain enabled for browser-based
 * clients. API-only clients may disable it, but the baseline keeps it on
 * for security best practices.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .defaultSuccessUrl("/api/todos", true)
                        .permitAll()
                )
                // CSRF is enabled by default in Spring Security — no explicit config needed.
                // Keeping it enabled for security best practices (protects POST/PUT/DELETE).
                ;

        return http.build();
    }
}
