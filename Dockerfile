FROM openjdk:21-jdk-slim
VOLUME /tmp
ARG JAR_FILE=target/rbac-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
