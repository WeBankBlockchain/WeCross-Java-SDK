package com.webank.wecrosssdk.console.mock;

import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.utils.RPCUtils;
import java.io.Serializable;

public class MockWeCross implements Serializable {
    private static WeCrossRPC weCrossRPC;

    public MockWeCross(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }

    public static MockResource getResource(String path) {
        if (!RPCUtils.isValidPath(path)) {
            System.out.println("Please provide a valid path");
            return null;
        }
        return new MockResource(path, weCrossRPC);
    }

    public WeCrossRPC getWeCrossRPC() {
        return weCrossRPC;
    }

    public void setWeCrossRPC(WeCrossRPC weCrossRPC) {
        this.weCrossRPC = weCrossRPC;
    }
}
