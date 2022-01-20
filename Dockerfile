FROM ibm-semeru-runtimes:open-17-jdk as builder

COPY ./src /workspace/src
COPY ./build.gradle /workspace/build.gradle
ENV GV=7.3.3
ENV JAVA_OPTS="-Dfile.encoding=UTF-8"
ADD https://downloads.gradle-dn.com/distributions/gradle-${GV}-bin.zip /workspace
WORKDIR /workspace

RUN apt update && \
    apt install unzip -y && \
    unzip gradle-${GV}-bin.zip && \
    rm -f gradle-${GV}-bin.zip && \
    ./gradle-${GV}/bin/gradle assemble --no-daemon

FROM ibm-semeru-runtimes:open-17-jre

ADD https://github.com/tianon/gosu/releases/download/1.14/gosu-amd64 /bin/gosu

RUN cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone

COPY ./utils/docker-entrypoint.sh /bin
COPY --from=builder --chown=nobody:nogroup /workspace/build/libs/*.jar /workspace/spodlivoi.jar

WORKDIR /workspace
ENTRYPOINT /bin/bash /bin/docker-entrypoint.sh
