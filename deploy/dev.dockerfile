# Build stage

FROM gradle:8.11.1-jdk21-alpine AS builder
WORKDIR /app

COPY . .

RUN ./gradlew build

# Run stage

FROM bellsoft/liberica-openjdk-alpine:21
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

ARG SPRING_PROFILE=local
ENV SPRING_PROFILES_ACTIVE=$SPRING_PROFILE

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]