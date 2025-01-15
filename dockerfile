# Usa una imagen de OpenJDK como base
FROM openjdk:21-jdk-slim

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo .jar generado por Maven o Gradle
COPY target/gotopadel-0.0.1-SNAPSHOT.jar app.jar

# Exponer el puerto que utiliza la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
