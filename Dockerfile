FROM ibm-semeru-runtimes:open-17-jdk as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
ENV GV=7.3.3
ENV JAVA_OPTS="-Dfile.encoding=UTF-8"
ADD https://downloads.gradle-dn.com/distributions/gradle-${GV}-bin.zip /workspace
WORKDIR /workspace

RUN apt update -qq && \
    apt install -y \
        binutils \
        unzip \
    && \
    unzip gradle-${GV}-bin.zip && \
    rm -f gradle-${GV}-bin.zip && \
    ./gradle-${GV}/bin/gradle assemble --no-daemon

RUN jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM debian:stable-slim

ADD https://github.com/tianon/gosu/releases/download/1.14/gosu-amd64 /bin/gosu
COPY ./utils/docker-entrypoint.sh /bin

RUN cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone && \
    chmod +x /bin/docker-entrypoint.sh

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=builder /javaruntime $JAVA_HOME

COPY --from=builder --chown=nobody:nogroup /workspace/build/libs/*.jar /workspace/spodlivoi.jar

WORKDIR /workspace
ENTRYPOINT /bin/docker-entrypoint.sh
