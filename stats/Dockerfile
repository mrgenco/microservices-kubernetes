FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} stats.jar
ENTRYPOINT ["java","-jar","/stats.jar"]