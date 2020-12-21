package com.webank.wecrosssdk.performance;

import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCRest;
import com.webank.wecrosssdk.rpc.methods.response.UAResponse;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeCrossPerfRPCFactory {

    private static final Logger logger = LoggerFactory.getLogger(WeCrossPerfRPCFactory.class);

    public static WeCrossRPC build(String username, String password) throws Exception {

        WeCrossService weCrossService = new WeCrossRPCService();
        weCrossService.init();
        WeCrossRPCRest weCrossRPCRest = new WeCrossRPCRest(weCrossService);
        UAResponse uAResponse = weCrossRPCRest.login(username, password).send();

        logger.info("Login successfully, username: {}, UA: {}", username, uAResponse.getData());

        System.out.println("Login successfully:");
        System.out.println("\t Universal Account:");
        System.out.println("\t username: " + username);
        System.out.println("\t pubKey  : " + uAResponse.getUAReceipt().getCredential());
        System.out.println("\t uaID    : " + uAResponse.getUAReceipt().getCredential());

        return weCrossRPCRest;
    }

    public static WeCrossRPC build() throws Exception {
        return build("org1-admin", "123456");
    }
}
