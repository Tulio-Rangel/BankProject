FROM openjdk:17-jdk-slim-buster

WORKDIR /app

COPY gradlew .
COPY gradle gradle
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY src src

# Establece permisos de ejecución para gradlew
RUN chmod +x ./gradlew

# Construye la aplicación
RUN ./gradlew build -x test

# Puerto que expone la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java","-jar","build/libs/BankSofka-0.0.1-SNAPSHOT.jar"]