FROM adoptopenjdk/openjdk11:alpine-jre

WORKDIR /app

RUN \
  apk add --no-cache \
    ca-certificates \
    tini \
    tzdata

COPY . /app
ENTRYPOINT ["/sbin/tini", "--"]
CMD ["java","-jar","/app/target/Doplarr.jar"]

LABEL "maintainer"="crunchysoft <crunchysoft@gmail.com>"
LABEL "org.opencontainers.image.source"="https://github.com/crunchysoft/Doplarr"
