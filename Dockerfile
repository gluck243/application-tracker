FROM eclipse-temurin:21-jdk AS build

ENV HOME=/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY pom.xml mvnw ./
COPY .mvn .mvn
RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline
COPY src src
RUN ./mvnw clean package -Pproduction -DskipTests

FROM eclipse-temurin:21-jre-alpine

# VOLUME /tmp

COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app.jar", "--spring.profiles.active=prod"]
