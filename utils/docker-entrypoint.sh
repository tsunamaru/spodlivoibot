#!/bin/bash
chown -R nobody:nogroup /var/log

exec gosu nobody java -Dfile.encoding=UTF-8 -showversion -jar spodlivoi.jar
