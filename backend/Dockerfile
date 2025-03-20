FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /backend
COPY . /backend/.
RUN mvn -f /backend/pom.xml clean package -Dmaven.test.skip=true

FROM eclipse-temurin:21-jdk
WORKDIR /backend
COPY --from=builder /backend/target/*.jar /backend/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/backend/*.jar"]
