FROM openjdk:17-jdk-slim

WORKDIR /app

COPY target/JAVA-1.0-SNAPSHOT-all.jar /app/DistributedMatrixMultiplication.jar

EXPOSE 5701

CMD ["java", "-jar", "DistributedMatrixMultiplication.jar"]