# Multi-stage Dockerfile for taubakul-zhanibek-student-api (author: Taubakul Zhanibek)

# ---------- Stage 1: build the jar with the JDK and the Gradle wrapper ----------
FROM eclipse-temurin:21-jdk AS build
WORKDIR /workspace

# Copy only the build configuration first. These layers change rarely, so Docker
# reuses the cached Gradle distribution and dependencies when only src/ changes.
COPY gradlew settings.gradle build.gradle ./
COPY gradle gradle
RUN ./gradlew dependencies --no-daemon > /dev/null

# Copy the source code and build the executable Spring Boot jar.
# Tests are not run here: the Jenkins pipeline runs them in its Test stage
# before the Docker Build stage, so the image is built from already tested code.
COPY src src
RUN ./gradlew bootJar --no-daemon

# ---------- Stage 2: small runtime image with the JRE only ----------
FROM eclipse-temurin:21-jre

LABEL org.opencontainers.image.title="taubakul-zhanibek-student-api" \
      org.opencontainers.image.description="Student CRUD API (Spring Boot, PostgreSQL)" \
      org.opencontainers.image.authors="Taubakul Zhanibek <taubakulzhanibek@gmail.com>" \
      author="Taubakul Zhanibek"

# Run the application as an unprivileged user instead of root
RUN groupadd --system app && useradd --system --gid app --no-create-home app

WORKDIR /app

# Only the jar is taken from the build stage; the JDK, Gradle and sources stay behind
COPY --from=build /workspace/build/libs/*.jar app.jar

USER app

# Port the application listens on inside the container (server.port default)
EXPOSE 8080

# Exec form: java is PID 1 and receives SIGTERM directly on "docker stop"
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
