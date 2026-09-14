#DOWNLOADING DEPENDECIES
FROM eclipse-temurin:21-jdk-alpine as dependencies

RUN apk add --no-cache maven

WORKDIR /build

COPY pom.xml .

RUN mvn dependecy:go-offline

#BUILD THE APPLICATION
FROM dependencies AS builder

COPY src ./src

RUN mvn clean package -DskiTests

#RUN THE APPLICATIOM
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]