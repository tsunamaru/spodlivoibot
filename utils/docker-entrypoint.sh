#!/bin/bash
chmod a+x /bin/gosu
chown -R nobody:nogroup /var/log

exec /bin/gosu nobody java -Dfile.encoding=UTF-8 -showversion -jar spodlivoi.jar
