FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/*.jar /app/ride-request-service.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ride-request-service.jar"]
