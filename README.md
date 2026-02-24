# SchoolApp 9 SSR

A server-side rendered (SSR) Spring Boot application for managing teachers and users, built with Thymeleaf views, Spring Data JPA, Spring Security, and Flyway for database migrations.

This README covers the stack, entry points, how to run it locally, scripts, environment variables, tests, project structure, and license information.

## Stack
- Language: Java 21 (Amazon Corretto toolchain via Gradle)
- Frameworks/Libraries:
  - Spring Boot 3.5.x (Web, Thymeleaf, Validation)
  - Spring Data JPA (Hibernate)
  - Spring Security 6 (with Thymeleaf extras)
  - Flyway (database migrations)
  - Lombok (compile-time annotations)
- Database: MySQL 8+
- Build/Package Manager: Gradle (Wrapper included)

## Entry Point
- Main class: `gr.aueb.cf.schoolapp.SchoolappApplication`
  - Method: `public static void main(String[] args)` launches Spring Boot.
- Default active profile: `dev` (configured in `src/main/resources/application.properties`).

## Requirements
- Java 21 (the Gradle toolchain can auto-provision an Amazon Corretto 21 JDK on build)
- MySQL 8+ running and reachable
- Ports:
  - Default server port is 8080 (can be changed via `server.port`)

Optional: Docker for local MySQL instance (example below).

## Configuration and Environment Variables
Configuration is managed via Spring profiles and environment variables. Defaults for `dev` are defined in `application-dev.properties`.

Recognized environment variables (with `dev` defaults in parentheses):
- `SPRING_PROFILES_ACTIVE` — active profile (default: `dev` set in `application.properties`)
- `MYSQL_HOST` — MySQL host (default: `localhost`)
- `MYSQL_PORT` — MySQL port (default: `3306`)
- `MYSQL_DB` — database name (default: `school9ssr`)
- `MYSQL_USER` — database user (default: `user9`)
- `MYSQL_PASSWORD` — database password (default: `12345`)

Other relevant settings (profile-specific):
- `spring.jpa.hibernate.ddl-auto=validate` (expects schema managed by Flyway)
- Flyway is enabled in `dev` (can be toggled with `spring.flyway.enabled`)
- `spring.jpa.open-in-view=false` (encourages fetch joins in services)
- Thymeleaf cache disabled in `dev` for faster view iteration

Profiles available:
- `dev` — default for local development.
- `staging` — placeholder (currently minimal props).
- `prod` — placeholder (add production DB and tuning props here).

Activate a profile at runtime:
- JVM arg: `-Dspring.profiles.active=prod`
- or Spring arg: `--spring.profiles.active=prod`
- or environment variable: `SPRING_PROFILES_ACTIVE=prod`

## Setup
1. Clone the repository.
2. Start MySQL and create the database if it doesn’t exist (Flyway will manage schema):
   - Database: `school9ssr` (or set `MYSQL_DB`)
   - User: `user9` with password `12345` (or adjust `MYSQL_USER`/`MYSQL_PASSWORD`)
3. Optionally, run MySQL with Docker:
   - Linux/macOS PowerShell equivalent:
     - `docker run --name mysql8 -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=school9ssr -e MYSQL_USER=user9 -e MYSQL_PASSWORD=12345 -p 3306:3306 -d mysql:8`
   - Then export/set environment variables if you changed defaults.

## How to Run
Using the Gradle Wrapper (recommended):
- Windows PowerShell:
  - Development profile (default): `./gradlew.bat bootRun`
  - Explicit profile: `./gradlew.bat bootRun --args="--spring.profiles.active=dev"`
- macOS/Linux:
  - Development profile (default): `./gradlew bootRun`
  - Explicit profile: `./gradlew bootRun --args="--spring.profiles.active=dev"`

Build an executable JAR:
- Windows: `./gradlew.bat clean bootJar`
- macOS/Linux: `./gradlew clean bootJar`

Note: The resulting artifact is `build/libs/schoolapp.jar` (customized via `bootJar.archiveFileName` in `build.gradle`).

Run the JAR:
- Windows: `java -jar build\libs\schoolapp.jar --spring.profiles.active=prod`
- macOS/Linux: `java -jar build/libs/schoolapp.jar --spring.profiles.active=prod`

Access the app:
- Default URL: `http://localhost:8080/`
- Thymeleaf views are rendered on the server. Example pages include teachers listing, insert/edit forms, login page, etc.

## Common Gradle Scripts / Tasks
- `./gradlew[.bat] bootRun` — Run the application with hot reload (DevTools included as developmentOnly)
- `./gradlew[.bat] test` — Run tests (JUnit Platform)
- `./gradlew[.bat] build` — Build the project (runs tests)
- `./gradlew[.bat] clean` — Clean build outputs
- `./gradlew[.bat] bootJar` — Build the executable Spring Boot JAR

## Database Migrations (Flyway)
- Migration scripts live under `src/main/resources/db/migration` and run automatically on startup (when enabled).
- Current migration files:
  - `V1__initial_schema.sql`
  - `V2__insert_regions.sql`
  - `V3__alter_teachers_add_soft_delete_columns.sql`
  - `V4__teachers_soft_delete_indexes.sql`
  - `V5__create_users_roles_capabilities_indexes.sql`
  - `V6__insert_roles_capabilites.sql`

## Tests
- Location: `src/test/java`
- Example test suite: `gr.aueb.cf.schoolapp.SchoolappApplicationTests`
- Run: `./gradlew[.bat] test`

## Project Structure (selected)
- `src/main/java/gr/aueb/cf/schoolapp/`
  - `SchoolappApplication.java` — Spring Boot entry point
  - `controller/` — MVC controllers (e.g., `TeacherController`, `UserController`, `LoginController`)
  - `service/` — Service layer (`TeacherService`, `UserService`, etc.)
  - `repository/` — Spring Data JPA repositories
  - `model/` — JPA entities (e.g., `Teacher`, `User`, `Role`, `Capability`, `Region`)
  - `authentication/` — Security config and handlers
  - `dto/` — Data Transfer Objects
  - `validator/` — Validation logic for forms
  - `mapper/` — Mapping utilities
- `src/main/resources/`
  - `templates/` — Thymeleaf HTML templates (SSR views)
  - `static/` — Static assets (CSS, images)
  - `db/migration/` — Flyway migration scripts
  - `application*.properties` — Profile-based configuration

## Localization
- Message bundles: `messages.properties`, `messages_el.properties`

## Logging
- Configured via `src/main/resources/logback-spring.xml`
- Log files under `./logs` (e.g., `all.log`, `error.log`, `sql.log`, `tomcat.log`, and dated rollovers)

## Security
- Spring Security is enabled. Login-related pages and handlers live under `authentication/` and `controller/`.
- Thymeleaf extra dialect for security: `thymeleaf-extras-springsecurity6`.

## Troubleshooting
- Verify DB connectivity and credentials (see env vars above).
- If schema validation fails (`ddl-auto=validate`), ensure Flyway ran and the DB is empty/consistent.
- Change server port via `server.port` in the active profile properties or runtime arg.

## License
No explicit license file is provided in this repository. Unless a license is added, all rights are reserved by the repository owner. If you need to use this code, please contact the owner or add an appropriate LICENSE file.
