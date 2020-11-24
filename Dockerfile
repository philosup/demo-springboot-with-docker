FROM openjdk:11-jre-slim AS builder
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY src src
RUN chmod +x ./gradlew
RUN ./gradlew bootJar

FROM openjdk:11-jre-slim
ARG JAR_FILE=build/libs/*.jar
COPY --from=builder ${JAR_FILE} app.jar
# COPY ${JAR_FILE} app.jar

ARG ENVIRONMENT
ENV SPRING_PROFILES_ACTIVE=${ENVIRONMENT}

EXPOSE 8080
# ENTRYPOINT ["java","-jar","/app.jar"]

ARG DEPENDENCY=build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java","-cp","app:app/lib/*","com.philosup.demo.DemoApplication"]