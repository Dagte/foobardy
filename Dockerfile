FROM openjdk:11-jre-slim

WORKDIR /app

COPY build/libs/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]
