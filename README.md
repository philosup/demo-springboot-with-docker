# 환경

- Windows 10
- Docker
- Visual Studio Code
- Java
- Springboot
- Gradle


## Visual Studio Code

확장
- Spring Boot Extension Pack

### 프로젝트 생성
Ctrl+Shift+P
>Spring Initializr: Create a Gradle Project
- 2.4.0 <- Spring Boot Version
- Java <- Use language
- com.groupid
- projectid
- JAR <- packaging type
- 11 <- Java version
- Dependencies
  - Spring Boot DevTools
  - Lombok
  - Spring Reactive Web
  - Spring Data JPA SQL

applicaton.properties => application.yml
```yml
spring:
    data:
        mongodb:
            uri: mongodb://localhost:27017
            database: testdb
sample:
    value: default
```

>.\gradlew.bat build

>.\gradlew.bat bootBuildImage --imageName=philosup/demo

>docker build -t philosup/demo .
```yml
version: "3"
services:
  mongodb:
    image: mongo:latest
    environment:
      - MONGO_DATA_DIR=/data/db
      - MONGO_LOG_DIR=/dev/null
    volumes:
      - ./mongodb:/data/db
    container_name: "mongodb_compose"
    ports:
    - 27017:27017
  app:
    image: docker.io/philosup/demo:latest
    ports:
    - 8080:8080
    environment:
      - SPRING_PROFILES_ACTIVE=dev
```