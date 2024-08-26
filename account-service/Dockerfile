FROM openjdk:17-jdk-slim
WORKDIR /project
COPY ./target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]