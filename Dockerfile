FROM eclipse-temurin:17-jre

WORKDIR /app

COPY target/app.jar app.jar

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=5s --retries=3 \
  CMD curl -f http://localhost:${SERVER_PORT:-8080}/actuator/health/readiness || exit 1


ENTRYPOINT ["java", "-jar", "app.jar"]
