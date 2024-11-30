# Use an official OpenJDK runtime as a parent image
FROM openjdk:23-jdk-slim-bullseye

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file into the container
COPY quick-chat-back-0.0.1-SNAPSHOT.jar myapp.jar

# Set environment variables
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://host.docker.internal:5432/quick-chat-db
ENV SPRING_DATASOURCE_USERNAME=quick-chat
ENV SPRING_DATASOURCE_PASSWORD=quick-chat
ENV KEYCLOAK_URL=http://host.docker.internal:8070/realms/quick-chat

# Expose the port your application runs on
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "myapp.jar"]