package com.webank.wecrosssdk.resource;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;

public class ResourceFactory {

    public static Resource build(WeCrossRPC weCrossRPC, String path, String accountName)
            throws WeCrossSDKException {
        Resource resource = new Resource(weCrossRPC, path, accountName);
        resource.check();
        return resource;
    }
}
