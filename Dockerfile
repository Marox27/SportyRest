# Usa una imagen de Maven para compilar el código
FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
WORKDIR /app

# Copia los archivos del proyecto y compila
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Segunda etapa: imagen final con Corretto
FROM amazoncorretto:17-alpine-jdk
WORKDIR /app

# Copia el JAR generado desde la etapa anterior
COPY --from=build /app/target/SportyRest-0.0.1-SNAPSHOT.jar ApiSportyRest-v1.jar

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/ApiSportyRest-v1.jar"]

