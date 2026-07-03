package ${{ values.packageName }}.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Security configuration test verifying that endpoints are protected.
 *
 * WHY @SpringBootTest: security configuration involves filter chains that
 * need the full application context to be properly tested. Slice tests
 * like @WebMvcTest do not load all security beans by default.
 *
 * WHY @AutoConfigureMockMvc: provides a MockMvc instance that respects
 * the Spring Security filter chain for testing authenticated/unauthenticated
 * access.
 */
@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRedirectUnauthenticatedRequestToLogin() throws Exception {
        // When an unauthenticated request is sent to a protected endpoint,
        // Spring Security redirects to the login page (302) or returns 401
        // depending on content type negotiation.
        // For browser requests (Accept: text/html), a 302 redirect is expected.
        mockMvc.perform(get("/api/todos")
                        .accept("text/html"))
                .andExpect(status().is3xxRedirection());
    }
}
