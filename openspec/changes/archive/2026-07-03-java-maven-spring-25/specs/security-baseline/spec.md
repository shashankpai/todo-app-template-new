## ADDED Requirements

### Requirement: Spring Security form login baseline
The generated project SHALL include `spring-boot-starter-security` as a dependency. A `SecurityConfig` class SHALL be generated with form login configured as the default authentication mechanism.

#### Scenario: Security dependency present
- **WHEN** the generated `pom.xml` is inspected
- **THEN** `spring-boot-starter-security` is listed as a dependency

#### Scenario: SecurityConfig class exists
- **WHEN** the generated project is inspected
- **THEN** a `SecurityConfig` class exists with `@Configuration` and `@EnableWebSecurity` annotations

#### Scenario: Form login configured
- **WHEN** the generated `SecurityConfig` is inspected
- **THEN** form login is configured with CSRF enabled and default login/logout endpoints

### Requirement: Security always included
Spring Security SHALL always be generated. No `includeSecurity` parameter or toggle SHALL exist in the template.

#### Scenario: No security toggle parameter
- **WHEN** the template parameters are inspected
- **THEN** no `includeSecurity` parameter exists

### Requirement: Security test
The generated project SHALL include a `SecurityConfigTest` that verifies the security configuration is applied and endpoints are protected.

#### Scenario: Unauthenticated request redirected
- **WHEN** a test sends an unauthenticated GET request to a protected endpoint
- **THEN** the response is a redirect to the login page (302 or 401 depending on content type negotiation)
