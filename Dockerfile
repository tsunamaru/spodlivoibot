FROM amazoncorretto:11-alpine

ARG RUNAS
ARG TOKEN
ARG BOTNAME
ARG TZ

RUN apk add --no-cache \
        ffmpeg \
        tzdata \
    && \
    cp /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo "${TZ}" > /etc/timezone && \
    mkdir -p /opt/spodlivoi && \
    chmod 755 /opt/spodlivoi && \
    chown ${RUNAS} /opt/spodlivoi

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/default-jvm/jre

CMD java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -Dtoken=${TOKEN} -Dbotname=${BOTNAME} -jar spodlivoi.jar