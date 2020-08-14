package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;

public class ResourceFactory {

    public static Resource build(WeCrossRPC weCrossRPC, String path, String account)
            throws WeCrossSDKException {
        Resource resource = new Resource(weCrossRPC, path, account);
        resource.check();
        return resource;
    }
}
