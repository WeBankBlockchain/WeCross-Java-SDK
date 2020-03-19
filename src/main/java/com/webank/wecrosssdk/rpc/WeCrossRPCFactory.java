package com.webank.wecrosssdk.rpc;

import com.webank.wecrosssdk.rpc.service.WeCrossService;

public class WeCrossRPCFactory {

    public static WeCrossRPC build(WeCrossService weCrossService) throws Exception {
        weCrossService.init();
        return new WeCrossRPCRest(weCrossService);
    }
}
