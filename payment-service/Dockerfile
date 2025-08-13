# Build stage
FROM gradle:8.9-jdk21 AS build
WORKDIR /home/gradle/src
COPY build.gradle .
COPY src ./src
RUN gradle build --no-daemon

# Package stage
FROM eclipse-temurin:21-jre-jammy
RUN apt-get update && apt-get install -y unzip
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/payment-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
