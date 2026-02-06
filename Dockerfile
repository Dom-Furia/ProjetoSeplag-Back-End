# ===== STAGE 1: Build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia arquivos do Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Baixa dependências e gera o JAR
RUN ./mvnw clean package -DskipTests

# ===== STAGE 2: Runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copia o JAR do stage de build
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Instala curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

# Healthcheck Spring Boot Actuator
HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT:-8080}/actuator/health/readiness || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
