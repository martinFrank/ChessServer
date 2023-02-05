FROM openjdk:17
MAINTAINER baeldung.com
COPY target/ChessServer-1.0-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]