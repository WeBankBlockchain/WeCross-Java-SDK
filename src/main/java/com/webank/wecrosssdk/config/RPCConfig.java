package com.webank.wecrosssdk.config;

import com.webank.wecrosssdk.exception.WeCrossSDKException;
import com.webank.wecrosssdk.rpc.WeCrossRPC;
import com.webank.wecrosssdk.rpc.WeCrossRPCFactory;
import com.webank.wecrosssdk.rpc.service.WeCrossRPCService;
import com.webank.wecrosssdk.rpc.service.WeCrossService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.*;

@Configuration
@EnableAspectJAutoProxy
@ComponentScan("com.webank.wecrosssdk")
public class RPCConfig {
    private Logger logger = LoggerFactory.getLogger(RPCConfig.class);

    @Bean
    @Scope(value = "prototype")
    public WeCrossRPC getWeCrossRPC() throws WeCrossSDKException {
        WeCrossRPC weCrossRPC;
        WeCrossService weCrossService = new WeCrossRPCService();
        try {
            weCrossRPC = WeCrossRPCFactory.build(weCrossService);
        } catch (WeCrossSDKException e) {
            logger.error(
                    "WeCrossRPC init error in getWeCrossRPC(), errorCode:{}, error:",
                    e.getErrorCode(),
                    e);
            throw new WeCrossSDKException(
                    e.getErrorCode(),
                    "WeCrossRPC init error in getWeCrossRPC(): " + e.getMessage());
        }
        return weCrossRPC;
    }
}
