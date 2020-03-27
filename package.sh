#!/usr/bin/env bash
./gradlew clean shadowJar

rm -rf build/package
mkdir build/package

docker run --rm --name graal -v $(pwd):/working oracle/graalvm-ce:20.0.0-java11 \
    /bin/bash -c "gu install native-image; \
                  native-image --enable-url-protocols=http \
                  --enable-https \
                    -Djava.net.preferIPv4Stack=true \
                    -H:ReflectionConfigurationFiles=/working/reflect.json \
                    -H:+ReportUnsupportedElementsAtRuntime --no-server -jar /working/build/libs/package-1.0-all.jar \
                    ; \
                    cp package-1.0-all /working/build/package/server"

cp runtime/bootstrap build/package

zip -j build/package/bundle.zip build/package/*
