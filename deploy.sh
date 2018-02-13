#!/bin/bash
d=$(date +%s)
echo "$d"
mv app/build/outputs/apk/debug/*.apk "hna-$d".apk

echo "sending hna app via ftp"
curl -T *.apk ftp://ssrahul96@ftp.drivehq.com/HNA/ -u $FTP_USER:$FTP_PASSWORD
