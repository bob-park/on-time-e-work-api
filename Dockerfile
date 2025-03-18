FROM amazoncorretto:21.0.5
WORKDIR /app

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENV TZ=Asia/Seoul

EXPOSE 8080
HEALTHCHECK --interval=10s --timeout=10s --retries=5 CMD curl --fail http://localhost:8080/api/actuator/health || exit 1

CMD ["java", "-jar", "app.jar"]