# Primera etapa: Construcción con Maven
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos del proyecto
COPY . .

# Ejecuta Maven para compilar el proyecto
RUN mvn clean package -DskipTests

# Segunda etapa: Imagen final más ligera con OpenJDK
FROM openjdk:21-jdk-slim

WORKDIR /app

# Copia solo el JAR compilado desde la imagen anterior
COPY --from=build /app/target/gotopadel-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto
EXPOSE 8080

# Comando de inicio
ENTRYPOINT ["java", "-jar", "app.jar"]
