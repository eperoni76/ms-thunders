# Usa un'immagine base di OpenJDK
FROM openjdk:17-jdk-slim

# Imposta la directory di lavoro nel container
WORKDIR /app

# Copia il file JAR del backend nella directory di lavoro
COPY target/ms-thunders-0.0.1-SNAPSHOT.jar /app/ms-thunders-0.0.1-SNAPSHOT.jar

# Esponi la porta usata dal tuo backend
EXPOSE 8081

# Comando per avviare il backend
ENTRYPOINT ["java", "-jar", "/app/ms-thunders-0.0.1-SNAPSHOT.jar"]
