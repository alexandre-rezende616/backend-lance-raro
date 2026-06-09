# Estagio 1: O Render usa o Maven oficial para compilar o seu projeto
FROM maven:3.9.6-eclipse-temurin-21 AS build
COPY . /app
WORKDIR /app
RUN mvn clean package -DskipTests

# Estagio 2: O Render cria um computador virtual apenas com Java para rodar o app
FROM eclipse-temurin:21-jre
COPY --from=build /app/target/api-0.0.1-SNAPSHOT.jar /app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app.jar"]