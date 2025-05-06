FROM amazoncorretto:21.0.7-alpine
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]