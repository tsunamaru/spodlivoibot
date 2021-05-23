FROM amazoncorretto:15.0.2-alpine as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
ENV GV=6.9
ENV JAVA_OPTS="-Xmx1024m -XX:+HeapDumpOnOutOfMemoryError -Dfile.encoding=UTF-8"
ADD https://downloads.gradle-dn.com/distributions/gradle-${GV}-bin.zip /workspace
WORKDIR /workspace

RUN unzip gradle-${GV}-bin.zip && \
    rm -f gradle-${GV}-bin.zip && \
    ./gradle-${GV}/bin/gradle assemble --no-daemon

FROM amazoncorretto:15.0.2-alpine

RUN apk add --no-cache -q \
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
