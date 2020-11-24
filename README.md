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
            uri: mongodb://mongodb_compose:27017
            database: testdb
```


>docker pull mongo

>docker create -v D:\Work\mongodb\db:/data/db --name mongodb -p 27017:27017 -it mongo:latest

>>WSL2를 사용하여 /mnt/d/Work/mongodb/db로 volume설정을 해보았지만 시작시 에러 발생 하여 윈도우 폴더 명으로 맵핑한다.

>docker start mongodb_server

>.\gradlew.bat build

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
      - D:\Work\mongodb\db:/data/db
    container_name: "mongodb_compose"
    ports:
    - 27017:27017
  app:
    image: philosup/demo
    ports:
    - 8080:8080
```