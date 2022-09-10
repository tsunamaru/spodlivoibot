FROM ibm-semeru-runtimes:open-17-jdk as builder

RUN apt update -qq && \
    apt install -y \
        binutils

RUN jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /javaruntime

FROM debian:stable-slim

ENV FFMPEG_AMD64_URL https://johnvansickle.com/ffmpeg/builds/ffmpeg-git-amd64-static.tar.xz
ENV FFMPEG_ARM64_URL https://johnvansickle.com/ffmpeg/builds/ffmpeg-git-arm64-static.tar.xz

RUN apt update -y && \
    apt upgrade -y && \
    apt install -y gosu && \
    rm -rf /var/lib/apt/lists/*

COPY ./utils/get-ffmpeg.sh /usr/local/bin/get-ffmpeg

RUN apt update -y && \
    apt install -y curl xz-utils && \
    bash /usr/local/bin/get-ffmpeg && \
    apt remove -y curl xz-utils && \
    rm -rf /var/lib/apt/lists/*

COPY ./utils/docker-entrypoint.sh /bin

RUN cp /usr/share/zoneinfo/Europe/Moscow /etc/localtime && \
    echo "Europe/Moscow" > /etc/timezone && \
    chmod +x /bin/docker-entrypoint.sh

ENV JAVA_HOME=/opt/java/openjdk
ENV PATH "${JAVA_HOME}/bin:${PATH}"
COPY --from=builder /javaruntime $JAVA_HOME

COPY --chown=nobody:nogroup ./build/libs/*.jar /workspace/spodlivoi.jar

WORKDIR /workspace
ENTRYPOINT /bin/docker-entrypoint.sh
