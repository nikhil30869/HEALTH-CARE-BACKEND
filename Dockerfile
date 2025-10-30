FROM openjdk:21-jdk-slim
WORKDIR /app
COPY . .
RUN ./mvnw clean package -DskipTests
EXPOSE 2007
CMD ["java", "-jar", "target/*.jar"]
