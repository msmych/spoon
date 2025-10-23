FROM eclipse-temurin:25-jre
COPY build/libs/app-all.jar app.jar
EXPOSE 10000
ENTRYPOINT ["java", "-jar", "/app.jar", "PROD"]