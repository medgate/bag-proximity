ARG base_image=azul/zulu-openjdk-alpine:11
ARG builder_base_image=gradle:6.2.1-jdk11

FROM $builder_base_image AS builder

COPY --chown=gradle:gradle . /home/gradle
WORKDIR /home/gradle
RUN gradle properties && gradle build

FROM $base_image

EXPOSE 8080

RUN mkdir /app

COPY --from=builder /home/gradle/build/libs/*.jar /app/backend.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-Djava.security.egd=file:/dev/./urandom","-jar", "/app/backend.jar"]
