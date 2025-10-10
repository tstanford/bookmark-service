# Use OpenJDK 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy the built JAR file into the container

COPY build/libs/bookmark-service.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]