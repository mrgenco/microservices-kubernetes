FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} stream.jar
# Define the network ports that this container will listen on at runtime.8080
EXPOSE 9002
ENTRYPOINT ["java","-jar","/stream.jar"]