# Docker image for java app
FROM bitnami/java:1.8
WORKDIR /app
COPY ./target/hapinistay-backend-1.0.0.jar ./
CMD ["java", "-jar", "hapinistay-backend-1.0.0.jar"]
EXPOSE 8080
