# First stage: Build stage
FROM maven:3-amazoncorretto-17 AS build

# Set the working directory inside the container
WORKDIR /app/myapp

# Copy the project's POM file
COPY pom.xml .

# Copy the source code
COPY src ./src

# Build the project
RUN mvn package

# Second stage: Deployment stage
FROM amazoncorretto:17-alpine

# Set the working directory inside the container
WORKDIR /app/myapp

# Copy the JAR file built in the previous stage to the current directory
COPY --from=build /app/myapp/target/ROOT.jar app.jar

# Define the command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
