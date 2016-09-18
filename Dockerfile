FROM maven:3-jdk-8

WORKDIR /usr/src

COPY pom.xml /usr/src/pom.xml
COPY ca-api /usr/src/ca-api
COPY java-ca-lib /usr/src/java-ca-lib

RUN mvn package -DskipTests=true

EXPOSE 8080
CMD ["java", "-jar", "ca-api/target/ca-api.jar", "server", "ca-api/config.yml"]
