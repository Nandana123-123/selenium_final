
FROM maven:3.9.9-eclipse-temurin-17

WORKDIR /app

# Install Chromium and ChromeDriver
RUN apt-get update && \
    apt-get install -y chromium chromium-driver && \
    rm -rf /var/lib/apt/lists/*

# Configure browser and driver paths
ENV CHROME_BIN=/usr/bin/chromium
ENV CHROMEDRIVER_PATH=/usr/bin/chromedriver

# Copy project files
COPY pom.xml .
COPY src ./src
COPY webpage ./webpage
COPY testng.xml .

# Download Maven dependencies
RUN mvn dependency:go-offline

# Execute Selenium TestNG tests
CMD ["mvn", "clean", "test"]
