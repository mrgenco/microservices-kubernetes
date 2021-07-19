FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} stream.jar
ENTRYPOINT ["java","-jar","/stream.jar"]