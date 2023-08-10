FROM --platform=$BUILDPLATFORM gradle:7-jdk11-alpine AS build
ARG VERSION=0.1.0
WORKDIR /home/gradle/src
COPY . /home/gradle/src
RUN --mount=type=cache,target=/home/gradle/.gradle gradle buildJar -x test
RUN mkdir -p build/dependency && \
    cd build/dependency && \
    jar -xvf ../libs/helicon-$VERSION-SNAPSHOT.jar

FROM --platform=$TARGETPLATFORM amazoncorretto:17-alpine
LABEL maintainer="Richard Stöckl"
RUN adduser --no-create-home --disabled-password heliconuser
USER heliconuser:heliconuser

ARG DEPENDENCY=/home/gradle/src/build/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /helicon/lib
COPY --from=build ${DEPENDENCY}/META-INF /helicon/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /helicon

WORKDIR /helicon
VOLUME /helicon/assets
VOLUME /helicon/application.yml
EXPOSE 2812

CMD ["java", "-cp", "/helicon:/helicon/lib/*", "at.markusnentwich.helicon.HeliconApplicationKt"]
