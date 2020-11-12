FROM amazoncorretto:11-alpine as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
WORKDIR /workspace
ENV GV="6.7"

RUN wget https://downloads.gradle-dn.com/distributions/gradle-${GV}-bin.zip && \
    unzip gradle-${GV}-bin.zip && \
    ./gradle-${GV}/bin/gradle assemble --no-daemon --warning-mode all

FROM amazoncorretto:11-alpine

COPY --from=builder --chown=nobody:nobody /workspace/build/libs/*.jar /workspace/spodlivoi.jar
ARG TZ="Europe/Moscow"

RUN apk add --no-cache \
        ffmpeg \
        tzdata \
    && \
    cp /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone

WORKDIR /workspace
USER nobody
CMD java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -jar spodlivoi.jar