FROM maven:3.8.5-openjdk-17-slim as build
LABEL authors = "elenadejanovic"
WORKDIR /app
COPY . .

RUN mvn package

FROM openjdk:17-slim

WORKDIR /app
COPY --from=build app/target/*.jar naming-server.jar
EXPOSE 8000
ENTRYPOINT ["java","-jar","naming-server.jar"]