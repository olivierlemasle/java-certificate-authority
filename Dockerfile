FROM maven:3-jdk-8

WORKDIR /usr/src

COPY pom.xml /usr/src/pom.xml
COPY ca-web /usr/src/ca-web
COPY java-ca-lib /usr/src/java-ca-lib

RUN mvn package -DskipTests=true

EXPOSE 8080
CMD ["java", "-jar", "ca-web/target/ca-api.jar", "server", "ca-web/example.yml"]
