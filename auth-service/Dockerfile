# Build stage
FROM gradle:8.5.0-jdk17-jammy AS build
WORKDIR /home/gradle/src
COPY build.gradle .
COPY src ./src
RUN gradle build --no-daemon
RUN find /home/gradle/src/build/libs -type f -name '*.jar'

# Package stage
FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y unzip
WORKDIR /app
COPY --from=build /home/gradle/src/build/libs/auth-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
