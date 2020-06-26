FROM openjdk:14-alpine

RUN apk add --no-cache maven

WORKDIR /usr/src/santorini

COPY ./src ./src
COPY ./pom.xml .

RUN mvn package

CMD ["java", "-jar", "target/AM8-1.0-jar-with-dependencies.jar", "-s", "0.0.0.0", "5656"]