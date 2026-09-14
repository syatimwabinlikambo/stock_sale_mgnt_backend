# ============================================================
# 1. DOWNLOAD DEPENDENCIES
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS dependencies

RUN apk add --no-cache maven

WORKDIR /build

COPY pom.xml .

RUN mvn dependency:go-offline


# ============================================================
# 2. BUILD THE APPLICATION
# ============================================================
FROM dependencies AS builder

WORKDIR /build

COPY src ./src

RUN mvn clean package -DskipTests


# ============================================================
# 3. RUN THE APPLICATION
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENTRYPOINT ["java", "-jar", "/app/app.jar"]