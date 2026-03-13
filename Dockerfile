# Stage 1: Build
FROM maven:3.9.7-eclipse-temurin-21-alpine AS builder

WORKDIR /build

# Copiar pom.xml
COPY pom.xml .

# Copiar mvnw (Maven Wrapper)
COPY mvnw .
COPY mvnw.cmd .

# Copiar código fuente
COPY src src

# Compilar (descargar dependencias y compilar)
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Copiar JAR compilado del stage anterior
COPY --from=builder /build/target/*.jar app.jar

# Puerto
EXPOSE 8080

# Ejecutar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]