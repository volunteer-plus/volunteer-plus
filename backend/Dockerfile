FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /backend
COPY . /backend/.
RUN mvn -f /backend/pom.xml clean package -Dmaven.test.skip=true

FROM openjdk:17-jdk-slim
WORKDIR /backend
COPY --from=builder /backend/target/*.jar /backend/*.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/backend/*.jar"]
