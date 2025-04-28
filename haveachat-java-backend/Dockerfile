# Use an official Gradle image to build the application
FROM gradle:8.13-jdk21 AS build

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and configuration files
COPY gradlew ./
COPY gradle/ gradle/
COPY build.gradle.kts settings.gradle.kts ./

# Copy the rest of your project
COPY src/ ./src/

# Ensure gradlew is executable
RUN chmod +x ./gradlew

# Build the application JAR file
RUN ./gradlew clean bootJar -x test --info

# Debugging: List contents of the build/libs folder to confirm JAR exists
RUN ls -l /app/build/libs/

# Use a minimal JDK image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/build/libs/*.jar /app/haveachat-java-backend.jar

# Expose the port the application runs on
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "/app/haveachat-java-backend.jar"]