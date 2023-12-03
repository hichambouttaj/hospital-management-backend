FROM eclipse-temurin:17

LABEL authors="HICHAM"

WORKDIR /app

COPY target/gestion-hopital-projet-0.0.1-SNAPSHOT.jar /app/gestion-hopital-projet.jar

ENTRYPOINT ["java", "-jar", "gestion-hopital-projet.jar"]