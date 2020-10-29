package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class WeCrossRPCFactory {
    private WeCrossRPCFactory() {}

    public static WeCrossRPC build(WeCrossService weCrossService) throws WeCrossSDKException {
        weCrossService.init();
        return new WeCrossRPCRest(weCrossService);
    }
}
