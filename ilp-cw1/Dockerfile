# Use a base image with Java 17 (or another version that matches your project)
FROM --platform=linux/amd64 openjdk:21

# Set the working directory in the container
WORKDIR /app

# Copy the Spring Boot jar file to the container
COPY target/ilp-cw1-0.0.1-SNAPSHOT.jar /app/ilp-cw1-0.0.1-SNAPSHOT.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Define the command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/ilp-cw1-0.0.1-SNAPSHOT.jar"]
