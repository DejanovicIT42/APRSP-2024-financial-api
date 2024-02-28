FROM maven:3.8.5-openjdk-17-slim as build
LABEL authors = "elenadejanovic"
WORKDIR /app
COPY . .

RUN mvn package

FROM openjdk:17-slim

WORKDIR /app
COPY --from=build app/target/*.jar currency-conversion.jar
EXPOSE 8100
ENTRYPOINT ["java","-jar","currency-conversion.jar"]