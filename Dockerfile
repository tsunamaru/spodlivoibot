FROM alpine:3.12

ARG version=8.265.01.2

ARG RUNAS
ARG TOKEN
ARG BOTNAME
ARG TZ

RUN wget -O /etc/apk/keys/amazoncorretto.rsa.pub https://apk.corretto.aws/amazoncorretto.rsa.pub && \
    echo "https://apk.corretto.aws" >> /etc/apk/repositories && \
    apk add --no-cache \ 
        amazon-corretto-8-jre=$version-r0 \
        ffmpeg \
	tzdata \
    && \
    cp /usr/share/zoneinfo/${TZ} /etc/localtime && \
    echo ${TZ} > /etc/timezone && \
    apk del tzdata \
    mkdir -p /opt/spodlivoi && \
    chmod 755 /opt/spodlivoi && \
    chown ${RUNAS} /opt/spodlivoi

ENV LANG C.UTF-8
ENV JAVA_HOME=/usr/lib/jvm/default-jvm/jre

CMD java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -Dtoken=${TOKEN} -Dbotname=${BOTNAME} -jar spodlivoi.jar
