# Etapa 1: Compilación con Maven
FROM maven:3.9-eclipse-temurin-11 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen ligera para ejecutar
FROM eclipse-temurin:11-jre
WORKDIR /app
COPY --from=build /app/target/ChatWebServer-uber.jar ./app.jar
COPY public ./public
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]
