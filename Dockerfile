FROM openjdk:11.0.12-jre-slim
EXPOSE 8080
ADD target/taskman-api-1.0-SNAPSHOT.jar taskman-api-1.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "taskman-api-1.0-SNAPSHOT.jar"]

