FROM eclipse-temurin:17

LABEL authors="HICHAM"

WORKDIR /app

COPY hospital_management_backend.jar /app/hospital_management_backend.jar

ENTRYPOINT ["java", "-jar", "hospital_management_backend"]