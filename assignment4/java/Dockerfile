# Usamos una imagen base de OpenJDK
FROM openjdk:17-jdk-slim

# Establecemos el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado y el resto de archivos necesarios al contenedor
COPY target/JAVA-1.0-SNAPSHOT-all.jar /app/DistributedMatrixMultiplication.jar

# Exponemos el puerto predeterminado de Hazelcast (5701) para la comunicación en el cluster
EXPOSE 5701

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "DistributedMatrixMultiplication.jar"]
