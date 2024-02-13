FROM maven:3.8.4-openjdk-17 AS build



WORKDIR /build

COPY pom.xml .
COPY src src

RUN mvn -e package



FROM amazoncorretto:17-alpine


WORKDIR /opt/myapp

COPY --from=build /build/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
