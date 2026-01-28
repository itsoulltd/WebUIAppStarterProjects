# =========================
# Runtime stage
# =========================
#FROM eclipse-temurin:21-jre-alpine
#FROM eclipse-temurin:17-alpine
FROM eclipse-temurin:17-jre
LABEL authors="towhid.islam"
MAINTAINER ${authors}

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
# Copy the built JAR from target dir
COPY target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Optional: reduce memory footprint
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
#ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar --spring.profiles.active=prod"]