#!/bin/sh
chmod +x /bin/gosu
chown -R nobody:nobody /var/log

exec /bin/gosu nobody java -Xms256m -Xmx512m -Dfile.encoding=UTF-8 -jar spodlivoi.jar