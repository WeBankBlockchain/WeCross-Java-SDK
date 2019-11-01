package com.webank.wecrosssdk.repl;

import java.io.Serializable;

public class MockWeCross implements Serializable {
    static MockResource getResource(String path) {
        return new MockResource(path);
    }
}
