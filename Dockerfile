# ==============================
# Stage 1: Build the Spring Boot JAR
# ==============================
FROM maven:3.9.9-eclipse-temurin-17 AS build
WORKDIR /app

# Copy Maven project files
COPY pom.xml .
COPY src ./src

# Build the JAR (skip tests to speed up)
RUN mvn clean package -DskipTests


# ==============================
# Stage 2: Run the built app
# ==============================
FROM eclipse-temurin:17-jre-focal
WORKDIR /app

# Copy the JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port
EXPOSE 8095

# Environment variables (Defaults, will be overridden by docker-compose)
ENV MONGODB_URI=""
ENV MONGODB_DATABASE=""

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
