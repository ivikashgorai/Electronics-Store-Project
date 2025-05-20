# Use OpenJDK 21 base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Argument for the jar file path from Maven/Gradle build
ARG JAR_FILE=target/*.jar

# Copy the jar into the container and rename it
COPY ${JAR_FILE} app.jar

# Expose the port Spring Boot runs on (default is 8080)
EXPOSE 8080

# Run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app.jar"]
