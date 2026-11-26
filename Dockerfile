FROM gradle:8.14-jdk21 AS build
WORKDIR /workspace
COPY . .
RUN gradle :app:bootJar --no-daemon

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /workspace/app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
