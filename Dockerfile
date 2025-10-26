FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies first (to leverage Docker cache)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the rest of the source code
COPY src ./src

# Build the Spring Boot JAR file
RUN mvn clean package -DskipTests

# =========================
# 2. Use a lightweight JDK image to run the app
# =========================
FROM eclipse-temurin:21-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy the built JAR from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 for the application
EXPOSE 8080

# Set environment variables (optional but handy for Spring Boot)
ENV SPRING_PROFILES_ACTIVE=docker

# Run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]