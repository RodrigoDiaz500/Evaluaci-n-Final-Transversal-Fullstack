# Usa una imagen base de Java (por ejemplo, una con JDK 17)
FROM openjdk:17-jdk-slim

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo .jar a dentro del contenedor
COPY target/bdget-0.0.1-SNAPSHOT.jar app.jar

COPY  Wallet_FKWXNYMCUL59PR4Y /app/oracle_wallet
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
CMD ["java", "-jar", "app.jar"]
