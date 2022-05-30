#!/usr/bin/env bash

set -e

docker run -it --rm -v "$(pwd)":"$(pwd)" -w "$(pwd)" gradle:7.4.2-jdk17 gradle assemble --stacktrace --debug
docker buildx build --platform linux/amd64,linux/arm64 -t ghcr.io/tsunamaru/spodlivoibot/ci-build:latest .