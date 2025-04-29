FROM openjdk:11-jre-slim

RUN apt-get update && \
    apt-get install -y --no-install-recommends \
    fontconfig \
    libfreetype6 \
    && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copy Gradle wrapper files
COPY gradlew gradlew
COPY gradle gradle

# Copy your project files
COPY . .

# Build the project
RUN ./gradlew buildFatJar -x test --stacktrace

EXPOSE 8080

# Set default environment variables for database connection
# These will be overridden by environment variables in Render
ENV DB_HOST=localhost
ENV DB_PORT=5432
ENV DB_NAME=damte
ENV DB_USER=postgres
ENV DB_PASSWORD=postgres

CMD ["java", "-jar", "/app/build/libs/fat.jar"]
