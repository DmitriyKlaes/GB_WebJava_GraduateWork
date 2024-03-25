FROM gradle:8.4-focal as build

WORKDIR /workspace

COPY src ./src
COPY dataset ./dataset
COPY build.gradle.kts ./build.gradle.kts
COPY settings.gradle.kts ./settings.gradle.kts

RUN gradle clean build

FROM bellsoft/liberica-openjdk-debian:17

RUN adduser --system spring-boot && addgroup --system spring-boot && adduser spring-boot spring-boot
USER spring-boot

WORKDIR /app

COPY dataset ./dataset
COPY --from=build /workspace/build/libs/app-0.0.1-SNAPSHOT.jar ./application.jar

ENTRYPOINT ["java", "-jar", "application.jar"]


