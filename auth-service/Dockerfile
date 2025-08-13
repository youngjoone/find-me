# Build stage
FROM gradle:8.9-jdk21 AS build
WORKDIR /home/gradle/src
COPY build.gradle .
COPY src ./src
RUN gradle build --no-daemon

# Package stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]