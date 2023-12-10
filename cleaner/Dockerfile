FROM openjdk:19

COPY target/cleaner-1.0-SANPSHOT-jar-with-dependencies.jar /app/app.jar
COPY src ./src

WORKDIR /app

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]