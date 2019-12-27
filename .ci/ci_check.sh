#!/bin/bash

set -e

#configure WeCrossSdk
#cp src/main/resources/application-sample.yml src/test/resources/application.yml

./gradlew verifyGoogleJavaFormat
./gradlew build
./gradlew test
./gradlew jacocoTestReport
