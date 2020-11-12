FROM amazoncorretto:11-alpine as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
ADD ./utils/gradle-6.7-bin.tar /workspace
WORKDIR /workspace

RUN ./gradle-6.7/bin/gradle assemble --no-daemon --warning-mode all

FROM amazoncorretto:11-alpine

COPY --from=builder --chown=nobody:nobody /workspace/build/libs/*.jar /workspace/spodlivoi.jar

RUN apk add --no-cache -q \
        ffmpeg \
        tzdata \
    && \
    cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone

WORKDIR /workspace
USER nobody
CMD java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -jar spodlivoi.jar