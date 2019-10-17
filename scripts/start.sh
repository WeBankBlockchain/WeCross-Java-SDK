#!/bin/bash

function run_wecrosssdk() {
    if [ "$(uname)" == "Darwin" ]; then
        # Mac
        java -cp 'apps/*:lib/*:conf' com.webank.wecrosssdk.Application
    elif [ "$(uname -s | grep MINGW | wc -l)" != "0" ]; then
        # Windows
        java -cp 'apps/*:lib/*:conf' com.webank.wecrosssdk.Application
    else
        # GNU/Linux
        java -cp 'apps/*;lib/*;conf' com.webank.wecrosssdk.Application
    fi
}

function run_script() {
    run_wecrosssdk
}

run_script
