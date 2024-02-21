FROM maven:3.8.5-openjdk-17-slim
LABEL authors = "elenadejanovic"
WORKDIR /app
COPY . .
EXPOSE 8000
RUN mvn package
COPY --from=build app/target/api-gateway.jar api-gateway.jar
ENTRYPOINT ["java","-jar","api-gateway.jar"]