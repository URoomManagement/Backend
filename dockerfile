# Stage 1: Base for dependencies
FROM maven:3.9.6-eclipse-temurin-17 AS base
WORKDIR /app
COPY pom.xml .
COPY src ./src
COPY .env .
RUN mvn dependency:resolve

# Stage 2: Development build
FROM base AS dev
CMD ["mvn", "spring-boot:run"]

# Stage 3: Build for production
FROM base AS build
RUN mvn clean package spring-boot:repackage -DskipTests

# Stage 4: Production runtime
FROM eclipse-temurin:17-jdk-alpine AS prod
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8000
CMD ["java", "-jar", "app.jar"]