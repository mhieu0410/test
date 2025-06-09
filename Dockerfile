# Stage 1: Build with Maven + JDK 21
FROM eclipse-temurin:21-jdk AS build
WORKDIR /app

# Cài Maven thủ công
RUN apt-get update && apt-get install -y maven
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run with JDK 21 only
FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
