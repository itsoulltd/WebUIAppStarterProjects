# =========================
# Runtime stage
# =========================
#FROM eclipse-temurin:21-jre-alpine
FROM eclipse-temurin:17-jre-alpine
#FROM eclipse-temurin:17-jre
LABEL authors="towhid.islam"
MAINTAINER ${authors}

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
# Copy the built JAR from target dir
COPY target/*.jar app.jar

# Expose Spring Boot default port
EXPOSE 8080

# Optional:
ENV JAVA_TOOL_OPTIONS="\
-XX:MaxRAMPercentage=75 \
-Xms256m \
-XX:+UseG1GC \
-Dfile.encoding=UTF-8 \
-Duser.timezone=UTC"

ENTRYPOINT ["java", "-jar", "app.jar"]
#ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]