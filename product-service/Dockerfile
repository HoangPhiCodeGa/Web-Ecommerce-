# Stage 1: Build the application
#  Start use maven image with jdk 21
FROM  maven:3.9.8-amazoncorretto-21 AS builder



# Set the working directory in the container
WORKDIR /app

# Sao chép settings.xml để sử dụng local repository
COPY ../multi-module-project/docker-maven-settings.xml /root/.m2/settings.xml

# Copy the pom.xml file into the container
COPY pom.xml .
COPY src ./src

# Sử dụng Maven repository từ multi-module-project
COPY --from=multi-module-repo:1.0.0 /maven-repo /root/.m2/repository

# Build all the dependencies to go offline.
#This is a separate step so the dependencies are cached and only rebuilt when the pom.xml file has changed.
RUN mvn package -DskipTests

# Stage 2: Build with amazoncorretto jdk 21
FROM amazoncorretto:21.0.4

# Set the working directory in the container
WORKDIR /app

# Copy the jar file from the builder stage
COPY --from=builder /app/target/*.jar product-service.jar

# Expose the port that the application will run on
EXPOSE 9898

# Run the jar file
ENTRYPOINT ["java","-jar","product-service.jar"]


