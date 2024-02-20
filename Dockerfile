FROM openjdk:17-jdk-alpine
# Create app directory
WORKDIR /app

# Copy the JAR file into the container
COPY target/DataWareHouse-0.0.1-SNAPSHOT.jar /app/app.jar

# Command to run the application
CMD ["java", "-jar", "/app/app.jar"]