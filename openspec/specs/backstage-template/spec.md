# Backstage Template

## Purpose

Defines the Backstage Software Template that scaffolds a Spring Boot project using the native `fetch:template` action.

## Requirements

### Requirement: Template definition with fetch:template action
The template SHALL use Backstage's native `fetch:template` action to scaffold the project. The template SHALL NOT use Cookiecutter or `fetch:cookiecutter`.

#### Scenario: Template uses fetch:template
- **WHEN** a user triggers the template from Backstage Software Templates
- **THEN** the `fetch:template` action copies the skeleton directory and substitutes `${{ values.* }}` variables

#### Scenario: No Cookiecutter dependency
- **WHEN** the template is inspected
- **THEN** no Cookiecutter action, Python script, or `cookiecutter.json` is present

### Requirement: Template input parameters
The template SHALL accept the following input parameters: `componentName` (required), `owner` (required), `groupId` (default: `com.example`), `artifactId` (default: derived from `componentName`), `packageName` (default: equals `groupId`), `description` (default: provided). The template SHALL NOT include boolean toggle parameters.

#### Scenario: Required parameters enforced
- **WHEN** a user submits the template form without `componentName` or `owner`
- **THEN** Backstage validation prevents submission

#### Scenario: Default values applied
- **WHEN** a user submits the form with only `componentName` and `owner`
- **THEN** `groupId` defaults to `com.example`, `artifactId` is derived from `componentName`, `packageName` equals `groupId`, and `description` uses the provided default

#### Scenario: No boolean toggles
- **WHEN** the template parameters are inspected
- **THEN** no `includeSecurity`, `includeDockerfile`, `includeCICD`, `includeSampleModule`, `includeLombok`, or `buildTool` parameters exist

### Requirement: Backstage variable syntax
All template files SHALL use `${{ values.<param> }}` syntax for variable substitution. The template SHALL NOT use raw Nunjucks `{{ var }}` syntax.

#### Scenario: Variable substitution in pom.xml
- **WHEN** the template generates `pom.xml`
- **THEN** `${{ values.groupId }}` is replaced with the user-provided groupId and `${{ values.artifactId }}` is replaced with the user-provided artifactId

#### Scenario: Variable substitution in Java files
- **WHEN** the template generates `Application.java`
- **THEN** `${{ values.packagePath }}` is replaced with the dot-separated package path derived from `packageName`

### Requirement: Skeleton files use real extensions
All skeleton files SHALL use their real file extensions (e.g., `pom.xml`, `Application.java`, `application.yml`). No skeleton file SHALL have a `.njk` suffix.

#### Scenario: Generated file names are correct
- **WHEN** the template is scaffolded
- **THEN** generated files have names like `pom.xml`, `Application.java`, `application.yml` without any `.njk` suffix

### Requirement: Template registration in Backstage
The template SHALL include a `catalog-info.yaml` at the template level that registers it as a Backstage template entity of kind `Template`.

#### Scenario: Template appears in Backstage
- **WHEN** the template's `catalog-info.yaml` is registered in Backstage
- **THEN** the template appears in the Software Templates catalog with its name, description, and parameters
