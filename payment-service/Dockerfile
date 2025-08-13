# Build stage
FROM gradle:8.9-jdk21 AS build
WORKDIR /home/gradle/src
COPY build.gradle ./
COPY src ./src
RUN gradle bootJar -x test --no-daemon

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/*-SNAPSHOT.jar /app/app.jar
ENTRYPOINT ["java","-XX:MaxRAMPercentage=75.0","-jar","/app/app.jar"]
