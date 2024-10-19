# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-slim

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Add Google's signing key and Chrome's repository
# Update and install dependencies
RUN apt-get update && \
    apt-get upgrade -y && \
    apt-get install -y \
    wget \
    gnupg \
    unzip \
    fonts-liberation \
    libappindicator3-1 \
    libasound2 \
    libatk-bridge2.0-0 \
    libatk1.0-0 \
    libcups2 \
    libdbus-glib-1-2 \
    libgdk-pixbuf2.0-0 \
    libnspr4 \
    libnss3 \
    libxcomposite1 \
    libxrandr2 \
    xdg-utils && \
    apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*


# Install the specific version of Google Chrome
RUN wget -q -O - https://dl.google.com/linux/linux_signing_key.pub | apt-key add - && \
    echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" | tee -a /etc/apt/sources.list.d/google-chrome.list && \
    apt-get update && \
    apt-get install -y google-chrome-stable=129.0.6668.100-1

# Install the specific version of ChromeDriver for Linux
RUN wget -N https://edgedl.me.gvt1.com/edgedl/chrome/chrome-for-testing/129.0.6668.100/linux64/chromedriver-linux64.zip && \
    unzip chromedriver-linux64.zip && \
    mv chromedriver-linux64/chromedriver /usr/local/bin/ && \
    chmod 755 /usr/local/bin/chromedriver && \
    rm -r chromedriver-linux64 chromedriver-linux64.zip


# Expose port if necessary (optional)
EXPOSE 8080

# Copy the JAR file into the container
COPY target/cursol-0.0.1-SNAPSHOT.jar /app/cursol.jar

# Run the application
CMD ["java", "-jar", "/app/cursol.jar"]
