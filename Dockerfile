FROM openjdk:8-jdk-alpine
WORKDIR /usr/local/app
ADD ./build/libs/kursach-0.0.1-SNAPSHOT.jar app.jar
CMD java -jar ./app.jar