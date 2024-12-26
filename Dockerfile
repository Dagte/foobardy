FROM openjdk:11-jre-slim

WORKDIR /app

# Copy Gradle wrapper files
COPY gradlew gradlew
COPY gradle gradle

# Copy your project files
COPY . .

# Build the project
RUN ./gradlew buildFatJar -x test --stacktrace

EXPOSE 8080

CMD ["java", "-jar", "/app/build/libs/fat.jar"]
