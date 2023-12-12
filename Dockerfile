#Maven build
FROM maven:3.6.3-openjdk-17-slim AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

#Run
FROM openjdk:17-jdk-slim
COPY --from=builder /app/target/gestion-hopital-projet-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
