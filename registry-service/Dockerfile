FROM openjdk:17-jdk-slim
RUN apt-get update && apt-get install -y curl
WORKDIR /project
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]