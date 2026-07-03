# Multi-stage Dockerfile for Spring Boot application
# Stage 1: Build the application using Maven
# Stage 2: Run the application using a JRE base image

# --- Build Stage ---
FROM maven:3.9-eclipse-temurin-25 AS build

WORKDIR /app

# Copy pom.xml first to cache dependencies
COPY pom.xml .

# Download dependencies (cached layer)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests to speed up Docker build; tests run in CI)
RUN mvn clean package -DskipTests -B

# --- Runtime Stage ---
FROM eclipse-temurin:25-jre

WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose the default Spring Boot port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
