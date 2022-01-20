#!/bin/sh
chmod a+x /bin/gosu
chown -R nobody:nobody /var/log

exec /bin/gosu nobody java -Dfile.encoding=UTF-8 -jar spodlivoi.jar
