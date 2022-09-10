#!/bin/bash

[[ $(uname -m) == "x86_64" ]] && FFMPEG_URL=$FFMPEG_AMD64_URL || FFMPEG_URL=$FFMPEG_ARM64_URL
curl -L $FFMPEG_URL | tar -xJ -C /usr/local/bin --strip-components=1