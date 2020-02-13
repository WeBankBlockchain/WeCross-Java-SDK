#!/bin/bash

set -e

#configure WeCrossSdk
#cp src/main/resources/application-sample.yml src/test/resources/application.yml

curl -LO https://github.com/WeBankFinTech/WeCross-Java-SDK/releases/download/resources/resources.tar.gz
tar -zxvf resources.tar.gz
cp resources src/test/ -r
rm resources -rf

./gradlew verifyGoogleJavaFormat
./gradlew build
./gradlew test
./gradlew jacocoTestReport
