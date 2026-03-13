# Stage 1: Build
FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copiar archivos Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn

# Copiar código fuente
COPY src src

# Compilar
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR compilado del stage anterior
COPY --from=builder /build/target/*.jar app.jar

# Puerto
EXPOSE 8080

# Health check
HEALTHCHECK --interval=10s --timeout=5s --retries=5 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# Ejecutar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]