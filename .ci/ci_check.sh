#!/bin/bash

set -e

#configure WeCrossSdk
#cp src/main/resources/application-sample.yml src/test/resources/application.yml

curl -LO https://github.com/WeBankBlockchain/WeCross-Java-SDK/releases/download/resources/resources.tar.gz
tar -zxvf resources.tar.gz
cp -r resources src/test/
rm -rf resources

./gradlew verifyGoogleJavaFormat
./gradlew build
./gradlew test
./gradlew jacocoTestReport
