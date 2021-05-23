#!/bin/sh
chmod +x /bin/gosu
chown -R nobody:nobody /var/log

exec /bin/gosu nobody java -Xms512m -Xmx1024m -Dfile.encoding=UTF-8 -jar spodlivoi.jar
