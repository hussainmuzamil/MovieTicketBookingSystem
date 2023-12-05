FROM openjdk:17-jdk

WORKDIR /app

#ARG jar_file=*.jar

COPY target/MovieTicketingSystem-0.0.1-SNAPSHOT.jar  /app/MovieTicketingSystem.jar

EXPOSE 8080

CMD ["java","-jar","MovieTicketingSystem.jar"]