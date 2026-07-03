## ADDED Requirements

### Requirement: Generated catalog-info.yaml
The generated project SHALL include a `catalog-info.yaml` file at the project root that registers the generated service as a Backstage entity of kind `Component`.

#### Scenario: catalog-info.yaml exists
- **WHEN** the generated project structure is inspected
- **THEN** `catalog-info.yaml` exists at the project root

#### Scenario: Component entity with correct metadata
- **WHEN** the generated `catalog-info.yaml` is inspected
- **THEN** it defines `apiVersion: backstage.io/v1alpha1`, `kind: Component`, `metadata.name` set to `${{ values.componentName }}`, and `metadata.description` set to `${{ values.description }}`

#### Scenario: Owner set from template parameter
- **WHEN** the generated `catalog-info.yaml` is inspected
- **THEN** `spec.owner` is set to `${{ values.owner }}`

#### Scenario: Component type is service
- **WHEN** the generated `catalog-info.yaml` is inspected
- **THEN** `spec.type` is set to `service`
