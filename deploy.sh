#!/bin/bash
d=$(date +%s)
echo "$d"
mv app/build/outputs/apk/debug/*.apk "debug-$d".apk

curl -T *.apk ftp://ssrahul96@ftp.drivehq.com/HNA/ -u $FTP_USER:$FTP_PASSWORD

rm *.apk

mv app/build/outputs/apk/release/*.apk "release-$d".apk

curl -T *.apk ftp://ssrahul96@ftp.drivehq.com/HNA/ -u $FTP_USER:$FTP_PASSWORD

rm *.apk

echo "apk directory"
cd app/build/outputs/apk/
ls

echo "debug directory"
cd app/build/outputs/apk/debug
ls

echo "release directory"
cd app/build/outputs/apk/release
ls

