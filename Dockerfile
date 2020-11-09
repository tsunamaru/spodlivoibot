FROM gradle:6.3-jdk11 as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
WORKDIR /workspace

RUN gradle assemble --no-daemon

FROM amazoncorretto:11-alpine

COPY --from=builder /workspace/build/libs/*.jar /workspace/spodlivoi.jar
ARG TZ="Europe/Moscow"

RUN apk add --no-cache \
        ffmpeg \
        tzdata \
    && \
    cp /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone

ENV LANG C.UTF-8
WORKDIR /workspace
CMD java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -jar spodlivoi.jar