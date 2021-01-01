FROM gradle:jdk15 as build
WORKDIR /home/gradle/src
COPY . /home/gradle/src
RUN gradle buildJar

FROM openjdk:16-alpine
LABEL maintainer="Richard Stöckl"
RUN adduser --no-create-home --disabled-password heliconuser
USER heliconuser:heliconuser

ARG DEPENDENCY=/home/gradle/src/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENTRYPOINT ["java", "-cp", "/app:app/lib/*", "at.markusnentwich.helicon.HeliconApplicationKt"]