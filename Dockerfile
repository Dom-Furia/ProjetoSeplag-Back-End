# ===== STAGE 1: Build =====
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

# Copia apenas pom primeiro (melhor cache)
COPY pom.xml .

# Baixa dependências primeiro
RUN mvn -B -U -DskipTests dependency:go-offline

# Agora copia o código
COPY src ./src

# Gera o JAR
RUN mvn -B -DskipTests package


# ===== STAGE 2: Runtime =====
FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Instala curl para healthcheck
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health/readiness || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
