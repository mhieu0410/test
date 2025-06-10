FROM openjdk:21-jdk-slim

# Set working directory
WORKDIR /app

# Copy Maven wrapper and pom.xml
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src/ ./src/


# Build the application
RUN ./mvnw package -DskipTests

# Expose the port (Spring Boot default is 8080)
EXPOSE 8080

# Set environment variables for database connection
ENV SPRING_DATASOURCE_URL="jdbc:sqlserver://localhost:1433;databaseName=HiemMuon1;encrypt=true;trustServerCertificate=true"
ENV SPRING_DATASOURCE_USERNAME="sa"
ENV SPRING_DATASOURCE_PASSWORD="12345"
ENV SPRING_DATASOURCE_DRIVER_CLASS_NAME="com.microsoft.sqlserver.jdbc.SQLServerDriver"

# Run the application
CMD ["java", "-jar", "target/customer-0.0.1-SNAPSHOT.jar"]
