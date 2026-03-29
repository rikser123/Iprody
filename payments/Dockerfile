FROM gradle:8.5-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle

RUN chmod +x gradlew

RUN ./gradlew dependencies --no-daemon --stacktrace

COPY src ./src
RUN ./gradlew bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring


COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
EXPOSE 8443
EXPOSE 5005

ENTRYPOINT ["java", "-jar", "app.jar"]