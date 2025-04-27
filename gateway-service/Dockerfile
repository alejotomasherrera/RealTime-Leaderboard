# Use an official Maven image to build the project
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy the entire project and build it
COPY . .
RUN mvn clean package -DskipTests

# Use an official JDK runtime image to run the application
FROM eclipse-temurin:21-jre

# Set the working directory inside the container
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

# Expose the port used by the Gateway
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]