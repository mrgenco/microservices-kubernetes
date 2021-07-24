FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} profile.jar
# Define the network ports that this container will listen on at runtime.8080
EXPOSE 9001
ENTRYPOINT ["java","-jar","/profile.jar"]