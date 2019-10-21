package com.webank.wecrosssdk.repl;

import java.io.Serializable;

public class MockResource implements Serializable {
    public String path;

    MockResource(String path) {
        this.path = path;
    }

    public String call(String param) {
        return "Call: " + param;
    }

    public String sendTransaction(String param) {
        return "SendTransaction: " + param;
    }
}
