FROM amazoncorretto:11-alpine as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
ADD https://downloads.gradle-dn.com/distributions/gradle-6.7-bin.zip /workspace
WORKDIR /workspace

RUN unzip gradle-6.7-bin.zip && \
    rm -f gradle-6.7-bin.zip && \
    ./gradle-6.7/bin/gradle assemble --no-daemon --warning-mode all

FROM amazoncorretto:11-alpine

RUN apk add --no-cache -q \
        ffmpeg \
        tzdata \
    && \
    cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone && \
    wget https://github.com/tianon/gosu/releases/download/1.12/gosu-amd64 && \
    mv gosu-amd64 /bin/gosu

COPY ./utils/docker-entrypoint.sh /bin
COPY --from=builder --chown=nobody:nobody /workspace/build/libs/*.jar /workspace/spodlivoi.jar

WORKDIR /workspace
ENTRYPOINT [ "/bin/docker-entrypoint.sh" ]